package com.gplio.fibrewallpaper.lib.graphics

import java.nio.FloatBuffer

open class CustomShape {
    var coordsPerVertex = 3
    var uvsPerVertex = 2
    var vertexBuffer: FloatBuffer? = null
    var vertexStride = 0
    var uvsBuffer: FloatBuffer? = null
    var vertexCount = 0

    companion object {
        var BYTES_PER_FLOAT = 4
    }
}
