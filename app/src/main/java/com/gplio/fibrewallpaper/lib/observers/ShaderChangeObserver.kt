package com.gplio.fibrewallpaper.lib.observers

interface ShaderChangeObserver {

    fun onShadersChanged(vertexShader: String, fragmentShader: String)
}