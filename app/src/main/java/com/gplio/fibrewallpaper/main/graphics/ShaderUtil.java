package com.gplio.andlib.graphics;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Shader helper functions.
 */
public class ShaderUtil {
    private static final String TAG = "ShaderUtil";

    public static int createGLShaderProgram(String vertexShaderCode, String fragmentShaderCode) {
        int vertexShader = loadGLShader(TAG + ":VertexShader", GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadGLShader(TAG + ":FragmentShader", GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        if (vertexShader == -1 || fragmentShader == -1) {
            return -1;
        }

        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
        GLES20.glUseProgram(program);

        ShaderUtil.checkGLError(TAG, "Program creation");

        //mQuadPositionParam = GLES20.glGetAttribLocation(program, "a_Position");
        //mQuadTexCoordParam = GLES20.glGetAttribLocation(program, "a_TexCoord");
        //mTransformationMatrixParam = GLES20.glGetUniformLocation(program, "u_Transformation");
        //ShaderUtil.checkGLError(TAG, "Program parameters");

        return program;
    }

    public static int loadGLShader(String tag, int type, String code) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            Log.e(tag, "error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = -1;
        }

        return shader;
    }
    /**
     * Converts a raw text file, saved as a resource, into an OpenGL ES shader.
     *
     * @param type The type of shader we will be creating.
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return The shader object handler.
     */
    public static int loadGLShader(String tag, Context context, int type, int resId) {
        String code = readRawTextFile(context, resId);
        return loadGLShader(tag, type, code);
    }

    /**
     * Checks if we've had an error inside of OpenGL ES, and if so what that error is.
     *
     * @param label Label to report in case of error.
     * @throws RuntimeException If an OpenGL error is detected.
     */
    public static boolean checkGLError(String tag, String label) {
        int errorCode;
        boolean error = false;
        while ((errorCode = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(tag, label + ": glError " + errorCode);
            error |= true;
        }
        if (!error) {
            Log.d(tag, label + ": NO-glError ");
        }
        return error;
    }

    /**
     * Converts a raw text file into a string.
     *
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return The context of the text file, or null in case of error.
     */
    private static String readRawTextFile(Context context, int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            Log.e(TAG, "readRawTextFile: ", e);
        }
        return null;
    }
}
