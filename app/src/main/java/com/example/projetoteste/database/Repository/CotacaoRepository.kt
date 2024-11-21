package com.example.projetoteste.database.Repository

import com.example.projetoteste.dao.CotacaoDao
import com.example.projetoteste.dao.UserDao
import com.example.projetoteste.model.Cotacao
import com.example.projetoteste.model.User

class CotacaoRepository(private val cotacaoDao: CotacaoDao?) {

    suspend fun addCotacao(cotacao: Cotacao) = cotacaoDao?.insertCotacao(cotacao)
    suspend fun updateCotacao(cotacao: Cotacao) = cotacaoDao?.updateCotacao(cotacao)
    suspend fun deleteCotacao(cotacao: Cotacao) = cotacaoDao?.deleteCotacao(cotacao)
    suspend fun getAllCotacoes() = cotacaoDao?.getAllCotacoes()
}
