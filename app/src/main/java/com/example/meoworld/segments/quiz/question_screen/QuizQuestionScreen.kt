package com.example.meoworld.segments.quiz.question_screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil3.compose.SubcomposeAsyncImage
import com.example.meoworld.R
import kotlinx.coroutines.delay
import com.example.meoworld.core.compose.ErrorMessage
import com.example.meoworld.segments.quiz.Quiz
import com.example.meoworld.segments.quiz.Quiz.Question
import com.example.meoworld.segments.quiz.Quiz.QuizQuestionEvent
import com.example.meoworld.segments.quiz.Quiz.QuizQuestionState

fun NavGraphBuilder.quizQuestionScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {

    val quizQuestionViewModel = hiltViewModel<QuizQuestionViewModel>()
    val state by quizQuestionViewModel.state.collectAsState()

    QuizQuestionScreen(
        state = state,
        onNextQuestion = {
                answer -> quizQuestionViewModel.setEvent(QuizQuestionEvent.NextQuestion(answer))
        },
        publishResult = {
                score -> quizQuestionViewModel.setEvent(QuizQuestionEvent.SubmitResult(score))
            navController.navigate("leaderboard")
        },
        cancelQuiz = { navController.navigate("quiz/start")},
        restartQuiz = { navController.navigate("quiz/start") },
        discoverPage = { navController.navigate("breeds") }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuizQuestionScreen(
    state: QuizQuestionState,
    onNextQuestion: (String) -> Unit,
    publishResult: (Double) -> Unit,
    cancelQuiz: () -> Unit,
    restartQuiz: () -> Unit,
    discoverPage: () -> Unit
) {

    when {
        state.error != null -> {
            val errorMessage = when (state.error) {
                is Quiz.QuizQuestionState.QuizError.QuizFailed ->
                    "Failed to load. Please try again later. Error message: ${state.error.cause?.message}."
            }
            ErrorMessage(errorMessage)
        }
        state.creatingQuestions -> CreatingQuestionsScreen()
        state.quizFinished -> QuizFinishedScreen(
            state = state,
            publishResult = publishResult,
            restartQuiz = restartQuiz,
            discoverPage = discoverPage
        )
        else -> ShowQuestionScreen(
            state = state,
            showCorrectAnswer = state.showCorrectAnswer,
            onNextQuestion = onNextQuestion,
            cancelQuiz = cancelQuiz
        )
    }

}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ShowQuestionScreen(
    state: QuizQuestionState,
    showCorrectAnswer: Boolean,
    onNextQuestion: (String) -> Unit,
    cancelQuiz: () -> Unit,
) {
    val question = state.questions[state.currentQuestionIndex]
    var answer by rememberSaveable { mutableStateOf("") }
    var showCancelDialog by remember { mutableStateOf(false) }
    var isQuestionVisible by remember { mutableStateOf(false) }

    if (state.showCorrectAnswer) {
        isQuestionVisible = true
    }

    LaunchedEffect(key1 = question) {
        isQuestionVisible = false
        delay(700)
        isQuestionVisible = true
    }

    BackHandler(enabled = !showCancelDialog) {
        // Intercept back button ONLY if dialog is NOT already showing
        showCancelDialog = true
    }

    if (showCancelDialog) {
        // When dialog is showing, intercept back press to dismiss dialog instead of closing screen
        BackHandler(enabled = true) {
            showCancelDialog = false
        }

        ConfirmCancelQuizDialog(
            onConfirm = {
                showCancelDialog = false
                cancelQuiz()
            },
            onDismiss = { showCancelDialog = false }
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .statusBarsPadding()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top content (info + timer + question)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuizSessionInfo(state = state)

                    CountdownTimer(
                        timeLeft = state.timeLeft,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AnimatedVisibility(
                        visible = isQuestionVisible,
                        enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(tween(300)),
                        exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(tween(300))
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            QuestionContent(
                                question = question,
                                answer = answer,
                                showCorrectAnswer = showCorrectAnswer,
                                onAnswerSelected = { selected -> answer = selected }
                            )
                        }
                    }
                }

                Row(
                   modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { onNextQuestion(answer); answer = "" },
                        enabled = answer.isNotEmpty(),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(text = "Next Question", color = colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { showCancelDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Cancel Quiz",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionContent(
    question: Question,
    answer: String,
    showCorrectAnswer: Boolean,
    onAnswerSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(4.dp)
        ) {
            SubcomposeAsyncImage(
                model = question.breedImageUrl,
                contentDescription = "Cat Image",
                modifier =  Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        2.dp,
                        colorScheme.secondary,
                        RoundedCornerShape(12.dp)
                    ),
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = question.text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(20.dp))

        ShowOfferedAnswers(
            question = question,
            selectedAnswer = answer,
            onAnswerSelected = onAnswerSelected,
            showCorrectAnswer = showCorrectAnswer
        )
    }
}


@Composable
fun ConfirmCancelQuizDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Are you sure you want to give up?", fontWeight = FontWeight.Bold) },
        text = { Text("Your progress will be lost.") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Yes", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("No")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun ShowOfferedAnswers(
    question: Question,
    selectedAnswer: String,
    onAnswerSelected: (String) -> Unit,
    showCorrectAnswer: Boolean              // when true, show correct answer in green, wrong answers in red
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
                    append("${state.currentQuestionIndex} of ${state.questions.size}")
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


@Composable
fun CountdownTimer(
    timeLeft: Long,
    modifier: Modifier
) {
    val animatedTimeLeft = remember { Animatable(timeLeft.toFloat()) }

    LaunchedEffect(timeLeft) {
        animatedTimeLeft.animateTo(
            targetValue = timeLeft.toFloat(),
            animationSpec = tween(
                durationMillis = 1000, // duration of one second
                easing = LinearEasing
            )
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Timer,
            contentDescription = "Timer",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = String.format("%02d:%02d", animatedTimeLeft.value.toInt() / 60, animatedTimeLeft.value.toInt() % 60),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

@Composable
fun CreatingQuestionsScreen() {
    val randomTexts = listOf(
        "Hang in there, we're conjuring up some purr-ific questions!",
        "Stay whiskered, we're crafting some fur-nomenal queries!",
        "Hold onto your tails, we're tailoring some claw-some questions!",
        "Paws for a moment, we're brewing up some fur-tastic questions!",
        "Don't fur-get to purr-severe, we're fetching some meow-velous questions!"
    )

    val randomText = randomTexts.random()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                //.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.waiting_cat),
                contentDescription = "Loading image",
                modifier = Modifier.size(300.dp)
            )
            Text(
                text = randomText,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }
    }
}

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

@Preview
@Composable
fun QuizFinishedScreenPreview() {
    QuizFinishedScreen(
        state = QuizQuestionState(
            creatingQuestions = false,
            questions = listOf(),
            currentQuestionIndex = 0,
            correctAnswers = 3,
            timeLeft = 300L,
            score = 80.0
        ),
        publishResult = {},
        restartQuiz = {},
        discoverPage = {}
    )
}
