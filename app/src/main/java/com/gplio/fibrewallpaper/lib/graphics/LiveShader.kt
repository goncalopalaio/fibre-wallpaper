package com.gplio.fibrewallpaper.lib.graphics

import android.content.Context
import android.util.Log
import com.gplio.fibrewallpaper.lib.observers.ShaderChangeObserver

/**
 * Created by goncalopalaio on 19/07/18.
 */
open class LiveShader(defaultVertexShaderCode: String?, defaultFragmentShaderCode: String?) :
    CustomShader(
        defaultVertexShaderCode!!,
        defaultFragmentShaderCode!!, -1
    ),
    ShaderChangeObserver {
    @Volatile
    var isDirty = false
        private set

    fun recompileShader(context: Context?) {
        var error = init(currentVertexShaderCode, currentFragmentShaderCode)
        if (error) {
            log("Falling back to default shader")
            currentVertexShaderCode = defaultVertexShaderCode
            currentFragmentShaderCode = defaultFragmentShaderCode
            error = init(defaultVertexShaderCode, defaultFragmentShaderCode)
            if (error) {
                log("Could not fallback to default shader")
            }
        }
        isDirty = false
    }

    override fun onShaderChanged(shaderType: ShaderType, content: String) {
        if (content.isEmpty()) {
            log("Updated vertex or fragment shader was empty $shaderType $content")
            return
        }
        when (shaderType) {
            ShaderType.Vertex -> currentVertexShaderCode = content
            ShaderType.Fragment -> currentFragmentShaderCode = content
        }
        isDirty = true
        log("Marked as dirty$shaderType $content")
    }

    companion object {
        private fun log(msg: String) {
            Log.d("LiveShader", msg)
        }
    }
}
