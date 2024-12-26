package com.example.separation.core.entity

import jakarta.persistence.*
import java.util.*

@MappedSuperclass
abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "separation_generator")
    @SequenceGenerator(name = "separation_generator", sequenceName = "separation_sequence", allocationSize = 50)
    var id: Long = 0

    var creationDate: Date? = null

    var modificationDate: Date? = null

    @Column(name = "deletion_timestamp")
    var deletionTimestamp: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    @PrePersist
    protected open fun onPrePersist() {
        creationDate = Date()
        modificationDate = Date()
    }

    @PreUpdate
    protected open fun onPreUpdate() {
        if (id == 0L)
            throw EntityNotFoundException()
        modificationDate = Date()
    }
}