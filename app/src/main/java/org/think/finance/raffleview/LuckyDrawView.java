package org.think.finance.raffleview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

/**
 * Description：.
 * Author：Created by YJ_Song on 2019-11-25.
 * Email:  songyuanjin@innotechx.com
 */
public class LuckyDrawView extends View {
    private static final String TAG = "LuckyDrawView";
    //0->1->2->3->5->6->7->8

    //0-1-2-5-8-7-6-3
    private int currentPosition = 0;
    private int stopPosition = -1;
    private final static int LOOP_COUNT = 4;
    private int currentLoopCount = 0;
    private Paint bgPaint;
    private int mWidth, mHeight;
    private int radiusBg;
    private Paint cellPaint;
    private Paint cellTextPaint;
    private int innerEachGap = dip2px(6);
    private int innerWidth, innerHeight;
    private int eachWidth, eachHeight;
    private boolean onTouchCenter = false;

    private RectF mCenterButtonRectF;
    private String[] rewardTexts = {"$0.04", "$0.10", "$0.80", "$0.85", "", "$3.00", "$5.00", "$0.15", "$0.10"};
    private int[] positions = {0, 1, 2, 5, 8, 7, 6, 3}; //顺时针
    String start = "Start";
    float scale = 1.0f;
    private boolean isRuning = false;


    public LuckyDrawView(Context context) {
        this(context, null);
    }

    public LuckyDrawView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckyDrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setStrokeCap(Paint.Cap.ROUND);
        bgPaint.setStrokeJoin(Paint.Join.ROUND);
        bgPaint.setAntiAlias(true);
        bgPaint.setDither(true);
        bgPaint.setColor(0xFFFF356B);

        cellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cellPaint.setStyle(Paint.Style.FILL);
        cellPaint.setColor(Color.WHITE);

        cellTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cellTextPaint.setTextSize(sp2px(context, 26));
        cellTextPaint.setColor(Color.WHITE);
        cellTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        cellTextPaint.setAntiAlias(true);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        onTouchCenter = false;
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        if (mCenterButtonRectF.contains(x, y) && !isRuning) {
                            if (scale != 0.8f) {
                                scale = 0.8f;
                                invalidate();
                            }
                            onTouchCenter = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (onTouchCenter) {
                            startPressScaleAnim();
                            startLoop();

                        }
                        onTouchCenter = false;
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        radiusBg = mWidth / 40;
        innerWidth = mWidth - innerEachGap * 4;
        innerHeight = mHeight - innerEachGap * 4;
        eachWidth = innerWidth / 3;
        eachHeight = innerHeight / 3;
        drawNineCell(canvas);
    }

    private void drawNineCell(Canvas canvas) {
        int nums = 9;
        RectF rectF = new RectF();
        for (int i = 0; i < nums; i++) {
            int startX = innerEachGap + (i % 3) * (eachWidth + innerEachGap);
            int startY = innerEachGap + (i / 3) * (eachHeight + innerEachGap);
            rectF.set(startX, startY, startX + eachWidth, startY + eachHeight);
            if (i == nums / 2) {
                cellPaint.setColor(0xFFFFE535);
                bgPaint.setColor(0xFFFF356B);
                rectF.set(rectF.left + rectF.left * (1 - scale) * 0.08f, rectF.top + rectF.top * (1 - scale) * 0.08f, rectF.right - rectF.right * (1 - scale) * 0.08f, rectF.bottom - rectF.bottom * (1 - scale) * 0.08f);
                canvas.drawRoundRect(rectF, radiusBg, radiusBg, cellPaint);
                mCenterButtonRectF = new RectF(rectF);
                rectF.set(rectF.left + dip2px(10), rectF.top + dip2px(10), rectF.right - dip2px(10), rectF.bottom - dip2px(10));
                canvas.drawRoundRect(rectF, radiusBg, radiusBg, bgPaint);
                cellTextPaint.setColor(Color.WHITE);
                canvas.drawText(start, rectF.centerX() - cellTextPaint.measureText(start) / 2, rectF.centerY() + getTextDiffY(cellTextPaint), cellTextPaint);
            } else {
                if (positions[currentPosition] == i) {
                    cellPaint.setColor(0xFFFBC01B);
                    cellTextPaint.setColor(Color.WHITE);
                } else {
                    cellPaint.setColor(Color.WHITE);
                    cellTextPaint.setColor(0xFFFF5A00);
                }
                canvas.drawRoundRect(rectF, radiusBg, radiusBg, cellPaint);
                canvas.drawText(rewardTexts[i], rectF.centerX() - cellTextPaint.measureText(rewardTexts[i]) / 2, rectF.centerY() + getTextDiffY(cellTextPaint), cellTextPaint);
            }
        }
    }


    private float getTextDiffY(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return Math.abs(fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
    }

    private void startLoop() {
        currentLoopCount = 0;
        Random random = new Random();
        stopPosition = random.nextInt(7);
        currentPosition = 0;
        new Thread(action).start();
    }

    private Runnable action = new Runnable() {
        @Override
        public void run() {
            while (true) {
                isRuning = true;
                if (currentLoopCount >= LOOP_COUNT) {
                    isRuning = false;
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "恭喜你抽中了position=" + stopPosition + "(" + rewardTexts[positions[stopPosition]] + ")", Toast.LENGTH_LONG).show();
                        }
                    }, 500);
                    break;
                }
                currentPosition++;
                if (currentPosition > 7) {
                    currentLoopCount++;
                    currentPosition = 0;
                }

                post(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });

                if (currentLoopCount == LOOP_COUNT - 1) {
                    if (currentPosition % 7 == stopPosition) {
                        if (currentPosition == stopPosition) {
                            currentLoopCount = LOOP_COUNT;
                        }
                    }
                    SystemClock.sleep(100 * (currentPosition + 1));
                } else {
                    SystemClock.sleep(100);
                }
            }

        }
    };

    private void startPressScaleAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.8f, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scale = ((float) animation.getAnimatedValue());
                invalidate();
            }
        });
        valueAnimator.setDuration(300);
        valueAnimator.start();
    }

    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


}
