package com.gplio.fibrewallpaper.lib.logger

interface SystemLogger {

    fun d(tag: String, message: String)

    fun e(tag: String, message: String)
}