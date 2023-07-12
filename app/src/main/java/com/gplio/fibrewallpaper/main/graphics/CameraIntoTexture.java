package com.gplio.andlib.graphics;


import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Created by goncalopalaio on 24/05/18.
 */

public class CameraIntoTexture {
    private static final String TAG = "CameraIntoTexture";
    private SurfaceTexture surfaceTexture;
    private Camera camera;
    private int textureOES;

    public CameraIntoTexture() {}

    /**
     * Returns OES texture id
     */
    public int init() {
        int[] texture = new int[1];

        // note: device has to support this extension

        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);

        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 1);

        if (surfaceTexture != null) {
            Log.d(TAG, "init: called init 2 times in the same object");
            surfaceTexture.release();
        }

        surfaceTexture = new SurfaceTexture(texture[0]);

        boolean error = startCamera(surfaceTexture);
        if (error) {
            return -1;
        }
        textureOES = texture[0];
        return texture[0];
    }

    private boolean startCamera(SurfaceTexture surfaceTexture) {

        if (camera != null) {
            Log.d(TAG, "intoCamera: stopping camera");
            camera.stopPreview();
            camera.release();
        }

        // choosing camera
        Camera.CameraInfo info = new Camera.CameraInfo();
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                camera = Camera.open(i);
                break;
            }
        }

        if (camera == null) {
            Log.d(TAG, "intoCamera: could not get camera");
            return true;
        }

        try {
            camera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            Log.d(TAG, "intoCamera: could not set preview texture: " + e.getLocalizedMessage());
            return true;
        }

        Camera.Parameters params = camera.getParameters();
        // note: not safe?
        List<Camera.Size> supportedPreviewSizes = params.getSupportedPreviewSizes();

        for (Camera.Size supportedPreviewSize : supportedPreviewSizes) {
            Log.d(TAG, "intoCamera: supportedPreviewSize: " + supportedPreviewSize.width + " " + supportedPreviewSize.height);
        }

        // choosing the last preview size (for no specific reason)
        Camera.Size size = supportedPreviewSizes.get(supportedPreviewSizes.size() - 1);
        params.setPreviewSize(size.width, size.height);

        camera.setParameters(params);
        camera.startPreview();

        return false;

    }

    public void update() {
        if (surfaceTexture == null) {
            Log.d(TAG, "update: calling update on an empty surface texture");
            return;
        }

        surfaceTexture.updateTexImage();
    }

    public void destroy() {
        if (surfaceTexture != null) {
            surfaceTexture.release();
            surfaceTexture = null;
        }
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    public int getTextureOES() {
        return textureOES;
    }
}
