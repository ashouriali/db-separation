package com.example.separation

import org.junit.jupiter.api.AfterEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseTestEnvironment {

    @AfterEach
    fun setUp() {

    }

    @AfterEach
    fun tearDown() {

    }

    @LocalServerPort
    var port: Int? = 0


}