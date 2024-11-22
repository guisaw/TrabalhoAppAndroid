package com.example.projetoteste.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projetoteste.database.Repository.UserRepository

class ViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}

