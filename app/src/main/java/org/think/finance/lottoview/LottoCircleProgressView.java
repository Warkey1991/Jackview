package org.think.finance.lottoview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import org.think.finance.raffleview.R;

/**
 * Description：.
 * Author：Created by YJ_Song on 2019-12-03.
 * Email:  songyuanjin@innotechx.com
 */
public class LottoCircleProgressView extends View {
    private int innerRaduis = 200;
    private Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int progress = 0;
    private RectF arcRectf = new RectF();
    private Bitmap iconBitmap;
    private Rect bitmapRect;

    public LottoCircleProgressView(Context context) {
        this(context, null);
    }

    public LottoCircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LottoCircleProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        progressPaint.setColor(0xFFF7A400);
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeJoin(Paint.Join.ROUND);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(dip2px(4));

        iconBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_progress_ball);
        bitmapRect = new Rect();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(width, height);
        setMeasuredDimension(size, size);

        innerRaduis = size / 2 - dip2px(30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(getWidth() / 2, getHeight() / 2);
        arcRectf.set(-innerRaduis, -innerRaduis, innerRaduis, innerRaduis);
        canvas.drawArc(arcRectf, -90, progress, false, progressPaint);

        int left = (int) (innerRaduis * Math.sin(Math.PI * (double) (progress * 1.0f / 180)) - (iconBitmap.getWidth() + 20) / 2);
        int top = (int) (-innerRaduis * Math.cos(Math.PI * (double) (progress * 1.0f / 180)) - (iconBitmap.getHeight() + 20) / 2);
        int right = (int) (innerRaduis * Math.sin(Math.PI * (double) (progress * 1.0f / 180)) + (iconBitmap.getWidth() + 20) / 2);
        int bottom = (int) (-innerRaduis * Math.cos(Math.PI * (double) (progress * 1.0f / 180)) + (iconBitmap.getHeight() + 20) / 2);
        bitmapRect.set(left, top, right, bottom);

        canvas.drawBitmap(iconBitmap, null, bitmapRect, null);
        super.onDraw(canvas);
    }

    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startProgressAnim();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearAnimation();
    }

    private void startProgressAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 360);
        valueAnimator.setDuration(60000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = ((int) animation.getAnimatedValue());
                invalidate();
            }
        });

        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.start();
    }


}
