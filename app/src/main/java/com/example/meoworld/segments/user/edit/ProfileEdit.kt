package com.example.meoworld.segments.user.edit

import com.example.meoworld.data.datastore.UserData
import kotlinx.serialization.InternalSerializationApi

interface ProfileEdit {

    data class ProfileEditState @OptIn(InternalSerializationApi::class) constructor(
        var fetchingData: Boolean = true,
        val userData: UserData = UserData(),
    )

    sealed class ProfileEditEvent {
        data class UpdateProfile(
            val firstName: String,
            val lastName: String,
            val email: String
        ) : ProfileEditEvent() {
            @OptIn(InternalSerializationApi::class)
            fun asUserData() : UserData {
                return UserData(
                    firstName = firstName,
                    lastName = lastName,
                    email = email
                )
            }
        }
    }
}