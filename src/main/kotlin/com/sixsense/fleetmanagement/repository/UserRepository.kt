package com.sixsense.fleetmanagement.repository

import com.sixsense.fleetmanagement.models.DMUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserRepository: JpaRepository<DMUser, Int> {
    fun findByEmail(email: String): DMUser?

    @Transactional
    @Modifying
    @Query("UPDATE DMUser d SET d.password = :password WHERE d.email = :email")
    fun updateDMUserByEmail(email: String, password: String)
}