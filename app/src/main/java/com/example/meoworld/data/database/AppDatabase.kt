package com.example.meoworld.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.meoworld.data.database.dao.BreedDao
import com.example.meoworld.data.database.dao.ImageDao
import com.example.meoworld.data.database.dao.LBItemDao
import com.example.meoworld.data.database.dao.ResultDao
import com.example.meoworld.data.database.entities.BreedDbModel
import com.example.meoworld.data.database.entities.ImageDbModel
import com.example.meoworld.data.database.entities.LBItemDbModel
import com.example.meoworld.data.database.entities.ResultDbModel

@Database(
    entities = [
        BreedDbModel :: class,
        ImageDbModel :: class,
        ResultDbModel :: class,
        LBItemDbModel :: class
    ],
    version = 2,
    exportSchema = true,
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun breedDao(): BreedDao

    abstract fun imageDao(): ImageDao

    abstract fun resultDao(): ResultDao

    abstract fun leaderboardDao(): LBItemDao
}