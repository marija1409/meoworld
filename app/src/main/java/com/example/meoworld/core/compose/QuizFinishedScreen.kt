package com.example.meoworld.core.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.meoworld.R
import com.example.meoworld.features.quiz.Quiz.QuizQuestionState

@Composable
fun QuizFinishedScreen(
    state: QuizQuestionState,
    publishResult: (Double) -> Unit,
    restartQuiz: () -> Unit,
    discoverPage: () -> Unit
) {
    Surface (
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.2F))

            Text(
                text = "Quiz Finished!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.happy_cat),
                contentDescription = "Quiz finished image",
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = "Correct answers: ${state.correctAnswers}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )
            Text(
                text = "Score: ${state.score}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Button(onClick = { publishResult(state.score) }) {
                    Text(text = "Publish Result", color = Color.White)
                }
                Button(onClick = { restartQuiz() }) {
                    Text(text = "Restart Quiz", color = Color.White)
                }
            }

            Button(
                onClick = { discoverPage() },
                modifier = Modifier.padding(bottom = 16.dp)
            ) { Text(text = "Discover More Breeds", color = Color.White) }
        }
    }
}
