package com.example.projetoteste.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projetoteste.dao.CotacaoDao
import com.example.projetoteste.dao.UserDao
import com.example.projetoteste.model.Cotacao
import com.example.projetoteste.model.User

@Database(entities = [User::class, Cotacao::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cotacaoDao(): CotacaoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // This function returns the singleton instance of the database
        fun getInstance(applicationContext: Context): AppDatabase {
            // Check if the database is already created
            return INSTANCE ?: synchronized(this) {
                // If not, create the database instance
                val instance = Room.databaseBuilder(
                    applicationContext.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()

                // Assign the instance to the singleton variable
                INSTANCE = instance
                instance
            }
        }
    }
}
