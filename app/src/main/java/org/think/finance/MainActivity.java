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
    }

    public void nineClick(View v) {
        startActivity(new Intent(MainActivity.this, LuckyDrawActivity.class));
    }

    public void lottoClick(View v) {
        startActivity(new Intent(MainActivity.this, LottoViewActivity.class));

    }
}
