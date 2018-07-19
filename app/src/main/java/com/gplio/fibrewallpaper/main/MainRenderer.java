package com.gplio.fibrewallpaper.main;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.util.Log;

import com.gplio.andlib.files.TextFiles;
import com.gplio.andlib.graphics.GShader;
import com.gplio.andlib.graphics.GShape;
import com.gplio.andlib.graphics.GenericShader;
import com.gplio.andlib.graphics.LiveShader;
import com.gplio.andlib.graphics.QuadShape;
import com.gplio.fibrewallpaper.BuildConfig;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by goncalopalaio on 14/07/18.
 */

public class MainRenderer implements GLSurfaceView.Renderer {

    private int height;
    private int width;

    private static class CustomShader extends LiveShader {
        public CustomShader(String defaultVertexShaderCode, String defaultFragmentShaderCode) {
            super(defaultVertexShaderCode, defaultFragmentShaderCode, Environment.getExternalStorageDirectory() + "/fibre/", "current.vert", "current.frag");
        }
    }

    private final Context context;
    private LiveShader genericShader;
    private float tick;
    private ArrayList<GShape> shapes;

    public MainRenderer(Context context) {
        this.context = context;

        // just leave it like this for now
        String previousVertexShader = TextFiles.readStringFromAssets(context, "shaders/current.vert", "");
        String previousFragmentShader = TextFiles.readStringFromAssets(context, "shaders/current.frag", "");
        genericShader = new CustomShader(previousVertexShader, previousFragmentShader);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        genericShader.init(context);
        tick = 0;
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        GLES20.glClearColor(0.1f, 0.4f, 1.0f, 1.0f);
        width = w;
        height = h;
        shapes = new ArrayList<>();
        shapes.add(new QuadShape(2.0f));

        Log.d("MainRenderer", "width::" + width + " height::" + height);

        genericShader.unsubscribe();
        genericShader.subscribe(context);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        if (genericShader.isDirty()) {
            genericShader.recompileShader(context);
        }

        genericShader.draw(shapes, tick, width, height);
        tick += 0.01;
    }

    public void unsubscribeExternalEvents() {
        genericShader.unsubscribe();
    }
}
