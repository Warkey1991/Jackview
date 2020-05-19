package org.think.finance.coinview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.think.finance.raffleview.R;

/**
 * Description：.
 * Author：Created by YJ_Song on 2019-12-12.
 * Email:  songyuanjin@innotechx.com
 */
public class NewCoinMoveView extends RelativeLayout {
    private ImageView icon1;
    private ImageView icon2;
    private ImageView icon3;
    private ImageView icon4;
    private ImageView icon5;
    private ImageView icon6;
    private ImageView icon7;
    private ImageView icon8;
    private ImageView icon9;
    private ImageView icon10;
    private ImageView icon11;
    private ImageView icon12;

    private View[] views = new View[12];

    private float startX, startY, endX, endY, fleX1, fleY1, fleX2, fleY2;

    public NewCoinMoveView(Context context) {
        this(context, null);
    }

    public NewCoinMoveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewCoinMoveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.layout_coin_view, this, true);
        icon1 = findViewById(R.id.icon_1);
        icon2 = findViewById(R.id.icon_2);
        icon3 = findViewById(R.id.icon_3);
        icon4 = findViewById(R.id.icon_4);
        icon5 = findViewById(R.id.icon_5);
        icon6 = findViewById(R.id.icon_6);
        icon7 = findViewById(R.id.icon_7);
        icon8 = findViewById(R.id.icon_8);
        icon9 = findViewById(R.id.icon_9);
        icon10 = findViewById(R.id.icon_10);
        icon11 = findViewById(R.id.icon_11);
        icon12 = findViewById(R.id.icon_12);

        views[0] = icon1;
        views[1] = icon2;
        views[2] = icon3;
        views[3] = icon4;
        views[4] = icon5;
        views[5] = icon6;
        views[6] = icon7;
        views[7] = icon8;
        views[8] = icon9;
        views[9] = icon10;
        views[10] = icon11;
        views[11] = icon12;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);

        startX = getWidth() / 2;
        startY = getHeight() / 2;
        endX = width - 100;
        endY = 100;

        fleX1 = (startX + endX) / 6;
        fleY1 = (startY + endY) / 2;

        PointF pointF = reflexPoint(startX, startY, endX, endY, fleX1, fleY1);
        fleX2 = pointF.x;
        fleY2 = pointF.y;
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

    private void createLeftViewMove(View view, long delay) {
        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo(fleX1, fleY1, endX, endY);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
        objectAnimator.setDuration(1200);
        objectAnimator.setStartDelay(delay);
        objectAnimator.start();
    }

    private void createRightViewMove(View view, long delay) {
        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo(fleX2, fleY2, endX, endY);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
        objectAnimator.setDuration(1200);
        objectAnimator.setStartDelay(delay);
        objectAnimator.start();
    }


    public void startAnimation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 6; i++) {
                    final int index = i;
                    post(new Runnable() {
                        @Override
                        public void run() {
                            createLeftViewMove(views[index], index * 50);
                            createRightViewMove(views[index + 6], (index+1) * 50);
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
