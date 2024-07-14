package com.sixsense.fleetmanagement.api.v1

import com.sixsense.fleetmanagement.models.SMUser
import com.sixsense.fleetmanagement.models.SMUserInfoDetails
import com.sixsense.fleetmanagement.models.SMUserRegistrationRequest
import com.sixsense.fleetmanagement.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/authentication")
class Authentication(
        private val userService: UserService
){
    private val logger = LoggerFactory.getLogger(Authentication::class.java)

    @PostMapping("/register")
    fun userRegistration(@RequestBody userRegistrationRequest: SMUser): ResponseEntity<Any> {

        logger.info("Request for User Registration $userRegistrationRequest")

        val result = userService.createUser(userRegistrationRequest)
        return when {
            result.isSuccess -> {
                logger.info("User with user details $userRegistrationRequest created successfully")

                ResponseEntity.status(HttpStatus.CREATED).body(result.getOrNull())
            }
            else -> {
                logger.info("User with user details $userRegistrationRequest creation failed")
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result.exceptionOrNull())
            }
        }
    }

    @GetMapping("/{email}/password/reset")
    fun resetPassword(@PathVariable email: String): Any {
        val token = userService.sendResetPasswordEmail(email)

        // TODO: This will be removed once the mail has been setup
        if (token != null) {
            return ResponseEntity.status(HttpStatus.OK).body(mapOf("token" to token))
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}