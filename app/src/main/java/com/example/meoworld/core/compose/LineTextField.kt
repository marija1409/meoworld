package com.example.meoworld.core.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    regex: Regex,
) {
    var isError by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label, style = TextStyle(
                fontSize = 16.sp,
                color = if (isError) Color.Red else MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        BasicTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                isError = !regex.matches(it)
            },
            textStyle = TextStyle(
                fontSize = 18.sp,
                color = if (isError) Color.Red else MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .height(24.dp)
        )

        Divider(color = Color.Gray, thickness = 1.dp)
    }
}
