package org.think.finance

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import org.think.finance.raffleview.R


class SVGActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_s_v_g)
    }

    fun btnClick(view: View) {
        val imageView: ImageView = view as ImageView
        val drawable: Drawable = imageView.drawable
        if (drawable is Animatable) {
            (drawable as Animatable).start()
        }
    }
}
