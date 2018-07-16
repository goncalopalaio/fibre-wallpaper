package com.gplio.fibrewallpaper.main;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.gplio.andlib.graphics.GShader;
import com.gplio.andlib.graphics.GShape;
import com.gplio.andlib.graphics.GenericShader;
import com.gplio.andlib.graphics.QuadShape;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by goncalopalaio on 14/07/18.
 */

public class MainRenderer implements GLSurfaceView.Renderer {

    private int height;
    private int width;

    private static class CustomShader extends GShader{
        private static String distLine = "float distLine(vec2 p, vec2 a, vec2 b) {" +
                "vec2 pa = p-a;" +
                "vec2 ba = b-a;" +
                "float t = clamp(dot(pa,ba)/dot(ba,ba), 0.0, 1.0);" +
                "return length(pa-ba*t);"+
                "}";
        private static String N21 = "" +
                "float N21(vec2 p) {" +
                "p = fract(p*vec2(233.994563, 851.73));" +
                "p += dot(p, p+4.9145);" +
                "return fract(p.x * p.y);" +
                "}";
        private static String N22 = "" +
                "vec2 N22(vec2 p ) {" +
                "float n = N21(p);" +
                "return vec2(n, N21(p+n));" +
                "}";
        private static String vertex =
                "attribute vec4 position;" +
                        "attribute vec2 uv;" +
                        "void main() {" +
                        "gl_Position = position;" +
                        "}";

        private static String fragment =
                "precision mediump float;" +
                        distLine +
                        N21 +
                        N22 +
                        "uniform float width;" +
                        "uniform float height;" +
                        "uniform float time;" +
                        "void main() {" +
                        "vec2 uv = (gl_FragCoord.xy - 0.5 * vec2(width, height)) / height;"+
                        "float d = distLine(uv, vec2(0.0), vec2(cos(time), sin(time)));" +
                        "float r = N22(uv).y;" +
                        "float m = smoothstep(0.05 , 0.035, d);"+
                        "vec3 col = vec3(m) * vec3(0.7,0.1,0.1);"+
                        "gl_FragColor = vec4(col,1.0);" +
                        "}";

        public CustomShader() {
            super(vertex, fragment);
        }
    }

    private final Context context;
    private GShader genericShader;
    private float tick;
    private ArrayList<GShape> shapes;

    public MainRenderer(Context context) {

        this.context = context;
    }
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        genericShader = new CustomShader();
        genericShader.init(context);
        tick = 0;
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        GLES20.glClearColor(0.1f, 0.4f , 1.0f, 1.0f);
        width = w;
        height = h;
        shapes = new ArrayList<>();
        shapes.add(new QuadShape(2.0f));

        Log.d("MainRenderer", "width::" + width + " height::" + height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        genericShader.draw(shapes, tick, width, height);
        tick += 0.01;
    }
}
