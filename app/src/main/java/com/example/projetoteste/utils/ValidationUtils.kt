package com.example.projetoteste.utils

// Função para validar CPF
fun isValidCPF(cpf: String): Boolean {
    return cpf.length == 11 && cpf.all { it.isDigit() }
}

// Função para validar e-mail
fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

// Função para validar nome
fun isValidName(name: String): Boolean {
    return name.matches("^[A-Za-z]+$".toRegex()) && name.first().isUpperCase()
}

// Função para validar senha
fun isValidPassword(password: String): Boolean {
    return password.length >= 8 && password.any { it.isDigit() } && password.any { it.isUpperCase() }
}
