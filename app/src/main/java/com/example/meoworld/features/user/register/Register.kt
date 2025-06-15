package com.example.meoworld.features.user.register

import com.example.meoworld.data.datastore.UserData
import kotlinx.serialization.InternalSerializationApi

interface Register {
    data class RegisterState(
        val isRegister: Boolean = false,
        val error: RegistrationError? = null,
        val firstName: String = "",
        val lastName: String = "",
        val nickname: String = "",
        val email: String = ""
    )


    sealed class RegisterEvent {
        data class UpdateFirstName(val value: String) : RegisterEvent()
        data class UpdateLastName(val value: String) : RegisterEvent()
        data class UpdateNickname(val value: String) : RegisterEvent()
        data class UpdateEmail(val value: String) : RegisterEvent()

        data class Register(
            val firstName: String,
            val lastName: String,
            val nickname: String,
            val email: String
        ) : RegisterEvent() {
            fun asUserData(): UserData = UserData(firstName, lastName, nickname, email)
        }
    }


    sealed class RegistrationError {
        data class RegistrationFailed(val cause: Throwable? = null) : RegistrationError()
    }
}