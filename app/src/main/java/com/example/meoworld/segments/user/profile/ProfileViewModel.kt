@file:OptIn(InternalSerializationApi::class)

package com.example.meoworld.segments.user.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meoworld.segments.user.UserRepo
import com.example.meoworld.segments.user.profile.Profile.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepo
): ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private fun setState(reducer: ProfileState.() -> ProfileState) = _state.update(reducer)

    init {
        fetchProfileInfo()
    }

    private fun fetchProfileInfo() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val userData = repository.getUserData()
                val quizResults = repository.getUserResults()
                val bestGlobalRank = repository.getBestGlobalRank()

                setState { copy(
                    userData = userData,
                    bestGlobalRank = bestGlobalRank,
                    totalGamesPlayed = quizResults.size,
                    quizResults = quizResults,

                    fetchingData = false
                )}
            }
        }
    }

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            repository.clearUserData()
            onLogoutComplete()
        }
    }

}