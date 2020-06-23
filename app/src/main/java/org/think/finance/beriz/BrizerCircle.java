package org.think.finance.beriz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：.
 * Author：Created by YJ_Song on 2019-11-04.
 * Email:  songyuanjin@innotechx.com
 */
public class BrizerCircle extends View {
    private Paint mRedPaint;
    private List<PointF> controllerPonits = new ArrayList<>();

    private int witdh, height;
    private int rdaius = 0;

    private float ratio = 0.55f;

    private Path mPath;

    public BrizerCircle(Context context) {
        this(context, null);
    }

    public BrizerCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrizerCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRedPaint.setColor(Color.RED);
        mRedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        witdh = getWidth();
        height = getHeight();
        rdaius = Math.min(witdh, height) / 2;

        calculateControllerPoints();

        canvas.translate(witdh / 2, height / 2);

        for (int i = 0; i < 4; i++) {
            int endPointIndex;
            if (i == 3) {
                endPointIndex = 0;
            } else {
                endPointIndex = i * 3 + 3;
            }
            if (i == 0) {
                mPath.moveTo(controllerPonits.get(i * 3).x, controllerPonits.get(i * 3).y);
            } else {
                mPath.lineTo(controllerPonits.get(i * 3).x, controllerPonits.get(i * 3).y);
            }
            mPath.cubicTo(controllerPonits.get(i * 3 + 1).x, controllerPonits.get(i * 3 + 1).y, controllerPonits.get(i * 3 + 2).x, controllerPonits.get(i * 3 + 2).y, controllerPonits.get(endPointIndex).x, controllerPonits.get(endPointIndex).y);
        }

        canvas.drawPath(mPath, mRedPaint);
    }


    private void calculateControllerPoints() {
        float controllWidth = rdaius * ratio;
        Log.d("BrizerCircle", "rdaius=" + rdaius);
        controllerPonits.clear();
        //右上
        controllerPonits.add(new PointF(0, -rdaius));
        controllerPonits.add(new PointF(controllWidth, -rdaius));
        controllerPonits.add(new PointF(rdaius, -controllWidth));

        //右下
        controllerPonits.add(new PointF(rdaius, 0));
        controllerPonits.add(new PointF(rdaius, controllWidth));
        controllerPonits.add(new PointF(controllWidth, rdaius));

        //左下
        controllerPonits.add(new PointF(0, rdaius));
        controllerPonits.add(new PointF(-controllWidth, rdaius));
        controllerPonits.add(new PointF(-rdaius, controllWidth));

        //左上
        controllerPonits.add(new PointF(-rdaius, 0));
        controllerPonits.add(new PointF(-rdaius, -controllWidth));
        controllerPonits.add(new PointF(-controllWidth, -rdaius));
    }
}
