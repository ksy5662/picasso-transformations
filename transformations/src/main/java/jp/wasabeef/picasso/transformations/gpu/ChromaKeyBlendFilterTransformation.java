package jp.wasabeef.picasso.transformations.gpu;

/**
 * Copyright (C) 2015 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;

import com.squareup.picasso.Transformation;

import java.util.Arrays;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageChromaKeyBlendFilter;

/**
 * Performs a ChromaKeyBlend effect,
 * remove specific color by setting it as alpha = 0,
 * with a default of smooth = 0.1, thresholdSensitivity = 0.3, and colorToReplay = {0.0f, 1.0f, 0.0f}.
 */
public class ChromaKeyBlendFilterTransformation implements Transformation {

  private Context mContext;

  private GPUImageChromaKeyBlendFilter mFilter = new GPUImageChromaKeyBlendFilter();

  private int mThresholdSensitivityLocation;
  private int mSmoothingLocation;
  private int mColorToReplaceLocation;
  private float mSmoothing = 0.1f;
  private float mThresholdSensitivity = 0.3f;
  private float[] mColorToReplace = new float[]{0.0f, 1.0f, 0.0f};

  public ChromaKeyBlendFilterTransformation(Context context) {
    this(context, 0.1f, 0.3f, new float[] { 0.0f, 0.0f, 0.0f });
  }

  public ChromaKeyBlendFilterTransformation(Context context, float smoothing, float thresholdSensitivity, float[] color) {
    mContext = context;
    mSmoothing = smoothing;
    mThresholdSensitivity = thresholdSensitivity;
    mColorToReplace = color;
    mFilter.setSmoothing(mSmoothing);
    mFilter.setThresholdSensitivity(mThresholdSensitivity);
    mFilter.setColorToReplace(mColorToReplace[0], mColorToReplace[1], mColorToReplace[2]);
  }

  @Override public Bitmap transform(Bitmap source) {

    GPUImage gpuImage = new GPUImage(mContext);
    gpuImage.setImage(source);
    gpuImage.setFilter(mFilter);
    Bitmap bitmap = gpuImage.getBitmapWithFilterApplied();

    source.recycle();

    return bitmap;
  }

  @Override public String key() {
    return "ChromaKeyBlendFilterTransformation(smoothing=" + mSmoothing +
        ",color=" + Arrays.toString(mColorToReplace) +
        ",thresholdSensitivity=" + mThresholdSensitivity + ")";
  }
}
