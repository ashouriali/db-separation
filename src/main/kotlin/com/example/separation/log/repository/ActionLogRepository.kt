package com.example.separation.log.repository

import com.example.separation.core.persistence.BaseJpaRepository
import com.example.separation.core.persistence.configuration.LogRepository
import com.example.separation.log.entity.ActionLogEntity

@LogRepository
interface ActionLogRepository: BaseJpaRepository<ActionLogEntity> {

}