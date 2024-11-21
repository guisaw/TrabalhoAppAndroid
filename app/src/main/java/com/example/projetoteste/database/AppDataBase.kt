package com.example.projetoteste.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.projetoteste.dao.CotacaoDao
import com.example.projetoteste.dao.UserDao
import com.example.projetoteste.model.Cotacao
import com.example.projetoteste.model.User

@Database(entities = [User::class, Cotacao::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cotacaoDao(): CotacaoDao
}
