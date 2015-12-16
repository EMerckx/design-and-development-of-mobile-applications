package com.emerckx.lab07;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sam on 15/10/15.
 */
public class ProgressMeterView extends View {
    private float progress = 0.5f;
    private static final int circle_width = 30;

    public ProgressMeterView(Context context) {
        super(context);
    }

    public ProgressMeterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setProgress(float progress){
        this.progress = progress;
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(ContextCompat.getColor(getContext(), R.color.detail));

        int dimension = getWidth() < getHeight() ? getWidth() : getHeight();
        int start_x = (getWidth() - dimension) / 2;
        int start_y = (getHeight() - dimension) / 2;

        canvas.drawArc(start_x, start_y, start_x+dimension, start_y+dimension, 0, 360, true, p);

        p.setColor(ContextCompat.getColor(getContext(), R.color.foreground));
        canvas.drawArc(start_x, start_y, start_x + dimension, start_y + dimension, -90, 360 * progress, true, p);

        p.setColor(ContextCompat.getColor(getContext(), R.color.background));
        canvas.drawArc(start_x + circle_width, start_y + circle_width, start_x + dimension - circle_width, start_y + dimension - circle_width, 0, 360, true, p);
    }
}
