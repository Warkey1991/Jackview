package org.think.finance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.think.finance.raffleview.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.coinRain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CoinRainActivity.class));
            }
        });
    }

    public void nineClick(View v) {
        startActivity(new Intent(MainActivity.this, LuckyDrawActivity.class));
    }

    public void lottoClick(View v) {
        startActivity(new Intent(MainActivity.this, LottoViewActivity.class));
    }

    public void coinClick(View v) {
        startActivity(new Intent(MainActivity.this, CoinViewActivity.class));
    }

    public void sportClick(View v) {
        startActivity(new Intent(MainActivity.this, SportActivity.class));
    }

    public void checkInClick(View v) {
        startActivity(new Intent(MainActivity.this, CheckInActivity.class));
    }

    public void startBeriz(View v) {
        startActivity(new Intent(MainActivity.this, SVGActivity.class));
    }


}
