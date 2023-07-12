package com.gplio.andlib.graphics;

/**
 * Created by goncalopalaio on 26/05/18.
 */

public class FunkyCameraTextureShader extends GShader{
    private static String vertex =
            "attribute vec4 position;" +
                    "attribute vec2 uv;" +
                    "uniform float time;" +
                    "varying vec2 vuv;" +
                    "void main() {" +
                    "vuv = uv;" +
                    "gl_Position = vec4(position.x , position.y * cos(time)  + position.x * sin(time) , position.z * sin(position.y * cos(time) * time * uv.y * uv.x), position.w);" +
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
    public FunkyCameraTextureShader(int textureOES) {
        super(vertex, fragment, textureOES);
    }
}
