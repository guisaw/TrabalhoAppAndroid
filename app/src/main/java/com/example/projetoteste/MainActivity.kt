package com.example.projetoteste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.projetoteste.database.AppDatabase
import com.example.projetoteste.model.User
import com.example.projetoteste.database.Repository.UserRepository
import com.example.projetoteste.ui.theme.ProjetoTesteTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar o banco de dados
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-database"
        ).build()

        // Inicializar o repositório
        val userDao = db.userDao()
        val repository = UserRepository(userDao, cotacaoDao = null)  // Passando null para cotacaoDao, pois não estamos usando cotação aqui

        // Adicionar um usuário diretamente no banco de dados
        GlobalScope.launch {
            val user = User(cpf = "12345678901", email = "email@domain.com", nome = "João")
            repository.addUser(user)  // Adicionando o usuário ao banco

            // Recuperar o usuário inserido
            val userFromDb = repository.getAllUsers().firstOrNull()

            // Atualizar a UI com o nome do usuário
            runOnUiThread {
                setContent {
                    ProjetoTesteTheme {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            Greeting(user = userFromDb, modifier = Modifier.padding(innerPadding))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(user: User?, modifier: Modifier = Modifier) {
    Text(
        text = user?.nome ?: "Nenhum usuário encontrado",  // Exibe o nome do usuário ou uma mensagem se não encontrar.
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProjetoTesteTheme {
        Greeting(user = null)
    }
}
