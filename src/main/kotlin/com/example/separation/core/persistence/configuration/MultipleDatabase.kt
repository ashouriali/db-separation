package com.example.separation.core.persistence.configuration

import com.example.separation.core.utils.ReflectionUtils
import jakarta.persistence.Entity
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

abstract class MultipleDatabase {
    init {
        val entityAnnotations = listOf(MainEntity::class.java, LogEntity::class.java)
        val allEntities =
            ReflectionUtils.scanAnnotation(Entity::class.java)?.map { it.canonicalName }?.toSet() ?: emptySet()
        val allSeparatedEntities = entityAnnotations.flatMap {
            ReflectionUtils.scanAnnotation(it)?.map { it.canonicalName }?.toSet() ?: emptySet()
        }

        check(
            allSeparatedEntities.containsAll(allEntities) && allEntities.size == allSeparatedEntities.size
        ) { "Some entities were not separated !" }
    }

    abstract fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean
    abstract fun transactionManager(factoryBean: LocalContainerEntityManagerFactoryBean): PlatformTransactionManager
}