package com.gplio.andlib.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * Created by goncalopalaio on 24/07/18.
 */

public class TextShader extends GShader {
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
            "precision mediump float;" +
                    "varying vec2 vuv;" +
                    "uniform sampler2D utexture0;" +
                    "void main() {" +
                    "vec4 c = texture2D(utexture0, vuv);" +
                    "if(c.g > 0.1) {" +
                    "c.rgb = vec3(0.1,0.1,0.1);" +
                    "} else {" +
                    "c.rgb = vec3(0.9,0.9,0.9);" +
                    "}" +
                    "gl_FragColor = c;" +
                    "}";

    public TextShader() {
        super(vertex, fragment);
    }

    @Override
    public boolean init(Context context) {
        Log.d("TextShader", "DebugFiles: " + TextUtils.join("|", new File(Environment.getExternalStorageDirectory() + "/debug/").list()));

        File dir = new File(Environment.getExternalStorageDirectory() + TextShape.FONT_MAP_PATH);
        Bitmap bitmap = BitmapFactory.decodeFile(dir.getAbsolutePath());
        return super.init(context, bitmap);
    }
}
