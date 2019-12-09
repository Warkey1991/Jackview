package org.think.finance.lottoview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import org.think.finance.raffleview.R;

/**
 * Description：.
 * Author：Created by YJ_Song on 2019-12-03.
 * Email:  songyuanjin@innotechx.com
 */
public class LottoCountDownView extends RelativeLayout {

    public LottoCountDownView(Context context) {
        this(context, null);
    }

    public LottoCountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LottoCountDownView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.layout_lotto_count_wodn_view, this, true);
    }
}
