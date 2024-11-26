package com.example.projetoteste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetoteste.database.AppDatabase
import com.example.projetoteste.database.Repository.UserRepository
import com.example.projetoteste.model.User
import com.example.projetoteste.ui.theme.ProjetoTesteTheme
import com.example.projetoteste.viewmodel.AppViewModel
import com.example.projetoteste.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    // Usando a ViewModel com a Factory
    private val appViewModel: AppViewModel by viewModels {
        ViewModelFactory(UserRepository(
            AppDatabase.getInstance(applicationContext).userDao(),
            AppDatabase.getInstance(applicationContext).cotacaoDao(),
        ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProjetoTesteTheme {
                var isLoginScreen by remember { mutableStateOf(false) }

                // Navegação entre Cadastro e Login
                if (isLoginScreen) {
                    LoginScreen(viewModel = appViewModel, onLoginSuccess = {
                        // Ação após login bem-sucedido
                    })
                } else {
                    CadastroScreen(viewModel = appViewModel, onCadastroSuccess = {
                        // Navega para a tela de login
                        isLoginScreen = true
                    })
                }
            }
        }
    }
}

@Composable
fun CadastroScreen(viewModel: AppViewModel, onCadastroSuccess: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    var nomeError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var cpfError by remember { mutableStateOf(false) }
    var senhaError by remember { mutableStateOf(false) }

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
            Text("Senha inválida", color = Color.Red)
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
            onClick = { /* Navegar para a tela de login */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Já tenho cadastro")
        }
    }
}

@Composable
fun LoginScreen(viewModel: AppViewModel, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var senhaError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
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
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            isError = senhaError,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        if (senhaError) {
            Text("Senha inválida", color = Color.Red)
        }

        Button(
            onClick = {
                val valid = viewModel.autenticarUsuario(email, senha)
                if (valid) {
                    onLoginSuccess()
                } else {
                    senhaError = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }
    }
}

fun isValidName(name: String): Boolean {
    return name.isNotBlank() && name.all { it.isLetter() } && name[0].isUpperCase()
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidCPF(cpf: String): Boolean {
    return cpf.length == 11 && cpf.all { it.isDigit() }
}

fun isValidPassword(password: String): Boolean {
    return password.length >= 8 && password.any { it.isDigit() } && password.any { it.isUpperCase() }
}

@Composable
fun UserScreen(viewModel: AppViewModel) {
    val users by viewModel.users.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Exibição da lista de usuários
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(users) { user ->
                Text(user.nome)
            }
        }

        // Botão para adicionar um novo usuário
        Button(
            onClick = {
                    viewModel.adicionarUsuario(User(cpf = "123", email = "email@domain.com", nome = "João"))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar Usuário")
        }
    }
}
