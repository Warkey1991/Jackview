package org.think.finance.coinrain

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import org.think.finance.raffleview.R
/**
 *Description：.
 *Author：Created by YJ_Song on 2020/6/10.
 *Email:  songyuanjin@innotechx.com
 */
class CoinRainView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {
    val coinPaint = Paint()

    //初始化金币Bitmap
    var coinBS = mutableListOf<Bitmap>()
    var coins = mutableListOf<Coin>()

    init {
        coinPaint.flags = Paint.ANTI_ALIAS_FLAG
        coinBS.add(BitmapFactory.decodeResource(context.resources, R.mipmap.b1))
        coinBS.add(BitmapFactory.decodeResource(context.resources, R.mipmap.b2))
        coinBS.add(BitmapFactory.decodeResource(context.resources, R.mipmap.b3))
        coinBS.add(BitmapFactory.decodeResource(context.resources, R.mipmap.b4))
        coinBS.add(BitmapFactory.decodeResource(context.resources, R.mipmap.b5))
        coinBS.add(BitmapFactory.decodeResource(context.resources, R.mipmap.b6))

        createCoin(5)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawCoins(canvas)
    }

    private fun createCoin(count: Int) {
        for (index in 0 until count) {
            val coin = Coin()
            coin.x = index * (dip2px(10f).toInt()..dip2px(100f).toInt()).random().toFloat()
            coin.y = (0..dip2px(20f).toInt()).random().toFloat()
            coin.speed = (20..50).random().toFloat()
            coin.image = coinBS[index % 6]
            coins.add(coin)
        }
    }

    private fun drawCoins(canvas: Canvas?) {
        for (index in 0 until coins.size) {
            val newCoin = Coin()
            newCoin.x = coins[index].x
            newCoin.y = coins[index].y + coins[index].speed
            newCoin.speed = (20..50).random().toFloat()
            newCoin.image = coins[index].image
            canvas?.drawBitmap(coins[index].image!!, coins[index].x, coins[index].y, coinPaint)
            coins[index] = newCoin
        }
    }


    fun addCoin() {
        val drawThread = object : Thread() {
            override fun run() {
                super.run()
                while (coins.size <= 100) {
                    sleep(1000)
                    createCoin((3..6).random())
                }
            }
        }
        drawThread.start()
    }


    fun startDraw() {
        val drawThread = object : Thread() {
            override fun run() {
                super.run()
                while (true) {
                    sleep(16)
                    post {
                        invalidate()
                    }
                    if (coins.size > 100) {
                        coins.clear()
                        return
                    }
                }
            }
        }
        drawThread.start()
    }


    private fun dip2px(dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toFloat()
    }

    fun sp2px(spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

}