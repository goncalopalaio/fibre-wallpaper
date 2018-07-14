package com.gplio.fibrewallpaper;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gplio.fibrewallpaper.main.MainRenderer;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        GLSurfaceView surface = findViewById(R.id.surface_view);

        surface.setPreserveEGLContextOnPause(true);
        surface.setEGLContextClientVersion(2);
        surface.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        surface.setRenderer(new MainRenderer());
        surface.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }
}
