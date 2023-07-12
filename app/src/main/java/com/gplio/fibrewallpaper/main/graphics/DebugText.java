package com.gplio.andlib.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by goncalopalaio on 23/07/18.
 */

public class DebugText {

    /**
     * Parses easy_font_raw.png from stb_easy_font
     * ascii 32 -> 126
     *       space -> ~
     */
    public static List<Offsets> parseEasyFont(String pathToFontMap) {
        Bitmap bitmap = BitmapFactory.decodeFile(pathToFontMap);

        if (bitmap == null) {
            log("Could not decode file " + pathToFontMap);
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        log("width: " + width + " height: " + height);

        //String s = "o";
        int previousStart = 0;
        List<Offsets> offsets = new LinkedList<>();
        char c = 32;
        for (int i = 1; i < width; i++) {
            int pixel = bitmap.getPixel(i,0);

            int red = Color.red(pixel);
            if (red == 0) {
                offsets.add(new Offsets(previousStart, i-1, c));
                previousStart = i;
                c++;
            }
        }

        return offsets;
    }

    private static void log(String m) {
        Log.d("DebugText", m);
    }

    static class Offsets {
        int start = 0;
        int end = 0;
        char correspondingChar;

        Offsets(int start, int end, char correspondingChar) {
            this.start = start;
            this.end = end;
            this.correspondingChar = correspondingChar;
        }
    }
}
