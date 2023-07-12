package com.gplio.andlib.graphics;

import android.opengl.Matrix;

import static android.opengl.Matrix.setLookAtM;

/**
 * Created by goncalopalaio on 24/05/18.
 */

public class GMath {
    public static float[] matrix4() {
        return new float[16];
    }

    public static void lookAt(float[] matrix, V3<Float> eye, V3<Float> center, V3<Float> up) {
        Matrix.setLookAtM(
                matrix,
                0,
                eye.x, eye.y, eye.z,
                center.x, center.y, center.z,
                up.x, up.y, up.z);
    }

    public static class V3<T> {
        public T x;
        public T y;
        public T z;

        public V3(T x, T y, T z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        V3(T val) {
            this.x = val;
            this.y = val;
            this.z = val;
        }
    }

    public static class v4<T> {
        public T x;
        public T y;
        public T z;
        public T w;
    }
}
