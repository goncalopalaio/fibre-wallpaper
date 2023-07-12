package com.gplio.andlib.graphics;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.gplio.andlib.observers.ShaderChangeObserver;
import com.gplio.andlib.values.ShaderType;

/**
 * Created by goncalopalaio on 19/07/18.
 */

public class LiveShader extends GShader implements ShaderChangeObserver {
    private final String defaultVertexShaderCode;
    private final String defaultFragmentShaderCode;

    private volatile String currentVertexShaderCode = "";
    private volatile String currentFragmentShaderCode = "";
    private volatile boolean dirty = false;

    public LiveShader(String defaultVertexShaderCode, String defaultFragmentShaderCode) {
        super(defaultVertexShaderCode, defaultFragmentShaderCode);

        this.defaultVertexShaderCode = defaultVertexShaderCode;
        this.defaultFragmentShaderCode = defaultFragmentShaderCode;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void recompileShader(Context context) {
        vertexShaderCode = currentVertexShaderCode;
        fragmentShaderCode = currentFragmentShaderCode;
        boolean error = init(context);

        if (error) {
            log("Falling back to default shader");
            vertexShaderCode = defaultVertexShaderCode;
            fragmentShaderCode = defaultFragmentShaderCode;
            error = init(context);

            if (error) {
                log("Could not fallback to default shader");
            }
        }

        dirty = false;
    }

    private static void log(String msg) {
        Log.d("LiveShader", msg);
    }

    @Override
    public void onShaderChanged(@NonNull ShaderType shaderType, @NonNull String content) {
        if (content.isEmpty()) {
            log("Updated vertex or fragment shader was empty " + shaderType + " " + content);
            return;
        }

        switch (shaderType) {
            case Vertex -> currentVertexShaderCode = content;
            case Fragment -> currentFragmentShaderCode = content;
        }

        dirty = true;

        log("Marked as dirty" + shaderType + " " + content);
    }
}
