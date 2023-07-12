package com.gplio.andlib.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Created by goncalopalaio on 24/05/18.
 */

public class GShader {

    protected String vertexShaderCode;
    protected String fragmentShaderCode;
    private int textureOES = -1;
    private int program;
    private int positionLoc;
    private int uvLoc;
    private int utextureLoc;
    private int[] textures = new int[1];
    private int uTimeLoc;
    private int uWidthLoc;
    private int uHeightLoc;
    private boolean texture0Enabled = false;
    private int uViewProjectionLoc;

    public GShader (String vertexShaderCode, String fragmentShaderCode) {
        this.vertexShaderCode = vertexShaderCode;
        this.fragmentShaderCode = fragmentShaderCode;
    }

    public GShader (String vertexShaderCode, String fragmentShaderCode, int textureOES) {
        this.vertexShaderCode = vertexShaderCode;
        this.fragmentShaderCode = fragmentShaderCode;
        this.textureOES = textureOES;
    }

    public boolean init(Context context) {
        return init(context, null);
    }

    public boolean init(Context context, Bitmap texture0) {
        // @note passing the texture here is dumb.

        boolean error = compile();
        if (error) {
            return true;
        }

        GLES20.glUseProgram(program);

        // attrib locations
        positionLoc = GLES20.glGetAttribLocation(program, "position");

        if (positionLoc < 0) {
            loge( "init: attribute positionLoc not found");
        }

        uvLoc = GLES20.glGetAttribLocation(program, "uv");

        if (uvLoc < 0) {
            loge( "init: attribute uvLoc not found");
        }


        // uniform locations
        GLES20.glGetUniformLocation(program, "color");
        GLES20.glGetUniformLocation(program, "mvp");
        uTimeLoc = GLES20.glGetUniformLocation(program, "time");
        utextureLoc = GLES20.glGetUniformLocation(program, "utexture0");

        uWidthLoc = GLES20.glGetUniformLocation(program, "width");
        uHeightLoc = GLES20.glGetUniformLocation(program, "height");
        uViewProjectionLoc = GLES20.glGetUniformLocation(program, "vp");

        ShaderUtil.checkGLError("init", "end of init");


        /**********************/
        // Extract this later

        // read texture.
        /*assetTextureName = "texture0.png";
        Bitmap texture0 = null;
        try {
            texture0 = BitmapFactory.decodeStream(
                    context.getAssets().open(assetTextureName));
        } catch (IOException e) {
            loge( "init: while decoding " + assetTextureName + " " + e);
        }

        */

        if (texture0 == null) {
            loge( "init: texture open failed. Ignoring texture.");
            return false;
        }

        texture0Enabled = true;


        // upload texture
        GLES20.glUseProgram(program);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(textures.length, textures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, texture0, 0);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glUseProgram(0);
        ShaderUtil.checkGLError("init", "texture upload");
        /**********************/


        return false;
    }

    public void draw(List<GShape> shapes, float time, int width, int height, float[] viewProjectionMatrix) {
        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(positionLoc);
        GLES20.glEnableVertexAttribArray(uvLoc);

        GLES20.glUniform1f(uTimeLoc, time);
        GLES20.glUniform1f(uWidthLoc, width);
        GLES20.glUniform1f(uHeightLoc, height);
        if (viewProjectionMatrix != null) {
            GLES20.glUniformMatrix4fv(uViewProjectionLoc, 1, false, viewProjectionMatrix, 0);
        }

        for (GShape shape : shapes) {
            GLES20.glVertexAttribPointer(
                    positionLoc, shape.coordsPerVertex, GLES20.GL_FLOAT, false,
                    shape.vertexStride, shape.vertexBuffer);

            if (shape.uvsBuffer != null) {
                GLES20.glVertexAttribPointer(
                        uvLoc, shape.uvsPerVertex, GLES20.GL_FLOAT,
                        false, 2 * 4, shape.uvsBuffer);
            }


            if (texture0Enabled) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
                GLES20.glUniform1i(utextureLoc, 0);
            }

            if (textureOES != -1) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
                GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureOES);
                GLES20.glUniform1i(utextureLoc, 1);
            }

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, shape.vertexCount);


            if (texture0Enabled) {
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
                GLES20.glUniform1i(utextureLoc, 0);
            }
        }
        GLES20.glDisableVertexAttribArray(positionLoc);
        GLES20.glDisableVertexAttribArray(uvLoc);
        GLES20.glUseProgram(0);


    }

    private boolean compile() {
        program = ShaderUtil.createGLShaderProgram(vertexShaderCode, fragmentShaderCode);
        return program == -1;
    }

    private void loge(String msg) {
        Log.e(getClass().getSimpleName(), msg);
    }
}