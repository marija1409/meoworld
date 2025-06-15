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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.meoworld.core.compose.RegistrationForm
import com.example.meoworld.features.user.register.Register.RegisterEvent

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
    errorMessage: String? = null
) {
    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var lastName by remember { mutableStateOf(TextFieldValue("")) }
    var nickname by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }

    Surface {
        Column {
            TopAppBar(
                title = { Text("Register") }
            )

            RegistrationForm(
                firstName = firstName,
                onFirstNameChange = { firstName = it },
                lastName = lastName,
                onLastNameChange = { lastName = it },
                nickname = nickname,
                onNicknameChange = { nickname = it },
                email = email,
                onEmailChange = { email = it },
                buttonText = "Register",
                onClick = { onRegister(firstName.text, lastName.text, nickname.text, email.text) },
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
