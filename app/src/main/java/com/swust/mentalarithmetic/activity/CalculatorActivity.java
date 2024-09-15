package com.swust.mentalarithmetic.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.swust.mentalarithmetic.databinding.ActivityCalculatorBinding;

public class CalculatorActivity extends AppCompatActivity {
    ActivityCalculatorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalculatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        registerClick();
    }

    private void registerClick() {
    }
}