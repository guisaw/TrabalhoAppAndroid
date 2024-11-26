package com.example.projetoteste.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.projetoteste.viewmodel.AppViewModel
import java.lang.reflect.Modifier

class loginScreen {

    @Composable
    fun LoginScreen(viewModel: AppViewModel, onLoginSuccess: () -> Unit) {
        var email by remember { mutableStateOf("") }
        var senha by remember { mutableStateOf("") }
        val emailError by remember { mutableStateOf(false) }
        var senhaError by remember { mutableStateOf(false) }

        Column() {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                isError = emailError,
                //modifier = Modifier.fillMaxWidth()
            )
            if (emailError) {
                Text("E-mail inválido", color = Color.Red)
            }

            TextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                isError = senhaError,
                //modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            if (senhaError) {
                Text("Senha inválida", color = Color.Red)
            }

            Button(
                onClick = {
                    val valid = viewModel.autenticarUsuario(email, senha)
                    if (valid) {
                        onLoginSuccess()  // Chamado se as credenciais forem válidas
                    } else {
                        senhaError = true  // Mostra erro se a senha for inválida
                    }
                },
                //modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar")
            }
        }
    }

}