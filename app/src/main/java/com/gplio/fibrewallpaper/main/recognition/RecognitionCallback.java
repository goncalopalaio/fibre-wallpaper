package com.gplio.andlib.recognition;

import android.graphics.Bitmap;

/**
 * Created by goncalopalaio on 16/07/18.
 */

public interface RecognitionCallback {
    RecognitionResult infer(Bitmap bitmap);
}
