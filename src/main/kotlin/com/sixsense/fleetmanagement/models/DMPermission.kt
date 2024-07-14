package com.sixsense.fleetmanagement.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "permission")
data class DMPermission (
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Int = 0,

        @Column(nullable = false)
        val name: String = "",
){
        companion object {
                fun from(smPermission: SMPermission): DMPermission {
                        return DMPermission(
                                name = smPermission.name!!
                        )
                }
        }
}
