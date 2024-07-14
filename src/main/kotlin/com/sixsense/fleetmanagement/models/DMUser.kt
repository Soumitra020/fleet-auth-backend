package com.sixsense.fleetmanagement.models

import com.sixsense.fleetmanagement.repository.RoleRepository
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.*
import org.springframework.security.crypto.password.PasswordEncoder


@Entity
@Table(name="\"user\"")
data class DMUser (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0,

        @Column(nullable = false)
        val name: String = "",

        @Column(nullable = false)
        val email: String = "",

        @Column(nullable = false)
        val password: String = "",

        @Column(nullable = false)
        val organisation: String = "",

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_role",
                joinColumns = [JoinColumn(name = "user_id")],
                inverseJoinColumns = [JoinColumn(name="role_id")]
        )
        val roles: Set<DMRole> = emptySet()
) {

        companion object {
                fun from(smUser: SMUser, roleRepository: RoleRepository?=null, passwordEncoder: PasswordEncoder): DMUser {
                        val roles = smUser.roles?.map {
                                roleRepository?.findByNameIgnoreCase(it.name!!)?: throw IllegalArgumentException("Role with name ${it.name} not found")
                        }
                        return DMUser(
                                name = smUser.name!!,
                                email = smUser.email!!,
                                password = passwordEncoder.encode(smUser.password!!),
                                organisation = smUser.organisation!!,
                                roles = roles?.toSet()?: emptySet()
                        )
                }
        }
}
