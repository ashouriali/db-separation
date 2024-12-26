package com.example.separation.core.persistence.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

@Configuration
class TransactionTemplateFactory {
    companion object {
        const val MAIN_TRANSACTION_TEMPLATE = "mainTransactionTemplate"
        const val LOG_TRANSACTION_TEMPLATE = "logTransactionTemplate"
    }

    @Primary
    @Bean(MAIN_TRANSACTION_TEMPLATE)
    fun mainTransactionTemplate(
        @Qualifier(MainJpaConfiguration.TRANSACTION_MANAGER) transactionManager: PlatformTransactionManager
    ): TransactionTemplate {
        return TransactionTemplate(transactionManager)
    }

    @Bean(LOG_TRANSACTION_TEMPLATE)
    fun logTransactionTemplate(
        @Qualifier(LogJpaConfiguration.TRANSACTION_MANAGER) transactionManager: PlatformTransactionManager
    ): TransactionTemplate {
        return TransactionTemplate(transactionManager)
    }
}