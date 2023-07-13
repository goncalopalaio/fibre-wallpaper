package com.gplio.fibrewallpaper.main

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import com.gplio.fibrewallpaper.lib.files.readStringFromAssets
import com.gplio.fibrewallpaper.lib.graphics.CustomShape
import com.gplio.fibrewallpaper.lib.graphics.LiveShader
import com.gplio.fibrewallpaper.lib.graphics.QuadShape
import com.gplio.fibrewallpaper.lib.graphics.Vec3
import com.gplio.fibrewallpaper.lib.graphics.lookAt
import com.gplio.fibrewallpaper.lib.graphics.matrix4
import com.gplio.fibrewallpaper.lib.observers.ShaderChangeObserver
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MainRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private var height = 0
    private var width = 0
    private val shader: LiveShader
    private var time = 0f
    private var generalShapes: List<CustomShape> = emptyList()
    private val viewMatrix = matrix4()
    private val projectionMatrix = matrix4()
    private val viewProjectionMatrix = matrix4()
    private val left = -1f
    private val right = 1f
    private val bottom = -1f
    private val top = 1f
    private val near = -1f
    private val far = 100f

    private val camera = Camera()

    init {

        // just leave it like this for now
        val previousVertexShader = readStringFromAssets(context, "shaders/current.vert", "")
        val previousFragmentShader = readStringFromAssets(context, "shaders/current.frag", "")
        shader = LiveShader(previousVertexShader, previousFragmentShader)

        /*textShader = new TextShader();*/
    }

    override fun onSurfaceCreated(gl10: GL10, eglConfig: EGLConfig) {
        shader.init()
        time = 0f
    }

    override fun onSurfaceChanged(gl10: GL10, w: Int, h: Int) {
        GLES20.glClearColor(0.1f, 0.4f, 1.0f, 1.0f)
        width = w
        height = h
        generalShapes = listOf(QuadShape(2.0f, 2.0f))

        Log.d(
            "MainRenderer",
            "width::$width height::$height"
        )
        Matrix.orthoM(projectionMatrix, 0, left, right, bottom, top, near, far)
        Matrix.setIdentityM(viewMatrix, 0)
        lookAt(viewMatrix, camera.eye, camera.center, camera.up)
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
    }

    override fun onDrawFrame(gl10: GL10) {
        /*long startFrame = System.currentTimeMillis();*/
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        if (shader.isDirty) {
            shader.recompileShader(context)
        }
        shader.draw(generalShapes, time, width, height, null)
        /*textShader.draw(textShapes, time, width, height, viewProjectionMatrix);*/time += 0.03.toFloat()
        val cutTime = 30.0f
        time %= cutTime

        /*textShape.updateBuffer(String.format(Locale.US, "frame: %d ms", (System.currentTimeMillis() - startFrame)) + "\ntime:" + String.format("%.1f", time) + "\ncutTime: " + cutTime);*/
    }

    val shaderChangeObserver: ShaderChangeObserver
        get() = shader

    private class Camera() {
        var eye: Vec3<Float> = Vec3(0.5f, 0.5f, 1f)
        var center: Vec3<Float> = Vec3(0.5f, 0.5f, 0f)
        var up: Vec3<Float> = Vec3(0.0f, 1.0f, 0f)

    }
}