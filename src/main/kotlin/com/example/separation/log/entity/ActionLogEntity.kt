package com.example.separation.log.entity

import com.example.separation.core.entity.BaseEntity
import com.example.separation.core.persistence.configuration.LogEntity
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction

@SQLRestriction("deletion_timestamp IS NULL")
@Entity
@LogEntity
@Table(name = "action", indexes = [Index(columnList = "deletion_timestamp")])
class ActionLogEntity: BaseEntity() {
    var name: String? = null
    var content: String? = null
}