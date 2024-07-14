package com.sixsense.fleetmanagement.config

import com.sixsense.fleetmanagement.service.UserInfoDetailService
import com.sixsense.fleetmanagement.utils.JWTUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*


@Component
class JWTAuthenticationFilter(
    @Autowired val jwtUtils: JWTUtils,
    @Autowired val userDetailsService: UserInfoDetailService
): OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JWTAuthenticationFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")
        var jwt: String = ""
        var username: String? = null
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7)
            log.info("JWT Token: $jwt")
            username = jwtUtils.extractUsername(jwt)
            logger.info("Username againt JWT found: $username")
        }

        if(username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)

            log.info("user details for username $username: ${userDetails.toString()}")

            if(!jwtUtils.isTokenExpired(jwt)) {
                log.info("Token not expired setting the authentication details with ${userDetails.authorities}")
                val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
                println(SecurityContextHolder.getContext().authentication)
            }
        }
        filterChain.doFilter(request, response);
    }
}