package com.example.separation.container

import com.example.separation.BaseTestEnvironment
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
class SeparationPostgreSQLContainer : BaseTestEnvironment() {


    @Container
    private val postgresqlContainer = PostgreSQLContainer("postgres:17-alpine")
        .withUsername("sa")
        .withPassword("sa")


    @Test
    fun `check if postgreSQL container is running`() {
        assertThat(postgresqlContainer.isRunning).isTrue()
    }

}