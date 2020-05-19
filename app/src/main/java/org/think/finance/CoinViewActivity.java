package org.think.finance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.think.finance.coinview.NewCoinMoveView;
import org.think.finance.raffleview.R;

/**
 * Description：.
 * Author：Created by YJ_Song on 2019-12-10.
 * Email:  songyuanjin@innotechx.com
 */
public class CoinViewActivity extends AppCompatActivity {
    private NewCoinMoveView newCoinMoveView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_coin_view);

        newCoinMoveView = findViewById(R.id.new_coinMoveView);
        newCoinMoveView.postDelayed(new Runnable() {
            @Override
            public void run() {
                newCoinMoveView.startAnimation();
            }
        }, 1000);
    }
}
