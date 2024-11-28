package com.example.projetoteste

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projetoteste.database.AppDatabase
import com.example.projetoteste.database.Repository.UserRepository
import com.example.projetoteste.ui.UserListScreen
import com.example.projetoteste.ui.theme.ProjetoTesteTheme
import com.example.projetoteste.utils.isValidCpf
import com.example.projetoteste.viewmodel.AppViewModel
import com.example.projetoteste.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    private val appViewModel: AppViewModel by viewModels {
        ViewModelFactory(
            UserRepository(
                AppDatabase.getInstance(applicationContext).userDao(),
                AppDatabase.getInstance(applicationContext).cotacaoDao(),
            )
        )
    }

    private fun shareUser(user: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, user)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, "Compartilhar Usuário"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProjetoTesteTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            viewModel = appViewModel,
                            onLoginSuccess = { email ->
                                val user = appViewModel.getUserByEmail(email)
                                val nome = user?.nome ?: "Usuário"
                                navController.navigate("home/${Uri.encode(nome)}")
                            },
                            onNavigateToCadastro = {
                                navController.navigate("cadastro")
                            }
                        )
                    }

                    composable("cadastro") {
                        CadastroScreen(
                            onCadastroConcluido = {
                                navController.popBackStack()
                            },
                            onNavigateToLogin = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(
                        route = "home/{nome}",
                        arguments = listOf(navArgument("nome") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val nome = backStackEntry.arguments?.getString("nome") ?: "Usuário"
                        HomeScreen(
                            nome = nome,
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToUsers = { navController.navigate("users") }
                        )
                    }

                    composable("users") {
                        UserListScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onShareUser = { user -> shareUser(user) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: AppViewModel, onLoginSuccess: (String) -> Unit, onNavigateToCadastro: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }

    // Função de validação
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

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
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                emailError = !isValidEmail(email)

                if (!emailError) {
                    onLoginSuccess(email)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onNavigateToCadastro() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Criar conta")
        }
    }
}

@Composable
fun CadastroScreenForm(onCadastroConcluido: () -> Unit, onNavigateToLogin: () -> Unit) {
    // implementação
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var cpfError by remember { mutableStateOf(false) }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidCpf(cpf: String): Boolean {
        return cpf.isValidCpf() // Certifique-se que o utilitário de CPF está correto
    }

    fun isFormValid(): Boolean {
        return isValidEmail(email) && isValidCpf(cpf) && senha.length >= 6
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = nome,
            onValueChange = { novoNome -> nome = novoNome.replaceFirstChar { it.uppercase() } },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

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

        Button(
            onClick = {
                if (isFormValid()) {
                    onCadastroConcluido()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormValid()
        ) {
            Text("Cadastrar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Já sou cadastrado")
        }
    }
}

@Composable
fun HomeScreen(nome: String, onLogout: () -> Unit, onNavigateToUsers: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Bem-vindo, $nome!", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onNavigateToUsers() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ir para usuários")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sair")
        }
    }
}
