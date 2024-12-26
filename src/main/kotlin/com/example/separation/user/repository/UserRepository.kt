package com.example.separation.user.repository

import com.example.separation.core.persistence.BaseJpaRepository
import com.example.separation.core.persistence.configuration.MainRepository
import com.example.separation.user.entity.UserEntity

@MainRepository
interface UserRepository: BaseJpaRepository<UserEntity> {
    fun findByEmail(email: String): UserEntity?
}