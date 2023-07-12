package com.gplio.andlib.observers

import com.gplio.andlib.values.ShaderType

interface ShaderChangeObserver {

    fun onShaderChanged(shaderType: ShaderType, content: String)
}