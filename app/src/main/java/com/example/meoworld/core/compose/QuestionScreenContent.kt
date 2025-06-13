package com.example.meoworld.core.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meoworld.features.quiz.Quiz.Question
import com.example.meoworld.features.quiz.Quiz.QuizQuestionState

@Composable
fun QuestionScreenContent(
    question: Question,
    state: QuizQuestionState,
    showCorrectAnswer: Boolean,
    answer: String,
    onAnswerSelected: (String) -> Unit,
    onNextClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    timeLeft: Long,
    isQuestionVisible: Boolean
) {
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuizSessionInfo(state = state)

                    CountdownTimer(
                        timeLeft = timeLeft,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AnimatedVisibility(
                        visible = isQuestionVisible,
                        enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(tween(300)),
                        exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(tween(300))
                    ) {
                        QuestionCard(
                            question = question,
                            answer = answer,
                            showCorrectAnswer = showCorrectAnswer,
                            onAnswerSelected = onAnswerSelected
                        )
                    }
                }
                QuizButtonsRow(
                    answerNotEmpty = answer.isNotEmpty(),
                    onNextClicked = onNextClicked,
                    onCancelClicked = onCancelClicked
                )
            }
        }
    }
}
