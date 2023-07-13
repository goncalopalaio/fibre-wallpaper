package com.gplio.fibrewallpaper.lib.graphics

private const val vertexShader = "attribute vec4 position;" +
        "attribute vec2 uv;" +
        "void main() {" +
        "gl_Position = position;" +
        "}"
private const val fragmentShader = "precision mediump float;" +
        "void main() {" +
        "gl_FragColor = vec4(1.0,0.0,0.0,1.0);" +
        "}"

class DefaultShader : CustomShader(vertexShader, fragmentShader)
