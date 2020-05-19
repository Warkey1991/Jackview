package org.think.finance.sportviews;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description：.
 * Author：Created by YJ_Song on 2019-12-15.
 * Email:  songyuanjin@innotechx.com
 */
public class SportView extends View {
    private Paint mPaint;
    private Paint linePaint;
    private float bigRadius = 200;
    private int lineCount = 120;
    private int degress = 3;  //度数
    private float innerDashRadius;
    private float outDashRadius;
    private float gradientRadius;
    private int[] colors = {0xFF249CFF, 0xFF6B43FF, 0xFF349AFA, 0xFF6B43FF, 0xFF41F99E, 0xFF12E0FA, 0xFF41F99E, 0xFF249CFF};
    private SweepGradient mSweepGradient;
    private float smallRadius = 120;
    private float currentProgress = 60;
    private Paint mProgressPaint;
    private RectF arcRectF = new RectF();

    public SportView(Context context) {
        this(context, null);
    }

    public SportView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SportView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(4);
        mPaint.setColor(0xFF1D2D4E);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(0xFF1C2F52);
        linePaint.setStrokeWidth(dip2px(2));
        linePaint.setStrokeCap(Paint.Cap.ROUND);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(dip2px(8));
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(0xFFFF5A94);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);

        bigRadius = size / 2 - dip2px(2);
        gradientRadius = bigRadius - dip2px(30);
        innerDashRadius = gradientRadius + dip2px(10);
        outDashRadius = innerDashRadius + dip2px(10);
        degress = 360 / lineCount;

        smallRadius = bigRadius - dip2px(52);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(getWidth() / 2, getHeight() / 2);
        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(dip2px(3));
        mPaint.setColor(0xFF1D2D4E);
        canvas.drawCircle(0, 0, bigRadius, mPaint);

        drawDashCircle(canvas);

        mSweepGradient = new SweepGradient(0, 0, colors, null);
        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(dip2px(8));
        mPaint.setShader(mSweepGradient);
        canvas.drawCircle(0, 0, gradientRadius, mPaint);

        //绘制最小圆
        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(dip2px(1));
        mPaint.setColor(0xFF909190);

        arcRectF.set(-smallRadius, -smallRadius, smallRadius, smallRadius);
        canvas.drawArc(arcRectF, 184, 172, false, mPaint);
        mPaint.setPathEffect(new DashPathEffect(new float[]{20, 6}, 0));
        canvas.drawArc(arcRectF, 0, 180, false, mPaint);

        canvas.drawArc(arcRectF, 270, currentProgress, false, mProgressPaint);
        super.onDraw(canvas);
    }


    private void drawDashCircle(Canvas canvas) {
        float startX = 0, startY = 0, endX = 0, endY = 0;
        float currentDegress;

        for (int i = 0; i < lineCount; i++) {
            currentDegress = degress * i;
            linePaint.setColor(0xff408AFC);
            if (currentDegress == 0) {
                startX = innerDashRadius;
                startY = 0;
                endX = outDashRadius + dip2px(4);
                endY = 0;
            } else if (currentDegress == 90) {
                startX = 0;
                endX = 0;
                startY = innerDashRadius;
                endY = outDashRadius + dip2px(4);
            } else if (currentDegress == 180) {
                startY = 0;
                endY = 0;
                startX = -innerDashRadius;
                endX = -outDashRadius - dip2px(4);
            } else if (currentDegress == 270) {
                startX = 0;
                endX = 0;
                startY = -innerDashRadius;
                endY = -outDashRadius - dip2px(4);
            } else {
                linePaint.setColor(0xFF1C2F52);
                startX = (float) (innerDashRadius * Math.cos(currentDegress * Math.PI / 180));
                startY = (float) (innerDashRadius * Math.sin(currentDegress * Math.PI / 180));
                endX = (float) (outDashRadius * Math.cos(currentDegress * Math.PI / 180));
                endY = (float) (outDashRadius * Math.sin(currentDegress * Math.PI / 180));
            }
            canvas.drawLine(startX, startY, endX, endY, linePaint);
        }
    }


    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    private float getTextDiffY(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return Math.abs(fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
    }
}
