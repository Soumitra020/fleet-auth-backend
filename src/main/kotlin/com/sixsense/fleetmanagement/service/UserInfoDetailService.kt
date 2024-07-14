package com.sixsense.fleetmanagement.service

import com.sixsense.fleetmanagement.models.DMUser
import com.sixsense.fleetmanagement.models.SMUser
import com.sixsense.fleetmanagement.models.SMUserInfoDetails
import com.sixsense.fleetmanagement.repository.RoleRepository
import com.sixsense.fleetmanagement.repository.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.security.Key


@Service
class UserInfoDetailService(
        @Autowired val userRepository: UserRepository,
        @Autowired val roleRepository: RoleRepository
): UserDetailsService {

    private val logger = LoggerFactory.getLogger(UserInfoDetailService::class.java)

    override fun loadUserByUsername(username: String): UserDetails {
        val userInfo: DMUser = userRepository.findByEmail(username)?: throw UsernameNotFoundException("User with email $username not found")
        logger.info("User info details: email = ${userInfo.email}, roles = ${userInfo.roles}")
        val roles = userInfo.roles

        val authorities = roles.flatMap { role ->
            role.permissions.map { permission ->
                SimpleGrantedAuthority(permission.name)
            }
        }.toSet()

        logger.info("Set Authorities: $authorities")
        return SMUserInfoDetails(user = SMUser.from(userInfo, roleRepository), authorities = authorities)
    }
}
