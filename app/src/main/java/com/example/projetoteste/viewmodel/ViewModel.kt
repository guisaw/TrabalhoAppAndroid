package com.example.projetoteste.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetoteste.model.User
import com.example.projetoteste.database.Repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Estado dos usuários
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    // Estado de carregamento dos usuários
    private val _isUsersLoaded = MutableStateFlow(false)
    val isUsersLoaded: StateFlow<Boolean> = _isUsersLoaded

    // Estado de erro ao carregar usuários
    private val _errorLoadingUsers = MutableStateFlow<String?>(null)
    val errorLoadingUsers: StateFlow<String?> = _errorLoadingUsers

    // Carregar os usuários ao iniciar o ViewModel
    init {
        carregarUsuarios()
    }

    // Função para carregar usuários do banco
    private fun carregarUsuarios() {
        viewModelScope.launch {
            try {
                _users.value = userRepository.getAllUsers()  // Atualiza a lista de usuários
                _isUsersLoaded.value = true  // Define que os usuários foram carregados
                _errorLoadingUsers.value = null  // Limpa o erro, se houver
            } catch (e: Exception) {
                _isUsersLoaded.value = false
                _errorLoadingUsers.value = "Erro ao carregar usuários: ${e.message}"  // Mensagem de erro
            }
        }
    }

    // Função para autenticar o usuário com email e senha
    fun autenticarUsuario(email: String, senha: String): Boolean {
        // Se os usuários ainda não foram carregados ou não existem, recarrega
        if (!_isUsersLoaded.value || _users.value.isEmpty()) {
            carregarUsuarios()  // Carrega os usuários do banco
            return false  // Retorna false temporariamente até carregar
        }

        // Realiza a busca pelo email
        val user = _users.value.find { it.email == email }
        return user?.senha == senha  // Valida a senha do usuário encontrado
    }

    // Função para adicionar um usuário ao banco
    fun adicionarUsuario(user: User) {
        viewModelScope.launch {
            try {
                userRepository.addUser(user)  // Adiciona o usuário ao banco
                carregarUsuarios()  // Recarrega os usuários após adicionar
            } catch (e: Exception) {
                _errorLoadingUsers.value = "Erro ao adicionar usuário: ${e.message}"  // Mensagem de erro
            }
        }
    }

    // Função para salvar um novo usuário
    fun saveUser(user: User) {
        viewModelScope.launch {
            try {
                // Verifica se o email já está cadastrado
                val existingUser = getUserByEmail(user.email)
                if (existingUser != null) {
                    _errorLoadingUsers.value = "E-mail já cadastrado!"
                } else {
                    userRepository.addUser(user)  // Adiciona o usuário ao banco
                    carregarUsuarios()  // Recarrega a lista de usuários após salvar
                }
            } catch (e: Exception) {
                _errorLoadingUsers.value = "Erro ao salvar usuário: ${e.message}"  // Mensagem de erro
            }
        }
    }

    // Função para recuperar um usuário pelo email
    fun getUserByEmail(email: String): User? {
        return _users.value.find { it.email == email }
    }
}
