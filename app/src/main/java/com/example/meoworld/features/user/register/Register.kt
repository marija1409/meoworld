package com.example.meoworld.features.user.register

import com.example.meoworld.data.datastore.UserData
import kotlinx.serialization.InternalSerializationApi

interface Register {
    data class RegisterState(
        val isRegister: Boolean = false,
        val error: RegistrationError? = null,
    )

    sealed class RegisterEvent {
        @OptIn(InternalSerializationApi::class)
        data class Register(
            val firstName: String,
            val lastName: String,
            val nickname: String,
            val email: String
        ) : RegisterEvent() {
            fun asUserData() : UserData {
                return UserData(
                    firstName = firstName,
                    lastName = lastName,
                    nickname = nickname,
                    email = email
                )
            }
        }
    }

    sealed class RegistrationError {
        data class RegistrationFailed(val cause: Throwable? = null) : RegistrationError()
    }
}