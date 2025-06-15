package com.example.meoworld.features.user.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meoworld.features.leaderboard.screen.Leaderboard.LeaderboardState
import com.example.meoworld.features.user.UserRepo
import com.example.meoworld.features.user.register.Register.RegisterEvent
import com.example.meoworld.features.user.register.Register.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: UserRepo,
): ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private fun setState(reducer: RegisterState.() -> RegisterState) = _state.update(reducer)

    private val events = MutableSharedFlow<RegisterEvent>()

    fun setEvent(event: RegisterEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    init {
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is RegisterEvent.UpdateFirstName -> setState { copy(firstName = event.value) }
                    is RegisterEvent.UpdateLastName -> setState { copy(lastName = event.value) }
                    is RegisterEvent.UpdateNickname -> setState { copy(nickname = event.value) }
                    is RegisterEvent.UpdateEmail -> setState { copy(email = event.value) }

                    is RegisterEvent.Register -> {
                        setState { copy(isRegister = false, error = null) }
                        try {
                            repository.registerUser(event.asUserData())
                            setState { copy(isRegister = true, error = null) }
                        } catch (e: Throwable) {
                            setState {
                                copy(
                                    isRegister = false,
                                    error = Register.RegistrationError.RegistrationFailed(e)
                                )
                            }
                        }
                    }
                }
            }
        }
    }


}