package com.gplio

import android.app.Application
import android.util.Log
import com.gplio.fibrewallpaper.lib.logger.Logger
import com.gplio.fibrewallpaper.lib.logger.SystemLogger

private const val APPLICATION_TAG = "FBR"

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Logger.setLogger(object : SystemLogger {
            override fun d(tag: String, message: String) {
                Log.d(tag, "$APPLICATION_TAG | $message")
            }

            override fun e(tag: String, message: String) {
                Log.e(tag, "$APPLICATION_TAG | $message")
            }
        })
    }
}
