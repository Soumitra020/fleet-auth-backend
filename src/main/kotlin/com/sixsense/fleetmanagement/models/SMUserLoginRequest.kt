package com.sixsense.fleetmanagement.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.jetbrains.annotations.NotNull

data class SMUserLoginRequest (

        @JsonProperty("email")
        @NotNull
        val username: String,

        @JsonProperty("password")
        @NotNull
        val password: String,
)
