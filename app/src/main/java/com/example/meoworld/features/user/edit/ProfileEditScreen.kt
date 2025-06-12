package com.example.meoworld.features.user.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.meoworld.core.compose.LoadingIndicator
import com.example.meoworld.core.compose.UserForm
import com.example.meoworld.features.user.edit.ProfileEdit.ProfileEditEvent
import com.example.meoworld.features.user.edit.ProfileEdit.ProfileEditState
import kotlinx.serialization.InternalSerializationApi

fun NavGraphBuilder.profileEditScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {

    val profileEditViewModel = hiltViewModel<ProfileEditViewModel>()
    val state by profileEditViewModel.state.collectAsState()

    ProfileEditScreen(
        state = state,
        onBack = { navController.popBackStack() },
        onSave = { firstName, lastName, email ->
            profileEditViewModel.setEvent(ProfileEditEvent.UpdateProfile(firstName, lastName, email))
            navController.navigate("profile")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    state: ProfileEditState,
    onBack: () -> Unit,
    onSave: (String, String, String) -> Unit,
) {
    val scrollState = rememberScrollState()

    Surface {
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = colorScheme.primary
                        )
                    }
                },
                title = {
                    Text(
                        text = "Edit profile",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )

            if (state.fetchingData) {
                LoadingIndicator()
            } else {
                var firstName by remember { mutableStateOf(TextFieldValue(state.userData.firstName)) }
                var lastName by remember { mutableStateOf(TextFieldValue(state.userData.lastName)) }
                var email by remember { mutableStateOf(TextFieldValue(state.userData.email)) }

                Text(
                    text = "Username: ${state.userData.nickname}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                UserForm(
                    firstName = firstName,
                    onFirstNameChange = { firstName = it },
                    lastName = lastName,
                    onLastNameChange = { lastName = it },
                    email = email,
                    onEmailChange = { email = it },
                    buttonText = "Edit Profile",
                    onClick = {
                        onSave(firstName.text, lastName.text,  email.text)
                    },
                    paddingValues = PaddingValues()
                )
            }
        }
    }
}
