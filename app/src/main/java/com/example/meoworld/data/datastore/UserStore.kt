package com.example.meoworld.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStore @Inject constructor(
    private val persistence: DataStore<UserData>,
){

    private val scope = CoroutineScope(Dispatchers.IO)

    val userData: Flow<UserData> = persistence.data
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UserData()
        )

    suspend fun isUserRegistered(): Boolean {
        return try {
            val user = persistence.data.first()
            user.email.isNotEmpty() &&
                    user.firstName.isNotEmpty() &&
                    user.lastName.isNotEmpty() &&
                    user.nickname.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserData(): UserData {
        return try {
            persistence.data.first()
        } catch (e: Exception) {
            UserData()
        }
    }

    suspend fun setData(data: UserData) {
        try {
            persistence.updateData { data }
        } catch (e: Exception) {
            Log.d("CATAPULT", "Error setting data: $e")
        }
    }


    suspend fun deleteUser() {
        try {
            persistence.updateData { UserData() }
        } catch (e: Exception) {
            Log.d("CATAPULT", "Error deleting data: $e")
        }
    }
}