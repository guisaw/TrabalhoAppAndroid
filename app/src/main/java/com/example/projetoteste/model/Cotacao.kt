package com.example.projetoteste.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cotacoes_anteriores")
data class Cotacao(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nome_moeda") val nomeMoeda: String,
    @ColumnInfo(name = "valor") val valor: Double,
    @ColumnInfo(name = "data") val data: String
)

