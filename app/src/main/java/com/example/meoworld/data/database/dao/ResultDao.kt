package com.example.meoworld.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.meoworld.data.database.entities.ResultDbModel

@Dao
interface ResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: ResultDbModel)

    @Query("SELECT * FROM Results ORDER BY createdAt DESC")
    suspend fun getAll(): List<ResultDbModel>

    @Update
    suspend fun update(result: ResultDbModel)

    @Query("SELECT * FROM Results ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastEntry(): ResultDbModel

    @Query("SELECT * FROM Results WHERE nickname = :nickname ORDER BY createdAt DESC")
    suspend fun getUserResults(nickname: String): List<ResultDbModel>


}