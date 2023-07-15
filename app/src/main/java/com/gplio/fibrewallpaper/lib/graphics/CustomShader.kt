package com.gplio.fibrewallpaper.lib.graphics

import android.opengl.GLES11Ext
import android.opengl.GLES20
import com.gplio.fibrewallpaper.lib.logger.Tagged
import com.gplio.fibrewallpaper.lib.logger.d
import com.gplio.fibrewallpaper.lib.logger.e

open class CustomShader(
    protected val defaultVertexShaderCode: String,
    protected val defaultFragmentShaderCode: String,
    private val textureOes: Int = -1
) :
    Tagged {

    override val tag: String
        get() = "CustomShader"

    protected var currentVertexShaderCode: String = defaultVertexShaderCode
    protected var currentFragmentShaderCode: String = defaultFragmentShaderCode
    private var program = 0
    private var positionLocation = 0
    private var uvLoc = 0
    private var uniformTextureLocation = 0
    private val textures = IntArray(1)
    private var uniformTimeLocation = 0
    private var uniformWidthLocation = 0
    private var uniformHeightLocation = 0
    private var isTexture0Enabled = false
    private var uniformViewProjectionLocation = 0

    fun init(
        vertexShaderCode: String = defaultVertexShaderCode,
        fragmentShaderCode: String = defaultFragmentShaderCode
    ): Boolean {
        d("init", "vertexShaderCode=$vertexShaderCode, fragmentShaderCode=$fragmentShaderCode")

        val compiledProgramResult = createGlShaderProgram(vertexShaderCode, fragmentShaderCode)
        if (compiledProgramResult.hasErrors) {
            e("init", "compiled program has errors | compiledProgramResult=$compiledProgramResult")
            return true
        }
        program = compiledProgramResult.program

        GLES20.glUseProgram(program)

        // Attribute locations
        positionLocation = GLES20.glGetAttribLocation(program, "position")
        if (positionLocation < 0) e("init", "attribute position not found")

        uvLoc = GLES20.glGetAttribLocation(program, "uv")
        if (uvLoc < 0) e("init", "attribute uv not found")

        // Uniform locations
        GLES20.glGetUniformLocation(program, "color")
        GLES20.glGetUniformLocation(program, "mvp")
        uniformTimeLocation = GLES20.glGetUniformLocation(program, "time")
        uniformTextureLocation = GLES20.glGetUniformLocation(program, "utexture0")
        uniformWidthLocation = GLES20.glGetUniformLocation(program, "width")
        uniformHeightLocation = GLES20.glGetUniformLocation(program, "height")
        uniformViewProjectionLocation = GLES20.glGetUniformLocation(program, "vp")

        val glErrors = checkGlError()
        d("init", "glErrors=$glErrors")

        // TODO clear GL errors, remove uv attribute.
        /*if (glErrors.isNotEmpty()) {
            e("init", "finished with errors | glErrors=$glErrors")
            return true
        }*/

        return false
    }

    fun draw(
        shapes: List<CustomShape>,
        time: Float,
        width: Int,
        height: Int,
        viewProjectionMatrix: FloatArray?
    ) {
        GLES20.glUseProgram(program)

        GLES20.glEnableVertexAttribArray(positionLocation)
        GLES20.glEnableVertexAttribArray(uvLoc)

        GLES20.glUniform1f(uniformTimeLocation, time)
        GLES20.glUniform1f(uniformWidthLocation, width.toFloat())
        GLES20.glUniform1f(uniformHeightLocation, height.toFloat())

        if (viewProjectionMatrix != null) {
            GLES20.glUniformMatrix4fv(
                uniformViewProjectionLocation,
                1,
                false,
                viewProjectionMatrix,
                0
            )
        }

        for (shape in shapes) {
            GLES20.glVertexAttribPointer(
                positionLocation, shape.coordsPerVertex, GLES20.GL_FLOAT, false,
                shape.vertexStride, shape.vertexBuffer
            )
            if (shape.uvsBuffer != null) {
                GLES20.glVertexAttribPointer(
                    uvLoc, shape.uvsPerVertex, GLES20.GL_FLOAT,
                    false, 2 * 4, shape.uvsBuffer
                )
            }

            if (isTexture0Enabled) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])
                GLES20.glUniform1i(uniformTextureLocation, 0)
            }

            if (textureOes != -1) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
                GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureOes)
                GLES20.glUniform1i(uniformTextureLocation, 1)
            }

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, shape.vertexCount)
            if (isTexture0Enabled) {
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
                GLES20.glUniform1i(uniformTextureLocation, 0)
            }
        }

        GLES20.glDisableVertexAttribArray(positionLocation)
        GLES20.glDisableVertexAttribArray(uvLoc)
        GLES20.glUseProgram(0)
    }
}