package com.example.separation.core.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean


@NoRepositoryBean
interface BaseJpaRepository<T>: JpaRepository<T, Long>