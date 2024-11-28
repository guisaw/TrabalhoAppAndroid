package com.example.projetoteste.ui

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun UserListScreen(onNavigateBack: () -> Unit, onShareUser: (String) -> Unit) {
    val mockUsers = listOf("Usuário 1", "Usuário 2", "Usuário 3") // Dados de exemplo
    val context = LocalContext.current // Contexto para criar a Intent

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        // Título da tela
        Text(
            text = "Usuários Cadastrados",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Lista de usuários
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(mockUsers) { user ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row {
                        Text(
                            text = user,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                        IconButton(onClick = { onShareUser(user) }) {
                            Icon(Icons.Default.Share, contentDescription = "Share")
                        }
                    }

                }
            }
        }

        // Botão para retornar à HomeScreen
        Button(
            onClick = { onNavigateBack() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Retornar")
        }

        // Botão para compartilhar a lista de usuários
        Button(
            onClick = {
                val shareText = mockUsers.joinToString(separator = "\n") // Texto para compartilhar
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)// instanciando objeto pra fazer a caixa de compartilhamento
                }
                context.startActivity(Intent.createChooser(intent, "Compartilhar lista de usuários"))
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Compartilhar Lista")
        }
    }
}
