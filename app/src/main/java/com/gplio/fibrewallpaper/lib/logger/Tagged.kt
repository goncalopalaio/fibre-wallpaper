package com.gplio.fibrewallpaper.lib.logger

interface Tagged {
    val tag: String
}

fun Tagged.d(method: String, message: String) = Logger.d(tag, method, message)

fun Tagged.e(method: String, message: String) = Logger.d(tag, method, message)

fun Tagged.d(message: String) = Logger.d(tag, "", message)

fun Tagged.e(message: String) = Logger.d(tag, "", message)

