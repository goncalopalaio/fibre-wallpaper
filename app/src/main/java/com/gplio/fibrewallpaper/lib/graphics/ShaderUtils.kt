package com.gplio.fibrewallpaper.lib.graphics

import android.opengl.GLES20
import android.util.Log

private const val TAG = "ShaderUtils"

fun createGlShaderProgram(vertexShaderCode: String?, fragmentShaderCode: String?): Int {
    val vertexShader = loadGLShader(
        "$TAG:VertexShader",
        GLES20.GL_VERTEX_SHADER,
        vertexShaderCode
    )
    val fragmentShader = loadGLShader(
        "$TAG:FragmentShader",
        GLES20.GL_FRAGMENT_SHADER,
        fragmentShaderCode
    )
    if (vertexShader == -1 || fragmentShader == -1) {
        return -1
    }
    val program = GLES20.glCreateProgram()
    GLES20.glAttachShader(program, vertexShader)
    GLES20.glAttachShader(program, fragmentShader)
    GLES20.glLinkProgram(program)
    GLES20.glUseProgram(program)
    checkGlError(TAG, "Program creation")

    //mQuadPositionParam = GLES20.glGetAttribLocation(program, "a_Position");
    //mQuadTexCoordParam = GLES20.glGetAttribLocation(program, "a_TexCoord");
    //mTransformationMatrixParam = GLES20.glGetUniformLocation(program, "u_Transformation");
    //checkGLError(TAG, "Program parameters");
    return program
}

fun loadGLShader(tag: String?, type: Int, code: String?): Int {
    var shader = GLES20.glCreateShader(type)
    GLES20.glShaderSource(shader, code)
    GLES20.glCompileShader(shader)

    // Get the compilation status.
    val compileStatus = IntArray(1)
    GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

    // If the compilation failed, delete the shader.
    if (compileStatus[0] == 0) {
        Log.e(tag, "error compiling shader: " + GLES20.glGetShaderInfoLog(shader))
        GLES20.glDeleteShader(shader)
        shader = -1
    }
    return shader
}

/**
 * Checks if we've had an error inside of OpenGL ES, and if so what that error is.
 *
 * @param label Label to report in case of error.
 * @throws RuntimeException If an OpenGL error is detected.
 */
fun checkGlError(tag: String?, label: String): Boolean {
    var errorCode: Int
    var error = false
    while (GLES20.glGetError().also { errorCode = it } != GLES20.GL_NO_ERROR) {
        Log.e(tag, "$label: glError $errorCode")
        error = error or true
    }
    if (!error) {
        Log.d(tag, "$label: NO-glError ")
    }
    return error
}
