package com.example.projetoteste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projetoteste.database.AppDatabase
import com.example.projetoteste.database.Repository.UserRepository
import com.example.projetoteste.ui.UserListScreen
import com.example.projetoteste.ui.theme.ProjetoTesteTheme
import com.example.projetoteste.viewmodel.AppViewModel
import com.example.projetoteste.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    // Usando a ViewModel com a Factory
    private val appViewModel: AppViewModel by viewModels {
        ViewModelFactory(
            UserRepository(
                AppDatabase.getInstance(applicationContext).userDao(),
                AppDatabase.getInstance(applicationContext).cotacaoDao(),
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProjetoTesteTheme {
                var isLoginScreen by remember { mutableStateOf(true) }
                var userNome by remember { mutableStateOf("") }

                if (isLoginScreen) {
                    LoginScreen(
                        viewModel = appViewModel,
                        onLoginSuccess = { nome ->
                            // Redireciona para a HomeScreen passando o nome do usuário
                            userNome = nome
                            isLoginScreen = false
                        },
                        onNavigateToCadastro = {
                            // Navegar para a tela de cadastro
                            isLoginScreen = false
                        }
                    )
                } else {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                nome = userNome,
                                onLogout = { isLoginScreen = true },
                                onNavigateToUsers = { navController.navigate("users") }
                            )
                        }
                        composable("users") {
                            UserListScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    viewModel: AppViewModel,
    onLoginSuccess: (String) -> Unit,
    onNavigateToCadastro: () -> Unit
) {
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
            Text("E-mail inválido", color = MaterialTheme.colorScheme.error)
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
            Text("Senha inválida", color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                val user = viewModel.autenticarUsuario(email, senha)
                if (user != null) {
                    val nome = user ?: "Usuário"
                    onLoginSuccess(nome.toString())
                } else {
                    senhaError = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToCadastro,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cadastrar")
        }
    }
}

@Composable
fun HomeScreen(nome: String, onLogout: () -> Unit, onNavigateToUsers: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Bem-vindo, ${nome.ifEmpty { "Usuário" }}!",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 16.dp)
        )

        Button(
            onClick = onNavigateToUsers,
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
        ) {
            Text("Usuários Cadastrados")
        }

        Button(
            onClick = { onLogout() },
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
        ) {
            Text("Sair")
        }
    }
}
