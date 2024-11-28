package com.example.projetoteste.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.projetoteste.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    // Inserir um novo usuário
    @Insert
    suspend fun insertUser(user: User)

    // Atualizar um usuário existente
    @Update
    suspend fun updateUser(user: User)

    // Deletar um usuário
    @Delete
    suspend fun deleteUser(user: User)

    // Obter todos os usuários
    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>

    // Obter um usuário pelo e-mail
    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    // Função para obter todos os usuários como fluxo
    @Query("SELECT * FROM user")
    fun getAllUsersFlow(): Flow<List<User>>

    // Função para verificar a existência de um usuário pelo e-mail (opcional)
    @Query("SELECT EXISTS(SELECT 1 FROM user WHERE email = :email)")
    suspend fun userExists(email: String): Boolean
}
