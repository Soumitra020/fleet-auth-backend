package com.sixsense.fleetmanagement.api.v1

import com.sixsense.fleetmanagement.models.SMUser
import com.sixsense.fleetmanagement.service.UserInfoDetailService
import com.sixsense.fleetmanagement.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    @Autowired private val userService: UserService,
    @Autowired private val userInfoDetailService: UserInfoDetailService
) {

    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @PatchMapping("/")
    fun updateUserInfo(@RequestBody userInfo: SMUser): ResponseEntity<Any> {
        logger.info("Request received for updating user info $userInfo")
        return try{
            userService.updateUserInfo(userInfo.email!!, userInfo.password!!)
            ResponseEntity(HttpStatus.OK)
        } catch (e:Exception) {
            ResponseEntity(mapOf("error" to e), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/{email}")
    fun getUserDetails(@PathVariable email: String): ResponseEntity<UserDetails> {
        logger.info("Request received for fetching user details for user $email")
        val userDetails = userInfoDetailService.loadUserByUsername(email)

        return ResponseEntity(userDetails, HttpStatus.OK)
    }
}