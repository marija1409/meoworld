package com.example.meoworld.features.user.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meoworld.features.user.UserRepo
import com.example.meoworld.features.user.register.Register.RegisterEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: UserRepo,
): ViewModel() {

    private val events = MutableSharedFlow<RegisterEvent>()

    private val _isRegistered = MutableSharedFlow<Boolean>()
    val isRegistered = _isRegistered


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
            events.collect {
                when (it) {
                    is RegisterEvent.Register -> {
                        repository.registerUser(it.asUserData())
                        _isRegistered.emit(true)
                    }
                }
            }
        }
    }

}