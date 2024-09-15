package com.swust.mentalarithmetic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.swust.mentalarithmetic.R;
import com.swust.mentalarithmetic.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.loginTvRegister.setMovementMethod(LinkMovementMethod.getInstance());
        binding.loginTvRegister.setText(setClickableSpan());
        //保证密码初始时候是隐藏的
        binding.editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        binding.btnLogin.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, CalculatorActivity.class)) );
    }


    private SpannableString setClickableSpan() {
        SpannableString spannableString = new SpannableString(binding.loginTvRegister.getText().toString());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Log.d("点击注册","");
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
            //重写该方法去掉下划线
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(true);
            }
        };
        //设置文字的点击事件
        spannableString.setSpan(clickableSpan, 6, 11, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //设置文字的前景色
        int color = ContextCompat.getColor(LoginActivity.this,R.color.log_main);
        spannableString.setSpan(new ForegroundColorSpan(color), 6, 11, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}