package com.gplio.fibrewallpaper

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gplio.fibrewallpaper.lib.logger.Tagged
import com.gplio.fibrewallpaper.lib.logger.d
import com.gplio.fibrewallpaper.lib.observers.ShaderChangeObserver
import com.gplio.fibrewallpaper.main.MainRenderer

class TestActivity : AppCompatActivity(), Tagged {
    override val tag: String
        get() = "TestActivity"

    private var renderer: MainRenderer? = null
    private val shaderChangeObservers = mutableListOf<ShaderChangeObserver>()

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
    }

    override fun onResume() {
        super.onResume()
        d("onResume")

        renderer?.shaderChangeObserver?.let { shaderChangeObservers.add(it) } // TODO implement broadcast receiver to receive shader updates
    }

    override fun onPause() {
        super.onPause()
        d("onPause")

        renderer?.shaderChangeObserver?.let { shaderChangeObservers.remove(it) }
    }
}