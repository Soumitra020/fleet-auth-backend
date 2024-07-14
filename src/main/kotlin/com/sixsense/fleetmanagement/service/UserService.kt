package com.sixsense.fleetmanagement.service

import com.sixsense.fleetmanagement.models.*
import com.sixsense.fleetmanagement.repository.RoleRepository
import com.sixsense.fleetmanagement.repository.UserRepository
import com.sixsense.fleetmanagement.utils.JWTUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class UserService(
        @Autowired private val userRepository: UserRepository,
        @Autowired private val roleRepository: RoleRepository,
        @Autowired private val passwordEncoder: PasswordEncoder,
        @Autowired private val jwtUtils: JWTUtils
) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    @Transactional
    fun createUser(smUser: SMUser): Result<SMUserInfoDetails> {
        try {
            logger.info("Creating user with info ${smUser.email}")

            val existingUser = userRepository.findByEmail(smUser.email!!)
            if (existingUser != null) {
                return Result.failure(Exception("User with ${smUser.email} already exists"))
            }

            val user = DMUser.from(smUser, roleRepository, passwordEncoder)
            val savedUser = userRepository.save(user)
            val token = sendResetPasswordEmail(smUser.email)

            val userWithToken = SMUser.from(savedUser, roleRepository).copy(token = token)
            return Result.success(SMUserInfoDetails(userWithToken))
        } catch (e: Exception) {
            logger.error("Failed creating user with email ${smUser.email}, error: ${e.message}")
            throw RuntimeException("Failed creating user with email ${smUser.email}, error: ${e.message}")
        }
    }

    fun sendResetPasswordEmail(email: String): String? {
        return try{
            val token = jwtUtils.generateTokenUsingEmail(email)
            logger.info("Sending email to the email id: $email")
            // TODO: Set email sending logic here

            token
        } catch (e: Exception) {
            logger.error("Send Reset password email failed with error $e")
            throw Exception("Send Reset password email failed with error $e")
        }
    }

    fun updateUserInfo(email:String, password:String) {
        try{
            userRepository.updateDMUserByEmail(email, passwordEncoder.encode(password))
        } catch (exception:Exception) {
            throw Exception("User update transaction failure with exception: $exception")
        }
    }
}