package com.gplio.fibrewallpaper.lib.graphics

import com.gplio.fibrewallpaper.lib.logger.Tagged
import com.gplio.fibrewallpaper.lib.logger.d
import com.gplio.fibrewallpaper.lib.observers.ShaderChangeObserver

/**
 * Created by goncalopalaio on 19/07/18.
 */
open class LiveShader(defaultVertexShaderCode: String?, defaultFragmentShaderCode: String?) :
    CustomShader(
        defaultVertexShaderCode!!,
        defaultFragmentShaderCode!!, -1
    ),
    ShaderChangeObserver, Tagged {

    override val tag: String
        get() = "LiveShader"

    @Volatile
    var isDirty = false
        private set

    fun recompileShader() {
        var error = init(currentVertexShaderCode, currentFragmentShaderCode)
        if (error) {
            d("recompileShader", "falling back to default shader")
            currentVertexShaderCode = defaultVertexShaderCode
            currentFragmentShaderCode = defaultFragmentShaderCode

            error = init(defaultVertexShaderCode, defaultFragmentShaderCode)
            if (error) d("recompileShader", "Could not fallback to default shader")
        }
        isDirty = false
    }

    override fun onShaderChanged(shaderType: ShaderType, content: String) {
        if (content.isEmpty()) {
            d("recompileShader", "updated vertex or fragment shader was empty $shaderType $content")
            return
        }
        when (shaderType) {
            ShaderType.Vertex -> currentVertexShaderCode = content
            ShaderType.Fragment -> currentFragmentShaderCode = content
        }
        isDirty = true
        d("recompileShader", "marked as dirty$shaderType $content")
    }
}
