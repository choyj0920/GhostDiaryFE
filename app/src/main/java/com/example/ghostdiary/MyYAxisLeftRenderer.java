package com.example.ghostdiary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class MyYAxisLeftRenderer extends YAxisRenderer {

    ArrayList<Bitmap> myiconarr;

    public MyYAxisLeftRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans, ArrayList<Bitmap> iconarr) {
        super(viewPortHandler, yAxis, trans);
        this.myiconarr=iconarr;
    }



    @Override
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                ? mYAxis.mEntryCount
                : (mYAxis.mEntryCount - 1);

        // draw
        for (int i = from; i < to; i++) {

            String text = mYAxis.getFormattedLabel(i);

            // change y label color here before drawing (i is an index of label)
            Bitmap bitmap= myiconarr.get(i);

//            c.drawText(text, fixedPosition, positions[i * 2 + 1] + offset, mAxisLabelPaint);


            if (bitmap != null) {
                Log.d("TAG","비트맵 그림그리기");
                Bitmap scaledBitmap = getScaledBitmap(bitmap);
                c.drawBitmap(scaledBitmap, fixedPosition-scaledBitmap.getWidth()/2f, positions[i * 2 + 1] +offset -scaledBitmap.getHeight()/2f, null);
            }

        }


    }

    private Bitmap getScaledBitmap(Bitmap bitmap) {
        int width = 32;
        int height = 40;
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
}