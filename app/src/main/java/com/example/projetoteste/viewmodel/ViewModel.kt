package com.example.projetoteste.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetoteste.model.User
import com.example.projetoteste.model.Cotacao
import com.example.projetoteste.database.Repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Estado dos usuários
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    // Estado das cotações
    private val _cotacoes = MutableStateFlow<List<Cotacao>>(emptyList())
    val cotacoes: StateFlow<List<Cotacao>> = _cotacoes

    // Carregar os usuários e cotações ao iniciar o ViewModel
    init {
        carregarUsuarios()
        carregarCotacoes()
    }

    // Função para carregar usuários do banco
    private fun carregarUsuarios() {
        viewModelScope.launch {
            _users.value = userRepository.getAllUsers()  // Atualiza a lista de usuários
        }
    }

    // Função para carregar cotações (implementação futura)
    private fun carregarCotacoes() {
        // Supondo que você tenha um repositório ou método para obter cotações
        // _cotacoes.value = cotacaoRepository.getAllCotacoes()
        // Exemplo de implementação:
        // _cotacoes.value = cotacaoRepository.getAllCotacoes() // Atualiza a lista de cotações
    }

    // Função para adicionar um usuário ao banco
    fun adicionarUsuario(user: User) {
        viewModelScope.launch {
            userRepository.addUser(user)  // Adiciona o usuário ao banco
            carregarUsuarios()  // Recarrega os usuários após adicionar
        }
    }

    fun autenticarUsuario(email: String, senha: String): Boolean {
        // Aqui buscamos o usuário na lista de usuários carregados
        val user = _users.value.find { it.email == email }
        return user != null
    }


    // Função para adicionar cotação (implementação futura)
    fun adicionarCotacao(cotacao: Cotacao) {
        // Lógica para adicionar cotação
        // Exemplo:
        // cotacaoRepository.addCotacao(cotacao)
        // carregarCotacoes()  // Recarrega as cotações após adicionar
    }
}
