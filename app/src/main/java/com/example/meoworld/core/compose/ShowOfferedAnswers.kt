package com.example.meoworld.core.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meoworld.features.quiz.Quiz.Question
import kotlin.collections.chunked
import kotlin.collections.forEach

@Composable
fun ShowOfferedAnswers(
    question: Question,
    selectedAnswer: String,
    onAnswerSelected: (String) -> Unit,
    showCorrectAnswer: Boolean
) {
    val correctAnswer = question.correctAnswer

    Column {
        question.answers.chunked(2).forEach { rowAnswers ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowAnswers.forEach { ans ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp)
                            .clickable { onAnswerSelected(ans) },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            width = 2.dp,
                            color = when {
                                showCorrectAnswer && ans == correctAnswer -> Color(0xFF4CAF50) // Green
                                showCorrectAnswer && ans != correctAnswer -> Color(0xFFF44336) // Red
                                ans == selectedAnswer -> MaterialTheme.colorScheme.primary
                                else -> Color.Transparent
                            }
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ans,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(8.dp),
                                color = MaterialTheme.colorScheme.onSecondary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                    }
                }
            }
        }
    }
}
