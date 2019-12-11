package org.think.finance.lottoview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Description：.
 * Author：Created by YJ_Song on 2019-12-02.
 * Email:  songyuanjin@innotechx.com
 */
public class LottoView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int raduis = 200;
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint dashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int innerRaduis = 200;
    private int midRadius = 200;
    private int lineLength = 10;
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF rectF = new RectF();

    public LottoView(Context context) {
        this(context, null);
    }

    public LottoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LottoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.BLACK);

        linePaint.setColor(0xFF5C616A);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(dip2px(1));

        dashPaint.setColor(0xFF909090);
        dashPaint.setAntiAlias(true);
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setPathEffect(new DashPathEffect(new float[]{24, 4}, 0));
        dashPaint.setStrokeWidth(dip2px(2));

        textPaint.setColor(0xFF909090);
        textPaint.setTextSize(sp2px(12));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(width, height);
        setMeasuredDimension(size, size);

        raduis = size / 2;
        midRadius = raduis - dip2px(15);
        innerRaduis = raduis - dip2px(30);

        lineLength = midRadius - innerRaduis - dip2px(8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(getWidth() / 2.0f, getHeight() / 2.0f);
        //绘制背景圆
        canvas.drawCircle(0, 0, raduis, paint);

        //绘制直线
        canvas.drawLine(0, -innerRaduis, 0, -midRadius + lineLength, linePaint);
        canvas.drawLine(innerRaduis, 0, midRadius - lineLength, 0, linePaint);
        canvas.drawLine(0, innerRaduis, 0, midRadius - lineLength, linePaint);
        canvas.drawLine(-innerRaduis, 0, -midRadius + lineLength, 0, linePaint);

        //绘制圆弧
        rectF.set(-midRadius, -midRadius, midRadius, midRadius);
        canvas.drawArc(rectF, -85, 80, false, linePaint); //5
        canvas.drawArc(rectF, 5, 80, false, linePaint);  //10---- 85
        canvas.drawArc(rectF, 95, 80, false, linePaint);
        canvas.drawArc(rectF, 185, 80, false, linePaint);

        linePaint.setStrokeWidth(dip2px(3));
        canvas.drawCircle(0, 0, innerRaduis, linePaint);

        canvas.drawText("0", -textPaint.measureText("0") / 2, -midRadius + getTextDiffY(textPaint), textPaint);
        canvas.drawText("30", midRadius - textPaint.measureText("30") / 2, getTextDiffY(textPaint), textPaint);
        canvas.drawText("60", -textPaint.measureText("60") / 2, midRadius + getTextDiffY(textPaint), textPaint);
        canvas.drawText("90", -midRadius - textPaint.measureText("90") / 2, getTextDiffY(textPaint), textPaint);

        drawBalls(canvas);
        super.onDraw(canvas);
    }

    private void drawBalls(Canvas canvas) {
        float r = dip2px(18);
        float midBallDiffY = innerRaduis - dip2px(2) - r;
        float minInnerRadius = midBallDiffY - r * 2;

        double eachMidArc = Math.toDegrees(Math.asin(r * 1.0d / midBallDiffY)) * 2;
        double eachInnerArc = Math.toDegrees(Math.asin(r * 1.0d / minInnerRadius)) * 2;
        Random random = new Random();
        for (int i = 5; i > 0; i--) {
            Ball ballX = new Ball();
            float x = (float) ((innerRaduis - dip2px(2) - r) * (Math.sin(Math.toRadians(eachMidArc * (5 - i)))));
            float y = (float) ((innerRaduis - dip2px(2) - r) * (Math.cos(Math.toRadians(eachMidArc * (5 - i)))));
            ballX.cx = x;
            ballX.cy = y;
            ballX.num = String.valueOf(random.nextInt(69) + 1);
            ballX.rotateDegress = random.nextInt(90) + 10;
            canvas.drawCircle(ballX.cx, ballX.cy, ballX.radius, dashPaint);
            canvas.rotate(ballX.rotateDegress, x, y);
            canvas.drawText(ballX.num, x - textPaint.measureText(ballX.num) / 2, y + getTextDiffY(textPaint), textPaint);
            canvas.rotate(-ballX.rotateDegress, x, y);
        }

        for (int i = 4; i > 0; i--) {
            Ball ballX = new Ball();
            float x = -(float) ((innerRaduis - dip2px(2) - r) * (Math.sin(Math.toRadians(eachMidArc * (5 - i)))));
            float y = (float) ((innerRaduis - dip2px(2) - r) * (Math.cos(Math.toRadians(eachMidArc * (5 - i)))));
            ballX.cx = x;
            ballX.cy = y;
            ballX.num = String.valueOf(random.nextInt(69) + 1);
            canvas.drawCircle(ballX.cx, ballX.cy, ballX.radius, dashPaint);
            ballX.rotateDegress = random.nextInt(90) + 10;
            canvas.rotate(ballX.rotateDegress, x, y);
            canvas.drawText(ballX.num, x - textPaint.measureText(ballX.num) / 2, y + getTextDiffY(textPaint), textPaint);
            canvas.rotate(-ballX.rotateDegress, x, y);

        }

        for (int i = 3; i > 0; i--) {
            Ball ballX = new Ball();
            float x = (float) (minInnerRadius * (Math.sin(Math.PI * (eachInnerArc * (3 - i) / 180.0f))));
            float y = (float) (minInnerRadius * (Math.cos(Math.PI * (eachInnerArc * (3 - i) / 180.0f))));
            ballX.cx = x;
            ballX.cy = y;
            ballX.num = String.valueOf(random.nextInt(69) + 1);
            ballX.rotateDegress = random.nextInt(90) + 10;
            canvas.drawCircle(ballX.cx, ballX.cy, ballX.radius, dashPaint);
            canvas.rotate(ballX.rotateDegress, x, y);
            canvas.drawText(ballX.num, x - textPaint.measureText(ballX.num) / 2, y + getTextDiffY(textPaint), textPaint);
            canvas.rotate(-ballX.rotateDegress, x, y);

        }

        for (int i = 2; i > 0; i--) {
            Ball ballX = new Ball();
            float x = -(float) (minInnerRadius * (Math.sin(Math.PI * (eachInnerArc * (3 - i) / 180.0f))));
            float y = (float) (minInnerRadius * (Math.cos(Math.PI * (eachInnerArc * (3 - i) / 180.0f))));
            ballX.cx = x;
            ballX.cy = y;
            ballX.num = String.valueOf(random.nextInt(69) + 1);
            ballX.rotateDegress = random.nextInt(90) + 10;
            canvas.drawCircle(ballX.cx, ballX.cy, ballX.radius, dashPaint);
            canvas.rotate(ballX.rotateDegress, x, y);
            canvas.drawText(ballX.num, x - textPaint.measureText(ballX.num) / 2, y + getTextDiffY(textPaint), textPaint);
            canvas.rotate(-ballX.rotateDegress, x, y);
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

    private class Ball {
        int radius = dip2px(18);
        String num = "30";
        float cx;
        float cy;
        float rotateDegress = 30;
    }

}
