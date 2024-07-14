package com.sixsense.fleetmanagement.repository

import com.sixsense.fleetmanagement.models.DMRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<DMRole, Int> {
    fun findByNameIgnoreCase(name: String): DMRole?
}