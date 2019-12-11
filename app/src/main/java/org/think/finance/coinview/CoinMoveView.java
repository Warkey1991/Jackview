package org.think.finance.coinview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.think.finance.raffleview.R;

import java.util.ArrayList;

/**
 * Description：.
 * Author：Created by YJ_Song on 2019-12-10.
 * Email:  songyuanjin@innotechx.com
 */
public class CoinMoveView extends View {
    private static final String TAG = "CoinMoveView";
    private Path leftPath;
    private Path rightPath;
    private Paint mPaint;
    private PathMeasure pathMeasure;
    private PathMeasure rightpathMeasure;
    private float mesureLen;
    private Bitmap icon;
    private RectF iconRectF = new RectF();
    private int iconLen;
    private ArrayList<Coin> leftCoins = new ArrayList<>();
    private ArrayList<Coin> drawLeftCoins = new ArrayList<>();
    private ArrayList<Coin> rightCoins = new ArrayList<>();
    private ArrayList<Coin> drawRightCoins = new ArrayList<>();

    public CoinMoveView(Context context) {
        this(context, null);
    }

    public CoinMoveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoinMoveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_coin);
        iconLen = icon.getWidth();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setColor(Color.BLUE);
        leftPath = new Path();
        rightPath = new Path();

        pathMeasure = new PathMeasure();
        rightpathMeasure = new PathMeasure();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);

        leftPath.moveTo(width / 2, height / 2);
        leftPath.quadTo(width / 4, height / 4, width - 100, 0);
        pathMeasure.setPath(leftPath, false);
        mesureLen = pathMeasure.getLength();
        leftCoins.clear();
        for (float t = 0; t < mesureLen / (iconLen * 2.5f); t++) {
            float dis = t * iconLen * 2.5f;
            float[] point = new float[2];
            pathMeasure.getPosTan(dis, point, null);
            iconRectF.set(point[0] - iconLen / 2, point[1] - iconLen / 2, point[0] + iconLen / 2,
                    point[1] + iconLen / 2);
            leftCoins.add(new Coin(icon, new RectF(iconRectF)));
        }

        rightPath.moveTo(width / 2, height / 2);
        PointF point = reflexPoint(width / 2, height / 2, width - 100, 0, width / 4, height / 4);
        rightPath.quadTo(point.x, point.y, width - 100, 0);

        rightpathMeasure.setPath(rightPath, false);
        float rightLen = rightpathMeasure.getLength();
        rightCoins.clear();
        for (float t = 0; t < rightLen / (iconLen * 2.5f); t++) {
            float dis = t * iconLen * 2.5f;
            float[] point1 = new float[2];
            rightpathMeasure.getPosTan(dis, point1, null);
            iconRectF.set(point1[0] - iconLen / 2, point1[1] - iconLen / 2, point1[0] + iconLen / 2,
                    point1[1] + iconLen / 2);
            rightCoins.add(new Coin(icon, new RectF(iconRectF)));
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(rightPath, mPaint);
        canvas.drawPath(leftPath, mPaint);
        for (int i = 0; i < drawLeftCoins.size(); i++) {
            canvas.drawBitmap(drawLeftCoins.get(i).icon, null, drawLeftCoins.get(i).rectF, mPaint);
        }

        for (int i = 0; i < drawRightCoins.size(); i++) {
            canvas.drawBitmap(drawRightCoins.get(i).icon, null, drawRightCoins.get(i).rectF, mPaint);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        postDelayed(new Runnable() {
            @Override
            public void run() {
                createLeftCoin();
            }
        }, 1000);
    }

    /**
     * 已知直线上两点求直线的一般式方程
     * 已知直线上的两点P1(X1,Y1) P2(X2,Y2)， P1 P2两点不重合。则直线的一般式方程AX+BY+C=0中，A B C分别等于：
     * A = Y2 - Y1
     * B = X1 - X2
     * C = X2*Y1 - X1*Y2
     * <p>
     * y = -a/b * x - c/b
     * y2 = b/a * x +z
     * bx + az - ay2=0
     * z = (ay2 -bx) /a
     *
     */

    /**
     * @param x1 反射线某一点x的坐标
     * @param y1 反射线某一点y的坐标
     * @param x2 反射线某一点x的坐标
     * @param y2 反射线某一点y的坐标
     * @param x3 该点需要反射的x坐标
     * @param y3 该点需要反射的y坐标
     * @return (x3, y3)关于直线的对沉点坐标
     */
    private PointF reflexPoint(float x1, float y1, float x2, float y2, float x3, float y3) {
        float a1 = y1 - y2;
        float b1 = x2 - x1;
        float c1 = x1 * y2 - x2 * y1;

        float a2 = b1;
        float b2 = -a1;
        float c2 = (a1 * y3 - b1 * x3);

        float centerX = (b1 * c2 - b2 * c1) / (a1 * b2 - a2 * b1);
        float centerY = (a1 * c2 - a2 * c1) / (a2 * b1 - a1 * b2);

        float resultX = 2 * centerX - x3;
        float resultY = 2 * centerY - y3;

        return new PointF(resultX, resultY);
    }

    public static class Coin {
        public Bitmap icon;
        public RectF rectF;
        public float alpha = 0.8f;

        public Coin(Bitmap icon, RectF rectF) {
            this.icon = icon;
            this.rectF = rectF;
        }
    }

    int needCoinSize = 1;
    int removeCoinSize = 0;
    int interVal = 60;

    private void createLeftCoin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    drawLeftCoins.clear();
                    drawRightCoins.clear();
                    if (removeCoinSize <= leftCoins.size() / 2) {
                        for (int i = 0; i < needCoinSize; i++) {
                            drawLeftCoins.add(leftCoins.get(i));
                            drawRightCoins.add(rightCoins.get(i));
                        }
                        interVal = 60;
                    } else {
                        for (int i = removeCoinSize - needCoinSize; i < removeCoinSize; i++) {
                            drawLeftCoins.add(leftCoins.get(i));
                            drawRightCoins.add(rightCoins.get(i));
                        }
                        if (drawLeftCoins.size() > 0) {
                            drawLeftCoins.remove(0);
                            drawLeftCoins.add(leftCoins.get(removeCoinSize));
                        }
                        if (drawRightCoins.size() > 0) {
                            drawRightCoins.remove(0);
                            drawRightCoins.add(rightCoins.get(removeCoinSize));
                        }

                        interVal = 40;
                    }

                    post(new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                        }
                    });
                    try {
                        Thread.sleep(interVal);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (removeCoinSize < (leftCoins.size() - 1) && needCoinSize <= leftCoins.size() / 2) {
                        needCoinSize++;
                    }
                    if (removeCoinSize != leftCoins.size() - 1) {
                        removeCoinSize++;
                    }
                    if (removeCoinSize == leftCoins.size() - 1) {
                        needCoinSize--;
                    }
                    Log.d(TAG, "run: removeCoinSize==" + removeCoinSize + " needCoinSize=" + needCoinSize);
                    if (needCoinSize < 0) {
                        break;
                    }
                }
            }
        }).start();


    }

}
