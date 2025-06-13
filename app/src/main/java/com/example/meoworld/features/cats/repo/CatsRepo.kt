package com.example.meoworld.features.cats.repo

import com.example.meoworld.core.mappers.asBreedDbModel
import com.example.meoworld.core.mappers.asImageDbModel
import com.example.meoworld.data.api.CatsApi
import com.example.meoworld.data.database.AppDatabase
import com.example.meoworld.data.database.entities.BreedDbModel
import com.example.meoworld.data.database.entities.ImageDbModel
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject

class CatsRepo @Inject constructor(
    private val database: AppDatabase,
    private val breedsApi: CatsApi
) {

    @OptIn(InternalSerializationApi::class)
    suspend fun fetchBreeds() {
        val breeds = breedsApi.getAllBreeds()
        val breedDbModels = breeds.map { it.asBreedDbModel() }
        database.breedDao().insertAll(breedDbModels)

        breedDbModels.forEach { breed ->
            val imageCount = database.imageDao().countImagesForBreed(breed.id)
            if (imageCount == 0) {
                val images = breedsApi.getImagesForBreed(breed.id)
                database.imageDao().insertAll(images.map { it.asImageDbModel(breed.id) })
            }
        }

    }

    suspend fun getBreeds(): List<BreedDbModel> {
        return database.breedDao().getAll()
    }

    fun observeBreeds(): Flow<List<BreedDbModel>> {
        return database.breedDao().observeAll()
    }

    fun observeBreedDetails(breedId: String): Flow<BreedDbModel?> {
        return database.breedDao().observeBreedById(breedId)
    }

    fun observeImagesForBreed(breedId: String): Flow<List<ImageDbModel>> {
        return database.imageDao().observeAllForBreed(breedId)
    }

    suspend fun getImagesForBreed(breedId: String): List<ImageDbModel> {
        return database.imageDao().getAllForBreed(breedId)
    }


}

