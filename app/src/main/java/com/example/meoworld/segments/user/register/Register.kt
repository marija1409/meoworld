package com.example.meoworld.segments.user.register

import com.example.meoworld.data.datastore.UserData
import kotlinx.serialization.InternalSerializationApi

interface Register {
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
}