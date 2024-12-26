package com.example.separation.user.entity

import com.example.separation.core.entity.BaseEntity
import com.example.separation.core.persistence.configuration.MainEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction


@SQLRestriction("deletion_timestamp IS NULL")
@Entity
@MainEntity
@Table(indexes = [Index(columnList = "deletion_timestamp")])
class UserEntity: BaseEntity() {
    @Column(nullable = false)
    var name: String? = null

    @Column(nullable = false)
    var password: String? = null

    @Column(unique = true, nullable = false)
    var email: String? = null
}