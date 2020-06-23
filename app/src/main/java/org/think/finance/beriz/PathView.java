package org.think.finance.beriz;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Description：.
 * Author：Created by YJ_Song on 2019-11-09.
 * Email:  songyuanjin@innotechx.com
 */
public class PathView extends View {

    private Paint mPaint;
    private int mWidth;
    private Path mPath;
    private int waveLength = 100;
    private int control = 100;
    private int moveDiatance;

    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mPath = new Path();

        postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnim();
            }
        }, 1000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();

        mPath.reset();
        control = waveLength / 2;
        mPath.moveTo(-waveLength + moveDiatance, 1000 - moveDiatance);
        for (int i = -waveLength; i <= waveLength + mWidth; i += waveLength) {
            mPath.rQuadTo(control / 2, -20, control, 0);
            mPath.rQuadTo(control / 2, 20, control, 0);
        }

        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight());
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }


    public void startAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, waveLength);
        valueAnimator.setDuration(2500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                moveDiatance = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    public void clear() {
        mPath.reset();
        invalidate();
    }
}
