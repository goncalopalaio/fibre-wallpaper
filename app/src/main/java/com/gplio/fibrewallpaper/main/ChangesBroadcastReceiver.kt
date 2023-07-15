package com.gplio.fibrewallpaper.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Base64
import android.widget.Toast
import com.gplio.fibrewallpaper.R
import com.gplio.fibrewallpaper.lib.logger.Tagged
import com.gplio.fibrewallpaper.lib.logger.d
import com.gplio.fibrewallpaper.lib.observers.ShaderChangeObserver
import com.gplio.fibrewallpaper.values.EXTRA_UPDATE_SHADERS_FRAGMENT
import com.gplio.fibrewallpaper.values.EXTRA_UPDATE_SHADERS_VERTEX
import java.nio.charset.Charset

class ChangesBroadcastReceiver : BroadcastReceiver(), Tagged {
    private var listener: ShaderChangeObserver? = null

    override val tag: String
        get() = "ChangesBroadcastReceiver"

    override fun onReceive(context: Context, intent: Intent?) {
        d("onReceive", "context=$context, intent=$intent, listener=$listener")

        if (intent == null) return
        if (listener == null) return

        val vertexShaderBase64 = intent.getStringExtra(EXTRA_UPDATE_SHADERS_VERTEX)
        val fragmentShaderBase64 = intent.getStringExtra(EXTRA_UPDATE_SHADERS_FRAGMENT)

        d(
            "onReceive",
            "vertexShaderBase64=$vertexShaderBase64, fragmentShaderBase64=$fragmentShaderBase64"
        )
        if (vertexShaderBase64 == null || fragmentShaderBase64 == null) return

        val vertexShader = decodeBase64(vertexShaderBase64)
        val fragmentShader = decodeBase64(fragmentShaderBase64)

        Toast.makeText(context, R.string.reloading_shaders, Toast.LENGTH_SHORT).show()
        listener?.onShadersChanged(vertexShader, fragmentShader)
    }

    fun setListener(observer: ShaderChangeObserver?) {
        if (observer == null) return
        listener = observer
    }

    private fun decodeBase64(original: String): String {
        val array = Base64.decode(original, Base64.DEFAULT)

        return String(array, Charset.forName("UTF-8"))
    }
}