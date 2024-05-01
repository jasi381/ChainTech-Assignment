package com.jasmeet.chaintechassignment.utils

import java.util.Locale

object Utils {
    fun generateStrongPassword(): String {
        val length = 8
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + listOf('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+')
        val password = StringBuilder()


        password.append(allowedChars.random())
        password.append(('A'..'Z').random())
        password.append(('a'..'z').random())
        password.append(('0'..'9').random())


        repeat(length - 4) {
            password.append(allowedChars.random())
        }

        return password.toString().toList().shuffled().joinToString("")
    }
    fun capitalizeFirstLetter(input: String): String {
        if (input.isEmpty()) {
            return input
        }
        return input.substring(0, 1).uppercase(Locale.ROOT) + input.substring(1)
    }

}