package com.gplio.fibrewallpaper.lib.graphics

import android.opengl.GLES20
import java.lang.StringBuilder

data class ShaderResult(
    val hasErrors: Boolean,
    val error: String,
    val program: Int,
    val shaderType: ShaderType? = null
)

fun createGlShaderProgram(vertexShaderCode: String?, fragmentShaderCode: String?): ShaderResult {
    val vertexShaderResult = compileGlShader(
        ShaderType.Vertex,
        GLES20.GL_VERTEX_SHADER,
        vertexShaderCode
    )
    val fragmentShaderResult = compileGlShader(
        ShaderType.Fragment,
        GLES20.GL_FRAGMENT_SHADER,
        fragmentShaderCode
    )

    if (vertexShaderResult.hasErrors) return vertexShaderResult
    if (fragmentShaderResult.hasErrors) return fragmentShaderResult

    val program = GLES20.glCreateProgram()
    GLES20.glAttachShader(program, vertexShaderResult.program)
    GLES20.glAttachShader(program, fragmentShaderResult.program)
    GLES20.glLinkProgram(program)
    GLES20.glUseProgram(program)

    val glErrors = checkGlError()
    if (glErrors.isNotEmpty()) {
        return ShaderResult(true, glErrors, -1)
    }

    return ShaderResult(false, "", program)
}

private fun compileGlShader(shaderType: ShaderType, type: Int, code: String?): ShaderResult {
    val shader = GLES20.glCreateShader(type)
    GLES20.glShaderSource(shader, code)
    GLES20.glCompileShader(shader)

    // Get the compilation status.
    val compileStatus = IntArray(1)
    GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

    // If the compilation failed, delete the shader.
    if (compileStatus[0] == 0) {
        GLES20.glDeleteShader(shader)

        val error = GLES20.glGetShaderInfoLog(shader)
        return ShaderResult(true, error, -1)
    }
    return ShaderResult(false, "", shader)
}

/**
 * Checks if we've had an error inside of OpenGL ES, and if so what that error is.
 */
fun checkGlError(): String {
    val sb = StringBuilder()
    var errorCode: Int
    var error = false
    while (GLES20.glGetError().also { errorCode = it } != GLES20.GL_NO_ERROR) {
        sb.append("$errorCode").appendLine()
        error = error or true
    }
    if (!error) return ""
    return sb.toString()
}
