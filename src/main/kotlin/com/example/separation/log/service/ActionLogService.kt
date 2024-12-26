package com.example.separation.log.service

import com.example.separation.core.persistence.configuration.TransactionTemplateFactory.Companion.LOG_TRANSACTION_TEMPLATE
import com.example.separation.log.dto.ActionLogDto
import com.example.separation.log.entity.ActionLogEntity
import com.example.separation.log.repository.ActionLogRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


@Service
class ActionLogService(
    private val actionLogRepository: ActionLogRepository,
    @Qualifier(LOG_TRANSACTION_TEMPLATE) private val transactionTemplate: TransactionTemplate,
) {
    private val logQueue = ArrayBlockingQueue<ActionLogDto>(2_000)
    private val logProducer = Executors.newSingleThreadExecutor()
    private val logConsumer: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    init {
        logConsumer.scheduleWithFixedDelay({ saveLogs() }, 1000L, 500L, TimeUnit.MILLISECONDS)
    }


    fun logAction(log: ActionLogDto) {
        logProducer.submit { logQueue.put(log) }
    }

   private fun saveLogs() {
        transactionTemplate.executeWithoutResult {
            if (logQueue.isEmpty()) return@executeWithoutResult

            (1..minOf(200, logQueue.size)).mapNotNull {
                val log = logQueue.poll() ?: return@mapNotNull null
                ActionLogEntity().apply {
                    this.name = log.name
                    this.content = log.content
                }
            }.also { actionLogRepository.saveAll(it) }
        }
    }

}