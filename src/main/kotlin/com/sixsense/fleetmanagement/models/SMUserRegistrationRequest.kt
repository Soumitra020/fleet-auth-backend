package com.sixsense.fleetmanagement.models

data class SMUserRegistrationRequest(
        val email: String?,
        val name: String?,
        val password: String?,
        val organisation: String?,
        val roles: String?
)
