package com.example.separation.user.service

import com.example.separation.log.dto.ActionLogDto
import com.example.separation.log.service.ActionLogService
import com.example.separation.user.entity.UserEntity
import com.example.separation.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val actionLogService: ActionLogService,
) {

    @Transactional
    fun createAndSaveUser(email: String, password: String, name: String): Long {
        val user = userRepository.findByEmail(email)
        if (user != null)
            error("User with email $email already registered!")

        actionLogService.logAction(
            ActionLogDto("USER_CREATION", "user with email $email is going to be created!")
        )

        val newUser = UserEntity().apply {
            this.email = email
            this.password = password
            this.name = name
        }.also { userRepository.save(it) }

        actionLogService.logAction(ActionLogDto("USER_CREATION", "user with email $email created!"))

        return newUser.id
    }

}