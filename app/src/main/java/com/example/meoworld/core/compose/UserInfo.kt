package com.example.meoworld.core.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.meoworld.data.datastore.UserData

@Composable
fun UserInfo(
    user: UserData
) {
    Text(
        "Profile Info",
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp),
    )
    Column(modifier = Modifier.padding(bottom = 22.dp)) {
        InformationRow(
            title = "First Name: ",
            value = user.firstName,
            addUnderline = true
        )
        InformationRow(
            title = "Last Name: ",
            value = user.lastName,
            addUnderline = true
        )
        InformationRow(
            title = "Email: ",
            value = user.email,
            addUnderline = true
        )
        InformationRow(
            title = "Nickname: ",
            value = user.nickname,
            addUnderline = true
        )
    }
}