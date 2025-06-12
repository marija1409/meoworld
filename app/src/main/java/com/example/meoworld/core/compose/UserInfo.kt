package com.example.meoworld.core.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meoworld.data.datastore.UserData

@Composable
fun UserInfo(
    user: UserData
) {
    Column(modifier = Modifier.padding(bottom = 22.dp)) {
        InformationRow(
            title = "First Name: ",
            value = user.firstName,
        )
        InformationRow(
            title = "Last Name: ",
            value = user.lastName,
        )
        InformationRow(
            title = "Email: ",
            value = user.email,
        )
        InformationRow(
            title = "Nickname: ",
            value = user.nickname,
        )
    }
}