package com.example.meoworld.core.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meoworld.features.quiz.Quiz.Question




@Composable
fun QuestionCard(
    question: Question,
    answer: String,
    showCorrectAnswer: Boolean,
    onAnswerSelected: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer)
    ) {
        QuestionContent(
            question = question,
            answer = answer,
            showCorrectAnswer = showCorrectAnswer,
            onAnswerSelected = onAnswerSelected
        )
    }
}

