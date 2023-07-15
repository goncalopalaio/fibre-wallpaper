package com.gplio.fibrewallpaper.lib.logger

import android.util.Log

private const val APPLICATION_TAG = "FBR"

class AndroidLogger : SystemLogger {
    override fun d(tag: String, message: String) {
        Log.d(tag, "$APPLICATION_TAG | $message")
    }

    override fun e(tag: String, message: String) {
        Log.e(tag, "$APPLICATION_TAG | $message")
    }
}