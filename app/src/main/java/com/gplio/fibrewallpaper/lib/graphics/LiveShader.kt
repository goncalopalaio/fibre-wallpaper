package com.gplio.fibrewallpaper.lib.graphics

import com.gplio.fibrewallpaper.lib.logger.Tagged
import com.gplio.fibrewallpaper.lib.logger.d

/**
 * Created by goncalopalaio on 19/07/18.
 */
open class LiveShader(defaultVertexShaderCode: String, defaultFragmentShaderCode: String) :
    CustomShader(defaultVertexShaderCode, defaultFragmentShaderCode, -1), Tagged {

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

    fun requestShaderUpdate(vertexShader: String, fragmentShader: String) {
        currentVertexShaderCode = vertexShader
        currentFragmentShaderCode = fragmentShader

        isDirty = true
        d("requestShaderUpdate", "currentVertexShaderCode=$currentVertexShaderCode, currentFragmentShaderCode=$currentFragmentShaderCode")
    }
}
