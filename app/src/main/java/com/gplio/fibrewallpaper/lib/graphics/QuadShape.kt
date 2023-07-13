package com.gplio.fibrewallpaper.lib.graphics

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

data class QuadShape(
    private val scaleX: Float = 1.0f,
    private val scaleY: Float = 1.0f,
    private val offsetX: Float = 0.0f,
    private val offsetY: Float = 0.0f
) : CustomShape() {

    init {
        vertexBuffer = getPositionBuffer(scaleX, scaleY, offsetX, offsetY)
        uvsBuffer = getUvsBuffer()
        vertexStride = coordsPerVertex * BYTES_PER_FLOAT
        vertexCount = sPositionsLength / coordsPerVertex
    }

    companion object {
        private var sPositionsBuffer: FloatBuffer? = null
        private var sUvsBuffer: FloatBuffer? = null
        private var sPositionsLength = 0
        private var sUvsLength = 0
        private fun getPositionBuffer(
            scaleX: Float,
            scaleY: Float,
            offsetX: Float,
            offsetY: Float
        ): FloatBuffer? {
            if (sPositionsBuffer == null) {
                sPositionsBuffer = allocatePositionBuffer(scaleX, scaleY, offsetX, offsetY)
            }
            sPositionsBuffer!!.position(0)
            return sPositionsBuffer
        }

        private fun getUvsBuffer(): FloatBuffer? {
            if (sUvsBuffer == null) {
                sUvsBuffer = allocateUvsBuffer()
            }
            sUvsBuffer!!.position(0)
            return sUvsBuffer
        }

        private fun allocatePositionBuffer(
            scaleX: Float,
            scaleY: Float,
            offsetX: Float,
            offsetY: Float
        ): FloatBuffer {
            val positions = floatArrayOf(
                1.0f * scaleX + offsetX, -1.0f * scaleY + offsetY, 0f,
                1.0f * scaleX + offsetX, 1.0f * scaleY + offsetY, 0f,
                -1.0f * scaleX + offsetX, 1.0f * scaleY + offsetY, 0f,
                -1.0f * scaleX + offsetX, 1.0f * scaleY + offsetY, 0f,
                -1.0f * scaleX + offsetX, -1.0f * scaleY + offsetY, 0f,
                1.0f * scaleX + offsetX, -1.0f * scaleY + offsetY, 0f
            )
            sPositionsLength = positions.size
            return allocateBuffer(positions)
        }

        private fun allocateUvsBuffer(): FloatBuffer {
            val uvs = floatArrayOf(
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f
            )
            sUvsLength = uvs.size
            return allocateBuffer(uvs)
        }

        private fun allocateBuffer(values: FloatArray): FloatBuffer {
            val floatBuffer = ByteBuffer
                .allocateDirect(values.size * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
            floatBuffer
                .put(values).position(0)
            return floatBuffer
        }
    }
}
