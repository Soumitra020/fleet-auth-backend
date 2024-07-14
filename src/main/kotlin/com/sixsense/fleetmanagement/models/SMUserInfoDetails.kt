package com.sixsense.fleetmanagement.models

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class SMUserInfoDetails(
        private val user: SMUser,
        private val authorities: Set<GrantedAuthority>? = null,
): UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities?: emptySet()

    override fun getPassword(): String = user.password!!

    override fun getUsername(): String = user.email!!

    override fun isAccountNonExpired(): Boolean = false

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    fun getToken(): String? = user.token
}