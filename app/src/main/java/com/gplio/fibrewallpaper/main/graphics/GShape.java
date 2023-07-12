package com.gplio.andlib.graphics;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by goncalopalaio on 24/05/18.
 */

public class GShape {
    public static int BYTES_PER_FLOAT = 4;
    public int coordsPerVertex = 3;
    public int uvsPerVertex = 2;

    public FloatBuffer vertexBuffer;
    public int vertexStride;
    public FloatBuffer uvsBuffer;
    public int vertexCount;
}
