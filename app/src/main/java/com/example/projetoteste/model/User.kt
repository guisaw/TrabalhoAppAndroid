package com.example.projetoteste.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "cpf") val cpf: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "nome") val nome: String,
    @ColumnInfo(name = "senha") val senha: String  // Adicionando o campo "senha"
)
