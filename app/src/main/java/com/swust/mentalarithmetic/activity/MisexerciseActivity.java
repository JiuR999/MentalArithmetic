package com.swust.mentalarithmetic.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.swust.mentalarithmetic.adapter.ExerAdapter;
import com.swust.mentalarithmetic.database.RoomDataBase;
import com.swust.mentalarithmetic.database.dao.MisExerciseDao;
import com.swust.mentalarithmetic.databinding.ActivityMisexerciseBinding;
import com.swust.mentalarithmetic.entity.MisExercise;

import java.util.List;
import java.util.Objects;

public class MisexerciseActivity extends AppCompatActivity {
    private ActivityMisexerciseBinding binding;
    private ExerAdapter exerAdapter;
    private Handler handler;
    private List<MisExercise> misExerciseList;
    private RoomDataBase roomDataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMisexerciseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setHandler();
        initView();
    }

    private void setHandler() {
        handler =  new Handler(Objects.requireNonNull(Looper.myLooper())){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    exerAdapter = new ExerAdapter(MisexerciseActivity.this,null,misExerciseList);
                    binding.misRecyclerviewMistake.setLayoutManager(new LinearLayoutManager(MisexerciseActivity.this,LinearLayoutManager.VERTICAL,false));
                    binding.misRecyclerviewMistake.setAdapter(exerAdapter);
                }
            }
        };
    }

    private void initView() {
    roomDataBase = RoomDataBase.getInstance(MisexerciseActivity.this);
        MisExerciseDao misExerciseDao = roomDataBase.misExerciseDao();
        new Thread(()->{
            misExerciseList = misExerciseDao.selectAllMisExercise();
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }).start();
    }
}