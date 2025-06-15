package com.example.meoworld.features.user.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.meoworld.core.compose.RegistrationForm
import com.example.meoworld.features.user.register.Register.RegisterEvent
import com.example.meoworld.features.user.register.Register.RegisterState

fun NavGraphBuilder.registerScreen(
    route: String,
    navController: NavController
) = composable(route = route) {

    val registerViewModel = hiltViewModel<RegisterViewModel>()
    val state by registerViewModel.state.collectAsState()

    LaunchedEffect(state.isRegister) {
        if (state.isRegister) {
            navController.navigate("breeds") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    RegisterScreen(
        state = state,
        onEvent = { registerViewModel.setEvent(it) },
        onRegister = { firstName, lastName, nickname, email ->
            registerViewModel.setEvent(RegisterEvent.Register(firstName, lastName, nickname, email))
        },
        errorMessage = state.error?.let {
            when (it) {
                is Register.RegistrationError.RegistrationFailed -> it.cause?.message ?: "Registration failed"
                else -> null
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegister: (String, String, String, String) -> Unit,
    errorMessage: String? = null,
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit
) {
    Surface {
        Column {
            TopAppBar(
                title = { Text("Register") }
            )

            RegistrationForm(
                firstName = state.firstName,
                onFirstNameChange = { onEvent(RegisterEvent.UpdateFirstName(it)) },
                lastName = state.lastName,
                onLastNameChange = { onEvent(RegisterEvent.UpdateLastName(it)) },
                nickname = state.nickname,
                onNicknameChange = { onEvent(RegisterEvent.UpdateNickname(it)) },
                email = state.email,
                onEmailChange = { onEvent(RegisterEvent.UpdateEmail(it)) },
                buttonText = "Register",
                onClick = {
                    onRegister(
                        state.firstName,
                        state.lastName,
                        state.nickname,
                        state.email
                    )
                },
                paddingValues = PaddingValues()
            )

            if (!errorMessage.isNullOrEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
