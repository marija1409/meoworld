package com.example.catapult.segments.quiz.start_screen


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.example.meoworld.R.drawable.start_quiz_cat



fun NavGraphBuilder.quizStartScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    QuizStartScreen(
        onStartQuizClick = { navController.navigate("quiz") }
    )
}

@Composable
fun QuizStartScreen(
    onStartQuizClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = start_quiz_cat),
                contentDescription = "Cute cat",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(250.dp)
            )

            Button(
                onClick = {onStartQuizClick()},
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = "Start Quiz",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
            }
        }
    }
}
