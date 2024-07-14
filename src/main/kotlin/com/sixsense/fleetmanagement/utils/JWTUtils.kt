package com.sixsense.fleetmanagement.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.SecretKey
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.milliseconds

@Component
class JWTUtils(
        @Value("\${token-secret}") private val key: String,
        @Value("\${token-expiration}") private val expirationTime: String
) {
    val logger = LoggerFactory.getLogger(JWTUtils::class.java)

    private fun getSigningKey(): SecretKey? {
        return try{
            val keyBytes = Decoders.BASE64.decode(key)
            Keys.hmacShaKeyFor(keyBytes)
        } catch (e: Exception) {
            logger.error("Failed to generate key with the error: $e")
            throw Exception("Unable to generate signing key, due to $e")
        }
    }

    fun generateTokenUsingEmail(email: String): String? {
        return try {
            val secretKey = getSigningKey()
            val currentDatetime = Date()
            val expirationDateTime = Date(currentDatetime.time + expirationTime.toLong())

            Jwts.builder()
                    .subject(email)
                    .issuedAt(currentDatetime)
                    .expiration(expirationDateTime)
                    .signWith(secretKey)
                    .compact()
        } catch (e: Exception) {
            logger.error("Unable to create token, failure reason: $e")
            throw Exception("Unable to create token, due to $e")
        }

    }
    private fun extractAllClaims(token: String): Claims? {
        val key = getSigningKey()
        logger.info("Decrypting the token with the secret key: $key")
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
    }

    fun extractUsername(token: String): String? {
        return extractClaim(token, Claims::getSubject)
    }

    fun extractExpiration(token: String): Date? {
        return extractClaim(token, Claims::getExpiration)
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims!!)
    }

    fun isTokenExpired(jwt: String): Boolean {
        return extractExpiration(jwt)?.before(Date())?: false
    }
}