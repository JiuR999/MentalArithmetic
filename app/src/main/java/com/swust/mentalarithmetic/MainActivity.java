package com.swust.mentalarithmetic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.swust.mentalarithmetic.activity.MisexerciseActivity;
import com.swust.mentalarithmetic.activity.SwitchActivity;
import com.swust.mentalarithmetic.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MaterialSpinner spinner;
    private int level;
    private String s;
    private ActivityMainBinding activityMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        initView();
        //setBoN();
    }

    private void initView() {
        List<String> spinnerItem = Arrays.asList("一年级","二年级","三年级","四年级","五年级","六年级");
        spinner = findViewById(R.id.spinner_level);
        spinner.setItems(spinnerItem);
        activityMainBinding.relMisList.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, MisexerciseActivity.class));
        });
        activityMainBinding.enter.setOnClickListener(view->{
            Intent intent = new Intent(MainActivity.this, SwitchActivity.class);
            intent.putExtra("level",spinner.getSelectedIndex()+1);
            Log.d("MainActivity-年级",spinner.getSelectedIndex()+1+"");
            startActivity(intent);

        });
    }
    
/*    private void setBoN(){

        bot = findViewById(R.id.bottom_navi);
        bot.setMode(BottomNavigationBar.MODE_SHIFTING);
        bot.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bot.setBarBackgroundColor(R.color.bottom);
        bot.addItem(new BottomNavigationItem(R.drawable.home,"主页")
                        .setActiveColor(R.color.lavender).setInActiveColor(R.color.bottom))
                .addItem(new BottomNavigationItem(R.drawable.home,"主页2")
                        .setActiveColor(R.color.lavender).setInActiveColor(R.color.bottom)).initialise();
    }*/

}