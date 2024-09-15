package com.swust.mentalarithmetic.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.swust.mentalarithmetic.R;
import com.swust.mentalarithmetic.adapter.SwichTypeAdapter;
import com.swust.mentalarithmetic.databinding.ActivitySwitchBinding;
import com.swust.mentalarithmetic.impl.EnterClick;
import com.swust.mentalarithmetic.utils.CalculatorUtil;
import com.swust.mentalarithmetic.utils.MyRecycleDecoration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SwitchActivity extends AppCompatActivity {
    private List<String> grade1;
    private List<String> grade2;
    private List<String> grade3;
    private List<String> grade4;
    private List<String> grade5;
    private List<String> grade6;
    public  static HashMap<Integer, CalculatorUtil.Model> modelHashMap;
   private int level;
   public final static int DEFAULT_SUM = 10;
   private SwichTypeAdapter swichTypeAdapter;
   private ActivitySwitchBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySwitchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setModelMap();
        initRecycleData();
        initRecycleView();
    }




    private void initRecycleView() {
        this.level = getIntent().getIntExtra("level",1);
        switch (level){
            case 1:{
                swichTypeAdapter = new SwichTypeAdapter(SwitchActivity.this,
                        grade1, level,new MyClick());
                break;
            }
            case 2:{
                swichTypeAdapter = new SwichTypeAdapter(SwitchActivity.this,
                        grade2, level,new MyClick());
                break;
            }
            case 3:{
                swichTypeAdapter = new SwichTypeAdapter(SwitchActivity.this,
                        grade3, level,new MyClick());
                break;
            }
            case 4:{
                swichTypeAdapter = new SwichTypeAdapter(SwitchActivity.this,
                        grade4, level,new MyClick());
                break;
            }
            case 5:{
                swichTypeAdapter = new SwichTypeAdapter(SwitchActivity.this,
                        grade5, level,new MyClick());
                break;
            }
            case 6:{
                swichTypeAdapter = new SwichTypeAdapter(SwitchActivity.this,
                        grade6, level,new MyClick());
                break;
            }
            default:
                break;
        }
        binding.switchRecycleview.setAdapter(swichTypeAdapter);
        binding.switchRecycleview.setLayoutManager(new LinearLayoutManager(
                SwitchActivity.this,LinearLayoutManager.VERTICAL
                ,false));
        binding.switchRecycleview.addItemDecoration(new MyRecycleDecoration(16,0,
                0,0));
    }

    class MyClick implements EnterClick{
        @Override
        public void onItemClick(int position, int sum) {
            Intent intent = new Intent(SwitchActivity.this, ExerciseActivity.class);
            intent.putExtra("level",level);
            intent.putExtra("option",sum);
            intent.putExtra("position",position);
            startActivity(intent);
        }
    }

    private void setModelMap() {
        modelHashMap = new HashMap<>();
        modelHashMap.put(0, CalculatorUtil.Model.Add);
        modelHashMap.put(1, CalculatorUtil.Model.Sub);
        modelHashMap.put(2, CalculatorUtil.Model.AddSub);
        modelHashMap.put(3, CalculatorUtil.Model.Multi);
        modelHashMap.put(4, CalculatorUtil.Model.Division);
    }
    private void initRecycleData() {
         grade1 = Arrays.asList(getResources().getStringArray(R.array.grade1));
         grade2 = Arrays.asList(getResources().getStringArray(R.array.grade2));
         grade3 = Arrays.asList(getResources().getStringArray(R.array.grade3));
         grade4 = Arrays.asList(getResources().getStringArray(R.array.grade4));
         grade5 = Arrays.asList(getResources().getStringArray(R.array.grade5));
         grade6 = Arrays.asList(getResources().getStringArray(R.array.grade6));
    }
}