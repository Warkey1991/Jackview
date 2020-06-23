package org.think.finance.progress

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import org.think.finance.raffleview.R
import kotlin.concurrent.thread

/**
 *Description：.
 *Author：Created by YJ_Song on 2020/6/18.
 *Email:  songyuanjin@innotechx.com
 */
class SegmentProgress @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {
    private val bgPaint = Paint()
    private val progressPaint = Paint()
    private val textPaint = Paint()
    private val rect = RectF()
    var segments = mutableListOf<Segment>()
        set(value) {
            field = value
            segmentMaxs.clear()
            segmentMaxs.add(segments[0].progress)
            for (i in 0 until segments.size - 1) {
                segmentMaxs.add(segments[i + 1].progress - segments[i].progress)
            }
        }

    var progress = 0
        set(value) {
            field = value
            currentSegmentProgress = progress
            invalidate()
        }
    var currentSegmentProgress = 0
    var segmentMaxs = mutableListOf<Int>()
    var progressPadding = 0

    private val horizontalRound = dip2px(5f).toFloat()
    private val imageBitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.box_l5)
    private val lightBitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_halo)
    private val rotateAngle = listOf(-5.0f, -4f, -3f, -2f, -1f, 2f, 3f, 4f, 5f, -5.0f, -4f, -3f, -2f, -1f, 2f, 3f, 4f, 5f, -5.0f, -4f, -3f, -2f, -1f, 2f, 3f, 4f, 5f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    var animCount = 0
    var needStopThread = false
    private var touchCallBackRectF = mutableListOf<RectF>()

    init {
        bgPaint.flags = Paint.ANTI_ALIAS_FLAG
        bgPaint.color = Color.parseColor("#1e1d45")
        bgPaint.strokeCap = Paint.Cap.ROUND
        bgPaint.strokeJoin = Paint.Join.ROUND
        bgPaint.strokeWidth = dip2px(5f).toFloat()

        progressPaint.flags = Paint.ANTI_ALIAS_FLAG
        progressPaint.color = Color.RED
        progressPaint.strokeCap = Paint.Cap.ROUND
        progressPaint.strokeJoin = Paint.Join.ROUND
        progressPaint.strokeWidth = dip2px(5f).toFloat()

        textPaint.color = Color.WHITE
        textPaint.flags = Paint.ANTI_ALIAS_FLAG
        textPaint.textSize = sp2px(12f).toFloat()

        initData()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                val x = event.x
                val y = event.y
                for (i in 0 until touchCallBackRectF.size) {
                    if (touchCallBackRectF[i].contains(x, y)) {
                        segments[i].status = BoxStatus.AVAILABLE.ordinal
                        Toast.makeText(context, "click index:${i}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        rect.set(0f, (height / 2 - dip2px(5f)).toFloat(), width.toFloat(), (height / 2 + dip2px(5f)).toFloat())
        canvas?.drawRoundRect(rect, horizontalRound, horizontalRound, bgPaint)

        val eachWidth = (width - lightBitmap.width) / segments.size
        val index = findRange()
        //转为float,可以预防divide by zero 的异常
        val smallEachWidth = eachWidth.toFloat() / segmentMaxs[index].toFloat()
        var x = eachWidth * index + smallEachWidth * currentSegmentProgress
        x = x.coerceAtMost(width.toFloat())
        rect.right = x
        canvas?.drawRoundRect(rect, horizontalRound, horizontalRound, progressPaint)

        drawBitmaps(canvas)
    }

    private fun drawBitmaps(canvas: Canvas?) {
        touchCallBackRectF.clear()
        val eachWidth = width / segments.size
        val rectF = RectF((eachWidth - lightBitmap.width).toFloat(), ((height - lightBitmap.height) / 2).toFloat(), eachWidth.toFloat(), ((height + lightBitmap.height) / 2).toFloat())
        val innerRectF = RectF()
        val degrees = rotateAngle[animCount % rotateAngle.size]
        for (i in 0 until segments.size) {
            rectF.left = (eachWidth * (i + 1) - lightBitmap.width).toFloat()
            rectF.right = (eachWidth * (i + 1)).toFloat()
            if (segments[i].status == BoxStatus.AVAILABLE.ordinal) {
                canvas?.drawBitmap(lightBitmap, null, rectF, null)
            }
            touchCallBackRectF.add(RectF(rectF))

            innerRectF.left = rectF.centerX() - imageBitmap.width / 2
            innerRectF.right = rectF.centerX() + imageBitmap.width / 2
            innerRectF.top = rectF.centerY() - imageBitmap.height / 2
            innerRectF.bottom = rectF.centerY() + imageBitmap.height / 2
            val numText = segments[i].progress.toString()
            canvas?.drawText(numText, innerRectF.centerX() - textPaint.measureText(numText), rectF.bottom + dip2px(4f), textPaint)
            if (segments[i].status == BoxStatus.AVAILABLE.ordinal) {
                canvas?.save()
                canvas?.rotate(degrees, rectF.centerX(), rectF.centerY())
                canvas?.drawBitmap(imageBitmap, null, innerRectF, null)
                canvas?.restore()
            } else {
                canvas?.drawBitmap(imageBitmap, null, innerRectF, null)
            }
        }
    }

    private fun findRange(): Int {
        if (progress >= segments.last().progress) {
            progressPadding = 0
            return segments.size - 1
        }
        var index = 0
        for (i in 0 until segments.size - 1) {
            if (progress in segments[i].progress..segments[i + 1].progress) {
                index = i + 1
            }
        }
        if (index >= 1) {
            currentSegmentProgress = progress - segments[index - 1].progress
        }
        return index
    }

    private fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    private fun sp2px(spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        needStopThread = false
        rotate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        needStopThread = true
    }

    private fun rotate() {
        thread {
            while (!needStopThread) {
                if (animCount == rotateAngle.size) {
                    animCount = 0
                }
                Thread.sleep(16)
                post {
                    invalidate()
                }
                animCount++
            }
        }
    }

    open class Segment {
        var status: Int = 0
        var progress: Int = 0
    }

    enum class BoxStatus {
        UNAVAILABLE, AVAILABLE, OPENED
    }

    private fun initData() {
        val segment1 = Segment()
        segment1.progress = 3
        segment1.status = BoxStatus.AVAILABLE.ordinal
        segments.add(segment1)

        val segment2 = Segment()
        segment2.progress = 5
        segment2.status = BoxStatus.UNAVAILABLE.ordinal
        segments.add(segment2)

        val segment3 = Segment()
        segment3.progress = 8
        segment3.status = BoxStatus.UNAVAILABLE.ordinal
        segments.add(segment3)

        segmentMaxs.clear()
        segmentMaxs.add(segments[0].progress)
        for (i in 0 until segments.size - 1) {
            segmentMaxs.add(segments[i + 1].progress - segments[i].progress)
        }

        progress = 4
    }
}