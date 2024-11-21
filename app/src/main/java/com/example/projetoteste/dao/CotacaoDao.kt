package com.example.projetoteste.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.projetoteste.model.Cotacao

@Dao
interface CotacaoDao {
    @Insert
    suspend fun insertCotacao(cotacao: Cotacao)

    @Update
    suspend fun updateCotacao(cotacao: Cotacao)

    @Delete
    suspend fun deleteCotacao(cotacao: Cotacao)

    @Query("SELECT * FROM cotacoes_anteriores")
    suspend fun getAllCotacoes(): List<Cotacao>

    @Query("SELECT * FROM cotacoes_anteriores WHERE id = :cotacaoId LIMIT 1")
    suspend fun getCotacaoById(cotacaoId: Int): Cotacao?
}
