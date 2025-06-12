package com.example.meoworld.core.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meoworld.features.quiz.Quiz.QuizQuestionState


@Composable
fun QuizSessionInfo(
    state: QuizQuestionState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                    append("Question: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                    append("${state.currentQuestionIndex+1} of ${state.questions.size}")
                }
            }
        )
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle( fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                    append("Correct answers: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                    append("${state.correctAnswers}")
                }
            }
        )
    }
}
