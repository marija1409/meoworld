package com.example.meoworld.features.quiz.question_screen

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.meoworld.core.compose.ConfirmCancelQuizDialog
import kotlinx.coroutines.delay
import com.example.meoworld.core.compose.ErrorMessage
import com.example.meoworld.core.compose.LoadingQuestionsScreen
import com.example.meoworld.core.compose.QuestionScreenContent
import com.example.meoworld.core.compose.QuizFinishedScreen
import com.example.meoworld.features.quiz.Quiz
import com.example.meoworld.features.quiz.Quiz.QuizQuestionEvent
import com.example.meoworld.features.quiz.Quiz.QuizQuestionState

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
        state.creatingQuestions -> LoadingQuestionsScreen()
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

    QuestionScreenContent(
        question = question,
        state = state,
        showCorrectAnswer = showCorrectAnswer,
        answer = answer,
        onAnswerSelected = { answer = it },
        onNextClicked = {
            onNextQuestion(answer)
            answer = ""
        },
        onCancelClicked = { showCancelDialog = true },
        timeLeft = state.timeLeft,
        isQuestionVisible = isQuestionVisible
    )
}








