package com.example.meoworld.features.quiz

import com.example.meoworld.data.api.CatsApi
import com.example.meoworld.data.database.AppDatabase
import com.example.meoworld.data.database.entities.BreedDbModel
import com.example.meoworld.data.database.entities.ResultDbModel
import com.example.meoworld.data.datastore.UserStore
import com.example.meoworld.features.quiz.Quiz.Question
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.random.Random

class QuizRepository @Inject constructor(
    private val breedsApi: CatsApi,
    private val database: AppDatabase,
    private val store: UserStore
) {

    private var temperaments: List<String> = emptyList()

    suspend fun generateQuestions(): List<Question> = coroutineScope {
        val usedImageUrls = mutableSetOf<String>()
        val questions = mutableListOf<Deferred<Question>>()
        repeat(20) {
            questions += async(Dispatchers.IO) {
                when (Random.nextInt(2)) {
                    0 -> generateTypeOne(usedImageUrls)
                    else -> generateTypeTwo(usedImageUrls)
                }
            }
        }
        questions.awaitAll()
    }

    private suspend fun generateTypeOne(usedImageUrls: MutableSet<String>): Question {
        val breed = getBreedsWithImages().random()
        val imageUrl = getUniqueImageForBreed(breed.id, usedImageUrls)

        val allBreeds = database.breedDao().getAll()
        val answers = allBreeds
            .filter { it.id != breed.id }
            .shuffled()
            .take(3)
            .map { it.name.lowercase() } + breed.name.lowercase()

        return Question(
            text = "Which breed is this?",
            breedId = breed.id,
            breedImageUrl = imageUrl,
            answers = answers.shuffled(),
            correctAnswer = breed.name.lowercase()
        )
    }

    private suspend fun generateTypeTwo(usedImageUrls: MutableSet<String>): Question {
        val breed = getBreedsWithImages().random()
        val imageUrl = getUniqueImageForBreed(breed.id, usedImageUrls)

        val breedTemperaments = breed.temperament.split(",").map { it.trim().lowercase() }
        val allTemperaments = fetchTemperaments().map { it.trim().lowercase() }

        val wrongTemperaments = allTemperaments.filter { it !in breedTemperaments }.shuffled()
        val correctAnswer = wrongTemperaments.random()

        val answers = breedTemperaments.take(3) + correctAnswer

        return Question(
            text = "Which temperament does not belong to this breed?",
            breedId = breed.id,
            breedImageUrl = imageUrl,
            answers = answers.shuffled(),
            correctAnswer = correctAnswer
        )
    }

    private suspend fun getUniqueImageForBreed(
        breedId: String,
        usedImages: MutableSet<String>
    ): String {
        val images = database.imageDao().getAllForBreed(breedId)
        val availableImages = images.filter { it.url !in usedImages }
        val chosen = availableImages.randomOrNull() ?: images.random() // fallback to duplicate if all used
        usedImages += chosen.url
        return chosen.url
    }


    private suspend fun fetchTemperaments(): List<String> {
        if (temperaments.isEmpty()) {
            temperaments = database.breedDao().getAllTemperaments()
        }
        return temperaments
    }

    private suspend fun getBreedsWithImages(): List<BreedDbModel> {
        return database.breedDao().getAll().filter { breed ->
            database.imageDao().countImagesForBreed(breed.id) > 0
        }
    }

    suspend fun submitResultToDatabase(score: Double) = withContext(Dispatchers.IO) {
        database.resultDao().insert(
            ResultDbModel(
                nickname = store.getUserData().nickname,
                result = score,
                createdAt = System.currentTimeMillis(),
                published = false
            )
        )
    }

}
