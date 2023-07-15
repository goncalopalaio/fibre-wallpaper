package com.gplio

import android.app.Application
import com.gplio.fibrewallpaper.lib.logger.AndroidLogger
import com.gplio.fibrewallpaper.lib.logger.Logger
import com.gplio.fibrewallpaper.lib.logger.Tagged
import com.gplio.fibrewallpaper.lib.logger.d

class CustomApplication : Application(), Tagged {

    override val tag: String
        get() = "CustomApplication"

    override fun onCreate() {
        super.onCreate()

        Logger.setLogger(AndroidLogger())

        d("onCreate")
    }
}
