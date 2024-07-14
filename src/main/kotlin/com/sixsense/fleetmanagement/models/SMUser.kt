package com.sixsense.fleetmanagement.models

import com.sixsense.fleetmanagement.repository.RoleRepository

data class SMUser(
        val email: String?,
        val name: String?,
        val password: String?,
        val organisation: String?,
        val roles: Set<SMRole>? = emptySet(),
        val token: String? = null
) {
    companion object {
        fun from(dmUser: DMUser, roleRepository: RoleRepository?=null): SMUser {
            val roles = dmUser.roles.map {
                roleRepository?.findByNameIgnoreCase(it.name)?: throw IllegalArgumentException("Role with name ${it.name} not found")
                SMRole(
                        name = it.name,
                        permissions = it.permissions.map { dmPermission ->
                            SMPermission(name = dmPermission.name)
                        }
                )
            }

            return SMUser(
                    name = dmUser.name,
                    email = dmUser.email,
                    password = dmUser.password,
                    organisation = dmUser.organisation,
                    roles = roles.toSet()
            )
        }
    }
}
