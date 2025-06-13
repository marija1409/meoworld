package com.example.meoworld.features.user.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
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
    val isRegistered by registerViewModel.isRegistered.collectAsState(initial = false)

    LaunchedEffect(isRegistered) {
        if (isRegistered) {
            navController.navigate("breeds") {
                popUpTo("register") { inclusive = true } 
            }
        }
    }

    RegisterScreen(
        onRegister = { firstName, lastName, nickname, email ->
            registerViewModel.setEvent(RegisterEvent.Register(firstName, lastName, nickname, email))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegister: (String, String, String, String) -> Unit
) {
    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var lastName by remember { mutableStateOf(TextFieldValue("")) }
    var nickname by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }

    Surface {
        Column {
            // TOP BAR
            TopAppBar(
                title = { Text("Register") }
            )

            // CONTENT
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
        }
    }

}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        onRegister = { _, _, _, _ -> }
    )
}
