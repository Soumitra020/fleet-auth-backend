package com.sixsense.fleetmanagement.models

import jakarta.persistence.*

@Entity
@Table(name = "role")
data class DMRole(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Int = 0,

        @Column(nullable = false)
        val name: String = "",

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "role_permission",
                joinColumns = [JoinColumn(name = "role_id")],
                inverseJoinColumns = [JoinColumn(name = "permission_id")]
        )
        val permissions: Set<DMPermission> = emptySet()
) {
        companion object {
                fun from(smRole: SMRole): DMRole {
                        return DMRole(
                                name = smRole.name!!,
                                permissions = smRole.permissions?.map { DMPermission.from(it) }?.toSet()?: emptySet()
                        )
                }
        }
}
