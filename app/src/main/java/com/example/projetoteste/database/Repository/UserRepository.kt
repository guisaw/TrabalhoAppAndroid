package com.example.projetoteste.database.Repository

import com.example.projetoteste.dao.CotacaoDao
import com.example.projetoteste.dao.UserDao
import com.example.projetoteste.model.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao, private val cotacaoDao: CotacaoDao?) {

    // Função para adicionar um usuário no banco
    suspend fun addUser(user: User) {
        userDao.insertUser(user)
    }

    // Função para atualizar um usuário no banco
    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    // Função para deletar um usuário do banco
    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    // Função para recuperar todos os usuários
    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

    // Função para recuperar um usuário pelo e-mail
    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)  // Certificando-se de usar o DAO corretamente e buscar um usuário por email
    }

    // Função para recuperar todos os usuários como um fluxo (para observar atualizações)
    fun getAllUsersFlow(): Flow<List<User>> {
        return userDao.getAllUsersFlow()
    }

    // Função para verificar se um usuário com um e-mail já existe
    suspend fun userExists(email: String): Boolean {
        return getUserByEmail(email) != null  // Retorna true se o usuário com esse e-mail já existir
    }
}
