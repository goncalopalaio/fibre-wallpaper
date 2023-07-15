package com.gplio.fibrewallpaper

import android.content.IntentFilter
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gplio.fibrewallpaper.lib.logger.Tagged
import com.gplio.fibrewallpaper.lib.logger.d
import com.gplio.fibrewallpaper.lib.observers.ShaderChangeObserver
import com.gplio.fibrewallpaper.main.ChangesBroadcastReceiver
import com.gplio.fibrewallpaper.main.MainRenderer
import com.gplio.fibrewallpaper.values.ACTION_UPDATE_SHADERS

class TestActivity : AppCompatActivity(), ShaderChangeObserver, Tagged {

    override val tag: String
        get() = "TestActivity"

    private val changesBroadcastReceiver = ChangesBroadcastReceiver()

    private var renderer: MainRenderer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val surface = findViewById<GLSurfaceView>(R.id.surface_view)

        renderer = MainRenderer(this@TestActivity)

        surface.preserveEGLContextOnPause = true
        surface.setEGLContextClientVersion(2)
        surface.setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        surface.setRenderer(renderer)
        surface.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

        d("onCreate", "registering receiver")

        IntentFilter(ACTION_UPDATE_SHADERS).also {
            registerReceiver(changesBroadcastReceiver, it)
        }
    }

    override fun onResume() {
        super.onResume()
        d("onResume")

        changesBroadcastReceiver.setListener(this)
    }

    override fun onPause() {
        super.onPause()
        d("onPause")

        // changesBroadcastReceiver.setListener(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        d("onDestroy")

        //unregisterReceiver(changesBroadcastReceiver)
    }

    override fun onShadersChanged(vertexShader: String, fragmentShader: String) {
        renderer?.requestShaderUpdate(vertexShader, fragmentShader)
    }
}