package com.gplio.andlib.graphics;

/**
 * Created by goncalopalaio on 26/05/18.
 */

public class CameraTextureShader extends GShader{
    private static String vertex =
            "attribute vec4 position;" +
                    "attribute vec2 uv;" +
                    "varying vec2 vuv;" +
                    "uniform mat4 vp;" +
                    "void main() {" +
                        "vuv = uv;" +
                        "gl_Position = vp * position;" +
                    "}";

    private static String fragment =
            "       #extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;" +
                    "varying vec2 vuv;" +
                    "uniform samplerExternalOES sTexture;" +
                    "void main() {" +
                    "gl_FragColor = texture2D(sTexture, vuv);" +
                    //"gl_FragColor = vec4(vuv.x,0.0,0.0,1.0);" +
                    "}";
    public CameraTextureShader(int textureOES) {
        super(vertex, fragment, textureOES);
    }
}
