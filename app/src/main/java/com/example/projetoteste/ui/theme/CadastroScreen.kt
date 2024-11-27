package com.example.projetoteste.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.projetoteste.model.User
import com.example.projetoteste.viewmodel.AppViewModel

@Composable
fun CadastroScreen(
    viewModel: AppViewModel,
    onCadastroSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit // Função de navegação para a tela de login
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    var nomeError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var cpfError by remember { mutableStateOf(false) }
    var senhaError by remember { mutableStateOf(false) }

    // Funções de validação
    fun isValidName(name: String): Boolean {
        return name.isNotEmpty() && name.all { it.isLetter() || it.isWhitespace() } && name[0].isUpperCase()
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidCPF(cpf: String): Boolean {
        return cpf.length == 11 && cpf.all { it.isDigit() }
    }

    fun isValidPassword(password: String): Boolean {
        val minLength = password.length >= 8
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        return minLength && hasUpperCase && hasDigit
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            isError = nomeError,
            modifier = Modifier.fillMaxWidth()
        )
        if (nomeError) {
            Text("Nome inválido", color = Color.Red)
        }

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            isError = emailError,
            modifier = Modifier.fillMaxWidth()
        )
        if (emailError) {
            Text("E-mail inválido", color = Color.Red)
        }

        TextField(
            value = cpf,
            onValueChange = { cpf = it },
            label = { Text("CPF") },
            isError = cpfError,
            modifier = Modifier.fillMaxWidth()
        )
        if (cpfError) {
            Text("CPF inválido", color = Color.Red)
        }

        TextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            isError = senhaError,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        if (senhaError) {
            Text("Senha inválida. A senha deve ter pelo menos 8 caracteres, uma letra maiúscula e um número.", color = Color.Red)
        }

        Button(
            onClick = {
                nomeError = !isValidName(nome)
                emailError = !isValidEmail(email)
                cpfError = !isValidCPF(cpf)
                senhaError = !isValidPassword(senha)

                if (!nomeError && !emailError && !cpfError && !senhaError) {
                    val user = User(cpf = cpf, email = email, nome = nome)
                    viewModel.adicionarUsuario(user)
                    onCadastroSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cadastrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onNavigateToLogin() }, // Navegação para a tela de login
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Já tenho cadastro")
        }
    }
}
