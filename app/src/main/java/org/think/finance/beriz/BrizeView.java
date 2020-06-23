package org.think.finance.beriz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description：.
 * Author：Created by YJ_Song on 2019-11-04.
 * Email:  songyuanjin@innotechx.com
 */
public class BrizeView extends View {
    private Paint mRedPaint;
    private Path path;

    public BrizeView(Context context) {
        this(context, null);
    }

    public BrizeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrizeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRedPaint.setColor(Color.RED);
        mRedPaint.setStrokeWidth(4);
        mRedPaint.setStyle(Paint.Style.STROKE);
        path = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.moveTo(100, 300);
        path.cubicTo(100, 100, 500, 100, 600, 300);
        canvas.drawPath(path, mRedPaint);
    }
}
