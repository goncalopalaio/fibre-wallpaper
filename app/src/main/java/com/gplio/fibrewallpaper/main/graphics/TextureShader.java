package com.gplio.andlib.graphics;

/**
 * Created by goncalopalaio on 26/05/18.
 */

public class TextureShader extends GShader{
    private static String vertex =
            "attribute vec4 position;" +
                    "attribute vec2 uv;" +
                    "varying vec2 vuv;" +
                    "void main() {" +
                    "vuv = uv;" +
                    "gl_Position = position;" +
                    "}";

    private static String fragment =
            "precision mediump float;" +
                    "varying vec2 vuv;" +
                    "uniform sampler2D utexture0;" +
                    "void main() {" +
                    "gl_FragColor = texture2D(utexture0, vuv);" +
                    //"gl_FragColor = vec4(vuv.x,0.0,0.0,1.0);" +
                    "}";
    public TextureShader() {
        super(vertex, fragment);
    }
}
