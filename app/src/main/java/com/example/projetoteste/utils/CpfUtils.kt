package com.example.projetoteste.utils

fun String.isValidCpf(): Boolean {
    val cpf = this.replace(".", "").replace("-", "")

    if (cpf.length != 11 || cpf.all { it == cpf[0] }) return false

    val calcDigit = { weight: IntArray, cpf: String ->
        var sum = 0
        for (i in 0 until weight.size) {
            sum += (cpf[i].toString().toInt()) * weight[i]
        }
        val remainder = sum % 11
        if (remainder < 2) 0 else 11 - remainder
    }

    val firstDigit = calcDigit(intArrayOf(10, 9, 8, 7, 6, 5, 4, 3, 2), cpf)
    val secondDigit = calcDigit(intArrayOf(11, 10, 9, 8, 7, 6, 5, 4, 3, 2), cpf)

    return cpf[9].toString().toInt() == firstDigit && cpf[10].toString().toInt() == secondDigit
}
