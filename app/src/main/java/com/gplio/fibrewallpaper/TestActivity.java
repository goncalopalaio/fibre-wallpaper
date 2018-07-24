package com.gplio.fibrewallpaper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gplio.andlib.files.ExternalFileObserver;
import com.gplio.andlib.graphics.DebugText;
import com.gplio.fibrewallpaper.main.MainRenderer;

public class TestActivity extends AppCompatActivity {

    private MainRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        GLSurfaceView surface = findViewById(R.id.surface_view);

        renderer = new MainRenderer(this);
        surface.setPreserveEGLContextOnPause(true);
        surface.setEGLContextClientVersion(2);
        surface.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        surface.setRenderer(renderer);
        surface.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            throw new RuntimeException("Please grant read/write storage permission");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        log("onResume");

        DebugText.parseEasyFont(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        log("onPause");
        renderer.unsubscribeExternalEvents();
    }

    private static void log(String msg) {
        Log.d("TestActivity", msg);
    }
}
