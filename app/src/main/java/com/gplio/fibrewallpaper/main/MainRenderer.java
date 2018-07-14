package com.gplio.fibrewallpaper.main;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by goncalopalaio on 14/07/18.
 */

public class MainRenderer implements GLSurfaceView.Renderer {

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        GLES20.glClearColor(0.1f, 0.4f , 1.0f, 1.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }
}
