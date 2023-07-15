package com.gplio.fibrewallpaper.lib.logger

object Logger{
    private var logger: SystemLogger? = null

    fun setLogger(logger: SystemLogger) {
        this.logger = logger
    }

    fun d(tag: String, method: String, message: String) {
        logger?.d(tag, "$method | $message")
    }

    fun e(tag: String, method: String, message: String) {
        logger?.e(tag, "$method | $message")
    }
}