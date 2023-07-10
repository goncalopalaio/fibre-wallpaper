package com.gplio.fibrewallpaper.main;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.util.Log;

import com.gplio.andlib.files.TextFiles;
import com.gplio.andlib.graphics.CameraRenderer;
import com.gplio.andlib.graphics.GMath;
import com.gplio.andlib.graphics.GShape;
import com.gplio.andlib.graphics.LiveShader;
import com.gplio.andlib.graphics.QuadShape;
import com.gplio.andlib.graphics.TextShader;
import com.gplio.andlib.graphics.TextShape;

import java.util.ArrayList;
import java.util.Locale;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.setIdentityM;

/**
 * Created by goncalopalaio on 14/07/18.
 */

public class MainRenderer implements GLSurfaceView.Renderer {

    private final Context context;
    /*TextShader textShader;*/ // TODO GONCALO either embed "/debug/easy_font_raw.png" or remove this
    private int height;
    private int width;
    private LiveShader genericShader;
    private float time;
    private ArrayList<GShape> generalShapes;
    private ArrayList<GShape> textShapes;

    private float[] viewMatrix = GMath.matrix4();
    private float[] projectionMatrix = GMath.matrix4();
    private float[] viewProjectionMatrix = GMath.matrix4();

    private float aspectRatio = 1.0f;
    private float left = -1f;
    private float right = 1f;
    private float bottom = -1f;
    private float top = 1f;
    private float near = -1f;
    private float far = 100f;

    /*private TextShape textShape;*/ // TODO GONCALO either embed "/debug/easy_font_raw.png" or remove this
    private Camera camera = new Camera();


    public MainRenderer(Context context) {
        this.context = context;

        // just leave it like this for now
        String previousVertexShader = TextFiles.readStringFromAssets(context, "shaders/current.vert", "");
        String previousFragmentShader = TextFiles.readStringFromAssets(context, "shaders/current.frag", "");
        genericShader = new CustomShader(previousVertexShader, previousFragmentShader);

        /*textShader = new TextShader();*/
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        genericShader.init(context);
        /*textShader.init(context);*/

        time = 0;
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        GLES20.glClearColor(0.1f, 0.4f, 1.0f, 1.0f);
        width = w;
        height = h;
        generalShapes = new ArrayList<>();
        generalShapes.add(new QuadShape(2.0f, 2.0f));

        textShapes = new ArrayList<>();
        /*textShape = new TextShape(context);
        textShape.updateBuffer(" ");
        textShapes.add(textShape);*/

        Log.d("MainRenderer", "width::" + width + " height::" + height);

        genericShader.unsubscribe();
        genericShader.subscribe(context);

        orthoM(projectionMatrix, 0, left, right, bottom, top, near, far);
        setIdentityM(viewMatrix, 0);
        GMath.lookAt(viewMatrix, camera.eye, camera.center, camera.up);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        long startFrame = System.currentTimeMillis();

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        if (genericShader.isDirty()) {
            genericShader.recompileShader(context);
        }

        genericShader.draw(generalShapes, time, width, height, null);
        /*textShader.draw(textShapes, time, width, height, viewProjectionMatrix);*/

        time += 0.03;

        float cutTime = 30.0f;
        time = time % cutTime;

        /*textShape.updateBuffer(String.format(Locale.US,"frame: %d ms", (System.currentTimeMillis() - startFrame))+"\ntime:"+String.format("%.1f", time) + "\ncutTime: " + cutTime);*/
    }

    public void unsubscribeExternalEvents() {
        genericShader.unsubscribe();
    }

    private static class CustomShader extends LiveShader {
        public CustomShader(String defaultVertexShaderCode, String defaultFragmentShaderCode) {
            super(defaultVertexShaderCode,
                    defaultFragmentShaderCode,
                    Environment.getExternalStorageDirectory() + "/fibre/",
                    "current.vert",
                    "current.frag");
        }
    }

    private class Camera {
        public GMath.V3<Float> eye;
        public GMath.V3<Float> center;
        public GMath.V3<Float> up;

        Camera() {
            eye = new GMath.V3<>(0.5f, 0.5f, 1f);
            center = new GMath.V3<>(0.5f, 0.5f, 0f);
            up = new GMath.V3<>(0.0f, 1.0f, 0f);
        }
    }
}
