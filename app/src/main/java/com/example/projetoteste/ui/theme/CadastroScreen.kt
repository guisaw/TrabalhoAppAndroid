package com.example.projetoteste

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import android.util.Patterns
import com.example.projetoteste.utils.isValidCpf

@Composable
fun CadastroScreen(
    onCadastroConcluido: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var cpfError by remember { mutableStateOf(false) }

    // Função de validação de email
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Função de validação de CPF
    fun isValidCpf(cpf: String): Boolean {
        return cpf.isValidCpf()  // Certifique-se de que o utilitário 'isValidCpf' está implementado corretamente
    }

    // Função que valida o formulário
    fun isFormValid(): Boolean {
        return isValidEmail(email) && isValidCpf(cpf) && senha.length >= 6
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Campo Nome
        TextField(
            value = nome,
            onValueChange = { novoNome -> nome = novoNome.replaceFirstChar { it.uppercase() } },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo E-mail
        TextField(
            value = email,
            onValueChange = {
                email = it
                emailError = !isValidEmail(email)
            },
            label = { Text("E-mail") },
            isError = emailError,
            modifier = Modifier.fillMaxWidth()
        )
        if (emailError) {
            Text("E-mail inválido", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        // Campo CPF
        TextField(
            value = cpf,
            onValueChange = {
                cpf = it
                cpfError = !isValidCpf(cpf)
            },
            label = { Text("CPF") },
            isError = cpfError,
            modifier = Modifier.fillMaxWidth()
        )
        if (cpfError) {
            Text("CPF inválido", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        // Campo Senha
        TextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            isError = senha.length < 6,
            modifier = Modifier.fillMaxWidth()
        )
        if (senha.length < 6) {
            Text("Senha deve ter pelo menos 6 caracteres", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão de Cadastro
        Button(
            onClick = {
                if (isFormValid()) {
                    onCadastroConcluido()  // Realiza o cadastro
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormValid()  // Habilita o botão apenas se o formulário for válido
        ) {
            Text("Cadastrar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botão para navegação ao Login
        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Já sou cadastrado")
        }
    }
}
