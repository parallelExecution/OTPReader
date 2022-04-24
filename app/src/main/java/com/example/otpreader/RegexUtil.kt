package com.example.otpreader

fun otpMatcher(message: String): String? {
    val otpPattern = "\\s\\d{4,8}\\b".toRegex()
    val found = otpPattern.find(message)
    return found?.value
}