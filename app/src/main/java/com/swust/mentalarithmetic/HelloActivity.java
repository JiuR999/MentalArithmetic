package com.swust.mentalarithmetic;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.swust.mentalarithmetic.databinding.ActivityHelloBinding;

public class HelloActivity extends AppCompatActivity {
    ActivityHelloBinding helloBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helloBinding = ActivityHelloBinding.inflate(getLayoutInflater());
        setContentView(helloBinding.getRoot());
        initView();
    }

    private void initView() {
        helloBinding.tvSize14D.setTextSize(14f);
        helloBinding.tvSize20D.setTextSize(20);
        helloBinding.tvColorRedD.setTextColor(getColor(R.color.lavender));
        helloBinding.tvColorBlueD.setTextColor(getColor(R.color.log_main));
    }
}