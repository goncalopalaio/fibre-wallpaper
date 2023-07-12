package com.gplio.andlib.graphics;

import android.content.Context;
import android.os.Environment;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

/**
 * Created by goncalopalaio on 23/07/18.
 */

public class TextShape extends GShape {
    private static final float QUAD_SIDE = 0.05f;
    public static final String FONT_MAP_PATH = "/debug/easy_font_raw.png";
    private static final float FONT_MAP_WIDTH = 453f;

    private int positionsLength;
    private int uvLength;
    private int textLength;
    private List<DebugText.Offsets> offsets;

    public TextShape(Context context) {
        coordsPerVertex = 2;
        vertexStride = coordsPerVertex * BYTES_PER_FLOAT;


        String pathToFontMap = Environment.getExternalStorageDirectory() + FONT_MAP_PATH;
        offsets = DebugText.parseEasyFont(pathToFontMap);
    }

    /**
     * Updates text buffer by re-generating and re-allocating all quads and uvs
     * note: this will slooow
     * @param text Text to display
     */
    public void updateBuffer(String text) {
        textLength = text.length();

        {
            // Build quads
            float[] quadPositions = getQuadPositions();
            float[] quads = buildVertices(text, quadPositions);

            positionsLength = quads.length;
            vertexBuffer = allocateBuffer(quads);
            vertexCount = positionsLength / coordsPerVertex;
        }

        {
            // Build uvs
            float[] uvs = buildUVs(text);
            uvLength = uvs.length;
            uvsBuffer = allocateBuffer(uvs);
        }
    }

    private static FloatBuffer allocateBuffer(float[] values) {
        FloatBuffer floatBuffer = ByteBuffer
                .allocateDirect(values.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer
                .put(values).position(0);
        return floatBuffer;
    }


    private float[] buildVertices(String text, float[] baseArray) {

        // todo all of this is not ideal.

        int partialLen = baseArray.length;
        final int totalLen = partialLen * text.length();

        float[] quads = new float[totalLen];

        float heightFactor = 0.5f;
        float widthFactor = 0.95f;
        float xFactor = 0.1f * widthFactor;
        float yFactor = 0.1f * heightFactor;

        float xOffset = 0f;
        float yOffset = 0f;
        char[] chars = text.toCharArray();
        int tix = 0;
        for (int j = 0; j < totalLen; j += partialLen) {
            char ch = chars[tix];
            DebugText.Offsets offset = getOffset(ch);
            float w = (offset.end - offset.start) * xFactor * widthFactor;

            if (ch == '\n') {
                w = 0f;
                xOffset = 0f;
                yOffset -= yFactor;
            }

            for (int i = 0; i < partialLen; i++) {

                if (i % 2 == 0) {
                    // x's
                    quads[j + i] = (baseArray[i] * w) + xOffset;
                } else {
                    quads[j + i] = (baseArray[i] * heightFactor) + yOffset;
                }
            }
            tix++;
            xOffset += QUAD_SIDE;
        }

        return quads;
    }


    private float[] buildUVs(String text) {

        // todo all of this is not ideal.

        float[] uvPositions = getUvPositions(0f, 1f);

        int partialLen = uvPositions.length;
        final int totalLen = partialLen * text.length();

        float[] quads = new float[totalLen];

        char[] chars = text.toCharArray();
        int tix = 0;
        for (int j = 0; j < totalLen; j += partialLen) {

            DebugText.Offsets offset = getOffset(chars[tix]);
            uvPositions = getUvPositions(offset.start / FONT_MAP_WIDTH, offset.end / FONT_MAP_WIDTH);

            System.arraycopy(uvPositions, 0, quads, j, partialLen);

            tix++;
        }

        return quads;
    }

    private DebugText.Offsets getOffset(char c) {

        for (DebugText.Offsets offset : offsets) {
            if (offset.correspondingChar == c) {
                return offset;
            }
        }

        return offsets.get(3); // default char
    }

    private float[] getQuadPositions() {
        return new float[]{
                QUAD_SIDE, -QUAD_SIDE,
                QUAD_SIDE, QUAD_SIDE,
                -QUAD_SIDE, QUAD_SIDE,
                -QUAD_SIDE, QUAD_SIDE,
                -QUAD_SIDE, -QUAD_SIDE,
                QUAD_SIDE, -QUAD_SIDE
        };
    }

    private float[] getUvPositions(float start, float end) {

        return new float[]{
                end, 0.0f,
                end, -0.90f,
                start, -0.90f,
                start, -0.90f,
                start, 0.0f,
                end, 0.0f
        };
    }

}
