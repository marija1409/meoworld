package com.example.meoworld.core.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun RegistrationForm(
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    nickname: String,
    onNicknameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    buttonText: String,
    onClick: () -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 42.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {

            LineTextField(
                value = firstName,
                onValueChange = { onFirstNameChange(it) },
                label = "First Name",
                regex = Regex("^[a-zA-Z]*$")
            )

            LineTextField(
                value = lastName,
                onValueChange = { onLastNameChange(it) },
                label = "Last Name",
                regex = Regex("^[a-zA-Z]*$")
            )

            LineTextField(
                value = nickname,
                onValueChange = { onNicknameChange(it) },
                label = "Nickname",
                regex = Regex("^[a-zA-Z0-9_]*$")
            )

            LineTextField(
                value = email,
                onValueChange = { onEmailChange(it) },
                label = "Email",
                regex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
            )

            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .align(Alignment.End),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = buttonText, color = Color.White)
            }
        }
    }
}
