package com.gplio.fibrewallpaper.lib.observers

import com.gplio.fibrewallpaper.lib.graphics.ShaderType

interface ShaderChangeObserver {

    fun onShaderChanged(shaderType: ShaderType, content: String)
}