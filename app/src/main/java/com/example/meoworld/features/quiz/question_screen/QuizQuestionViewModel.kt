package com.example.meoworld.features.quiz.question_screen

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meoworld.features.leaderboard.LeaderboardRepo
import com.example.meoworld.features.quiz.Quiz.QuizQuestionEvent
import com.example.meoworld.features.quiz.Quiz.QuizQuestionState
import com.example.meoworld.features.quiz.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuizQuestionViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val lbRepository: LeaderboardRepo,
) : ViewModel() {

    private val _state = MutableStateFlow(QuizQuestionState())
    val state = _state.asStateFlow()

    private fun setState(reducer: QuizQuestionState.() -> QuizQuestionState) = _state.getAndUpdate(reducer)

    private val events = MutableSharedFlow<QuizQuestionEvent>()

    fun setEvent(event: QuizQuestionEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    private val timer = object : CountDownTimer(5 * 60 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            setState { copy(timeLeft = millisUntilFinished / 1000) }
        }

        override fun onFinish() {
            setEvent(QuizQuestionEvent.TimeUp)
        }
    }

    init {
        observeEvents()
        createQuestions()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is QuizQuestionEvent.NextQuestion -> {
                        val currIndex = state.value.currentQuestionIndex
                        val questions = state.value.questions

                        setState { copy(showCorrectAnswer = true, error = null) }
                        delay(1000)
                        setState { copy(showCorrectAnswer = false) }

                        try {
                            val currentQuestion = questions.getOrNull(currIndex)
                            if (currentQuestion != null && event.selected == currentQuestion.correctAnswer) {
                                setState { copy(correctAnswers = correctAnswers + 1) }
                            }

                            if (currIndex < questions.lastIndex) {
                                val nextIndex = currIndex + 1
                                setState { copy(currentQuestionIndex = nextIndex) }
                            } else {
                                endQuiz()
                            }
                        } catch (e: Exception) {
                            Log.e("QUIZ", "Error transitioning question: ${e.message}", e)
                            setState { copy(error = QuizQuestionState.QuizError.QuizFailed(e)) }
                        }
                    }

                    is QuizQuestionEvent.TimeUp -> endQuiz()

                    is QuizQuestionEvent.SubmitResult -> {
                        viewModelScope.launch {
                            try {
                                lbRepository.submitQuizResult(result = event.score)
                            } catch (e: Exception) {
                                Log.e("QUIZ", "Failed to submit result: ${e.message}", e)
                                setState { copy(error = QuizQuestionState.QuizError.QuizFailed(e)) }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createQuestions() {
        viewModelScope.launch {
            setState { copy(creatingQuestions = true, error = null, isQuizRunning = true) }

            try {
                val questions = withContext(Dispatchers.IO) {
                    quizRepository.generateQuestions()
                }

                setState {
                    copy(questions = questions, creatingQuestions = false)
                }

                timer.start()

            } catch (e: Exception) {
                Log.e("QUIZ", "Failed to create quiz: ${e.message}", e)
                setState { copy(creatingQuestions = false, error = QuizQuestionState.QuizError.QuizFailed(e)) }
            }
        }
    }


    private suspend fun endQuiz() {
        try {
            timer.cancel()
            setState { copy(quizFinished = true, isQuizRunning = false) }

            val score = calculateScore()
            setState { copy(score = score) }

            try {
                quizRepository.submitResultToDatabase(score)
            } catch (e: Exception) {
                Log.e("QUIZ", "Failed to save result to DB: ${e.message}", e)
                setState { copy(error = QuizQuestionState.QuizError.QuizFailed(e)) }
            }

        } catch (e: Exception) {
            Log.e("QUIZ", "Error ending quiz: ${e.message}", e)
            setState { copy(error = QuizQuestionState.QuizError.QuizFailed(e)) }
        }
    }

    private fun calculateScore(): Double {
        return try {
            val correct = state.value.correctAnswers
            val totalDuration = 300
            val remainingTime = state.value.timeLeft.toInt()
            (correct * 2.5 * (1 + (remainingTime + 120) / totalDuration)).coerceAtMost(100.0)
        } catch (e: Exception) {
            Log.e("QUIZ", "Score calculation failed: ${e.message}", e)
            0.0
        }
    }
}
