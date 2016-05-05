package br.com.luizfp.segredosufsc.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by luiz on 2/27/16.
 */
public class ViewHelper {
    private static final String TAG = ViewHelper.class.getSimpleName();

    /**
     * Returns the bitmap that represents the chart.
     *
     * @return
     */
    public static Bitmap getBitmap(View view) {
        L.d(TAG, "view width: " + view.getWidth());
        L.d(TAG, "view height: " + view.getHeight());
        // Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        // Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        // Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            // has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            // does not have background drawable, then draw white background on
            // the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        // return the bitmap
        return returnedBitmap;
    }

    public static boolean saveToGallery(View view, String fileName, int quality) {
        return ImageUtils.saveBitmapToGallery(view.getContext(), getBitmap(view),
                fileName, quality);
    }
}