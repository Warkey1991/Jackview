package org.think.finance.check

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import org.think.finance.raffleview.R
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 *Description：签到环形进度条
 *Author：Created by YJ_Song on 2020/5/30.
 *Email:  songyuanjin@innotechx.com
 */
class CheckInCircleProgressView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {
    private val TAG = "CheckInCircleProgress"
    private val outerPaint = Paint() //外圈画笔
    private val innerPaint = Paint() //内圈画笔
    private val thickPaint = Paint() //粗进度条画笔
    private val thinPaint = Paint()  //细进度条画笔
    private val dayPaint = Paint()   //文本画笔
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var outerRadius = 0
    private var innerRadius = 0
    private val colors = intArrayOf(Color.parseColor("#FE9D23"), Color.parseColor("#FA8328"), Color.parseColor("#F5662D"), Color.parseColor("#F4632E"), Color.parseColor("#F15032"), Color.parseColor("#F15131"), Color.parseColor("#FA8428"), Color.parseColor("#FE9D23"))
    private val checkInDaysCount = 6
    private var coinBitmap: Bitmap
    private var coinWidth = 0
    private var coinHeight = 0
    private var viewWitdh = 0
    private var viewHeight = 0
    private val days = listOf("Day1", "Day2", "Day3", "Day4", "Day5", "Day6", "Day7")

    init {
        //初始化画笔的属性
        outerPaint.flags = Paint.ANTI_ALIAS_FLAG
        outerPaint.color = Color.parseColor("#1e1d45")
        outerPaint.isDither = true
        outerPaint.style = Paint.Style.STROKE
        outerPaint.strokeWidth = dip2px(10f).toFloat()

        innerPaint.flags = Paint.ANTI_ALIAS_FLAG
        innerPaint.color = Color.parseColor("#28284e")
        innerPaint.isDither = true
        innerPaint.style = Paint.Style.STROKE
        innerPaint.strokeWidth = dip2px(20f).toFloat()

        thickPaint.flags = Paint.ANTI_ALIAS_FLAG
        thickPaint.isDither = true
        thickPaint.style = Paint.Style.STROKE
        thickPaint.strokeCap = Paint.Cap.ROUND
        thickPaint.strokeWidth = dip2px(6f).toFloat()

        thinPaint.flags = Paint.ANTI_ALIAS_FLAG
        thinPaint.isDither = true
        thinPaint.style = Paint.Style.STROKE
        thinPaint.strokeCap = Paint.Cap.ROUND
        thinPaint.color = Color.WHITE
        thinPaint.strokeWidth = dip2px(2f).toFloat()

        dayPaint.flags = Paint.ANTI_ALIAS_FLAG
        dayPaint.color = Color.WHITE
        dayPaint.isDither = true
        dayPaint.textSize = sp2px(9f).toFloat()

        coinBitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_coin)
        coinWidth = coinBitmap.width
        coinHeight = coinBitmap.height
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWitdh = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = viewWitdh - dip2px(10f)
        mHeight = viewHeight - dip2px(10f)

        mWidth = mWidth.coerceAtMost(mHeight)
        mHeight = mWidth
        outerRadius = mWidth / 2 - dip2px(5f)
        innerRadius = outerRadius - dip2px(15f)
        thickPaint.shader = SweepGradient(outerRadius.toFloat(), outerRadius.toFloat(), colors, null)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.translate((viewWitdh / 2).toFloat(), (viewHeight / 2).toFloat())
        //画外圆
        canvas?.drawCircle(0f, 0f, outerRadius.toFloat(), outerPaint)
        //画内圆
        canvas?.drawCircle(0f, 0f, innerRadius.toFloat(), innerPaint)
        //画粗线条
        canvas?.drawCircle(0f, 0f, (innerRadius - dip2px(9f)).toFloat(), thickPaint)
        //画圆弧
        canvas?.drawArc(-innerRadius.toFloat(), -innerRadius.toFloat(), innerRadius.toFloat(), innerRadius.toFloat(), -225f, 270f, false, thinPaint)

        drawCoinBitmapWithText(canvas)
    }

    private fun drawCoinBitmapWithText(canvas: Canvas?) {
        var eachCellStartX = 0
        var eachCellStartY = 0
        val textPaddingLeft = dip2px(2f).toFloat()
        val eachAngle = 270 / checkInDaysCount
        val startAngle = 135 * PI / 180
        for (index in 0..checkInDaysCount) {
            eachCellStartX = (innerRadius * cos((startAngle + eachAngle * PI / 180 * index))).toInt() - coinWidth / 2
            eachCellStartY = (innerRadius * sin((startAngle + eachAngle * PI / 180 * index))).toInt() - coinHeight / 2
            canvas?.drawBitmap(coinBitmap, eachCellStartX.toFloat(), eachCellStartY.toFloat(), null)
            val day = days[index]
            if (checkInDaysCount % 2 == 0) {
                if (index < checkInDaysCount / 2) {
                    canvas?.drawText(day, eachCellStartX - dayPaint.measureText(day) - textPaddingLeft, eachCellStartY + coinHeight / 2 + getTextDiffY(dayPaint), dayPaint)
                } else if (index > checkInDaysCount / 2) {
                    canvas?.drawText(day, eachCellStartX + coinWidth.toFloat() + textPaddingLeft, eachCellStartY + coinHeight / 2 + getTextDiffY(dayPaint), dayPaint)
                } else {
                    canvas?.drawText(day, eachCellStartX + coinWidth / 2 - dayPaint.measureText(day) / 2, eachCellStartY - getTextDiffY(dayPaint) - textPaddingLeft, dayPaint)
                }
            } else {
                if (index < checkInDaysCount / 2) {
                    canvas?.drawText(day, eachCellStartX - dayPaint.measureText(day) - textPaddingLeft, eachCellStartY + coinHeight / 2 + getTextDiffY(dayPaint), dayPaint)
                } else if (index > checkInDaysCount / 2 + 1) {
                    canvas?.drawText(day, eachCellStartX + coinWidth.toFloat() + textPaddingLeft, eachCellStartY + coinHeight / 2 + getTextDiffY(dayPaint), dayPaint)
                } else {
                    canvas?.drawText(day, eachCellStartX + coinWidth / 2 - dayPaint.measureText(day) / 2, eachCellStartY - getTextDiffY(dayPaint) - textPaddingLeft, dayPaint)
                }
            }
        }
    }

    private fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun sp2px(spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    private fun getTextDiffY(paint: Paint): Float {
        val fontMetrics = paint.fontMetrics
        return abs(fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent
    }
}
