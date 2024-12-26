package com.example.separation.core.persistence.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.example.separation.core.persistence.configuration.MainJpaConfiguration.Companion.BASE_PACKAGE
import com.example.separation.core.persistence.configuration.MainJpaConfiguration.Companion.ENTITY_MANAGER_FACTORY
import com.example.separation.core.persistence.configuration.MainJpaConfiguration.Companion.TRANSACTION_MANAGER
import com.example.separation.core.utils.ReflectionUtils
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = [BASE_PACKAGE],
        includeFilters = [ComponentScan.Filter(MainRepository::class)],
        entityManagerFactoryRef = ENTITY_MANAGER_FACTORY,
        transactionManagerRef = TRANSACTION_MANAGER
)
class MainJpaConfiguration : MultipleDatabase() {

    companion object {
        const val BASE_PACKAGE = "com.example.separation"
        private val ENTITY_ANNOTATION = MainEntity::class.java
        private const val COMPONENT_NAME = "main"
        private const val ACTUAL_DATA_SOURCE = "${COMPONENT_NAME}ActualDataSource"
        const val DATA_SOURCE_CONFIG = "${COMPONENT_NAME}DataSourceConfig"
        const val JPA_PROPERTIES = "${COMPONENT_NAME}JpaProperties"
        const val ENTITY_MANAGER_FACTORY = "${COMPONENT_NAME}EntityManagerFactory"
        const val TRANSACTION_MANAGER = "${COMPONENT_NAME}TransactionManager"
    }

    @Bean(DATA_SOURCE_CONFIG)
    @ConfigurationProperties("separation.data.$COMPONENT_NAME.hikari")
    fun hikariConfig(): HikariConfig {
        return HikariConfig()
    }


    @Bean(ACTUAL_DATA_SOURCE)
    fun actualDataSource(): DataSource {
        return HikariDataSource(hikariConfig())
    }

    @Primary
    @Bean(JPA_PROPERTIES)
    @ConfigurationProperties("separation.data.$COMPONENT_NAME.jpa")
    fun jpaProperties(): JpaProperties {
        return JpaProperties()
    }

    @Primary
    @Bean(ENTITY_MANAGER_FACTORY)
    override fun entityManagerFactory(
        @Qualifier(ACTUAL_DATA_SOURCE) dataSource: DataSource
    ): LocalContainerEntityManagerFactoryBean {
        val entities = ReflectionUtils.scanAnnotation(ENTITY_ANNOTATION) ?: emptySet()
        return LocalContainerEntityManagerFactoryBean().apply {
            this.dataSource = dataSource
            setManagedTypes(PersistenceManagedTypes.of(*entities.map { it.canonicalName }.distinct().toTypedArray()))
            jpaVendorAdapter = HibernateJpaVendorAdapter()
            setJpaPropertyMap(jpaProperties().properties)
            afterPropertiesSet()
        }
    }

    @Primary
    @Bean(TRANSACTION_MANAGER)
    override fun transactionManager(
            @Qualifier(ENTITY_MANAGER_FACTORY) factoryBean: LocalContainerEntityManagerFactoryBean
    ): PlatformTransactionManager {
        return JpaTransactionManager(checkNotNull(factoryBean.`object`) { "Entity manager factory is null!" })
    }

}