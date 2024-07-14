package com.sixsense.fleetmanagement.repository

import com.sixsense.fleetmanagement.models.DMPermission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PermissionRepository: JpaRepository<DMPermission, Int>