package com.example.projetoteste.database.Repository

import com.example.projetoteste.dao.CotacaoDao
import com.example.projetoteste.dao.UserDao
import com.example.projetoteste.model.User

class UserRepository(private val userDao: UserDao, private val cotacaoDao: CotacaoDao?) {
    suspend fun addUser(user: User) = userDao.insertUser(user)
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    suspend fun getAllUsers() = userDao.getAllUsers()

}
