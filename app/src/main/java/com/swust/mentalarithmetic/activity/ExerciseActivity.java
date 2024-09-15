package com.swust.mentalarithmetic.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.swust.mentalarithmetic.R;
import com.swust.mentalarithmetic.adapter.ExerAdapter;
import com.swust.mentalarithmetic.database.RoomDataBase;
import com.swust.mentalarithmetic.database.dao.MisExerciseDao;
import com.swust.mentalarithmetic.databinding.ActivityExerciseBinding;
import com.swust.mentalarithmetic.entity.Expression;
import com.swust.mentalarithmetic.entity.MisExercise;
import com.swust.mentalarithmetic.utils.AnimationFactory;
import com.swust.mentalarithmetic.utils.CalculatorUtil;
import com.swust.mentalarithmetic.utils.FileUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author JIUR
 */
public class ExerciseActivity extends AppCompatActivity implements View.OnClickListener {
    private final int PERMISSION_REQUEST_CODE = 1;
    private final String TAG = "ExerciseActivity";
    private final int MSG_START = 1;
    private ActivityExerciseBinding binding;
    private ExerAdapter exerAdapter;
    private List<Expression<Number>> expressions = new ArrayList<>();
    /**
     * 错题列表
     */
    private  List<Expression<Number>> misExpressions = new ArrayList<>();
    private Handler mHandler;
    //生成算式总数和最大范围
    private int calculatorSum,calculatorMax;
    //当前在第几题
    private int curSum = 0,level,position,time;
    private CalculatorUtil.Model model;
    private ThreadPoolExecutor threadPoolExecutor;
    private BottomSheetDialog bottomSheetDialog;
    private boolean isInteger = true;
    private BottomSheetBehavior bottomSheetBehavior;
    private AnimationFactory animationFactory;
    private StringBuilder answerBuilder;
    private MyTimer myTimer;
    private int MSG_RESET = 2;
    private final int TIME_STD = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExerciseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initLimit();
        initView();
        initKeyBottomSheet();
        threadPoolExecutor.execute(()->{
            initData(ExerciseActivity.this.calculatorSum,ExerciseActivity.this.calculatorMax
                    ,ExerciseActivity.this.model);
            Message msg = new Message();
            msg.what = MSG_START;
            mHandler.sendMessage(msg);
        });
    }


    private void initLimit() {
         level    = getIntent().getIntExtra("level",1);
         position = getIntent().getIntExtra("position",0);
         model = SwitchActivity.modelHashMap.get(position);
         time = level*(position+1)*TIME_STD;
         myTimer = new MyTimer(time* 1000L,1000);
         myTimer.start();
         if(level>4) {
             isInteger = false;
         }
        setProperty(model);
        Log.d("ExerciseActivity年级",level+"");
        Log.d("ExerciseActivity模式",model.name());
    }

    private void setProperty(CalculatorUtil.Model model) {
        if (level == 1) {
            this.calculatorSum = SwitchActivity.DEFAULT_SUM / 2;
        } else {
            this.calculatorSum = getIntent().getIntExtra("option", SwitchActivity.DEFAULT_SUM);
        }
        if ((model == CalculatorUtil.Model.Add || model == CalculatorUtil.Model.Sub ||
                model == CalculatorUtil.Model.AddSub)) {
            if (level <= 2) {
                this.calculatorMax = level * 10;
            } else if (level == 3) {
                this.calculatorMax = 1000;
            } else if (level == 4) {
                this.calculatorMax = 10000;
            }
        } else if (model == CalculatorUtil.Model.Multi) {
            if (level == 2) {
                this.calculatorMax = (int) Math.pow(10, level - 1);
            }
        }
    }
    public void initView() {
        answerBuilder = new StringBuilder();
        animationFactory = new AnimationFactory(ExerciseActivity.this);
        findViewById(R.id.key_delete).setOnClickListener(this);
        threadPoolExecutor = new ThreadPoolExecutor(2,3,60L,
                TimeUnit.SECONDS,new ArrayBlockingQueue<>(8));
        mHandler = new Handler(Objects.requireNonNull(Looper.myLooper())){
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==MSG_START){
                    setCurProgress();
                    setExerText(curSum);
                    Log.d("ExerciseActivity生成算式数量",expressions.size()+""+"/"+calculatorSum);

                } else if (msg.what == MSG_RESET) {
                    initLimit();
                    threadPoolExecutor.execute(()->{
                        initData(ExerciseActivity.this.calculatorSum,ExerciseActivity.this.calculatorMax
                                ,ExerciseActivity.this.model);
                        Message msg1 = new Message();
                        msg1.what = MSG_START;
                        mHandler.sendMessage(msg1);
                    });
                }
            }
        };
        binding.exerEditResult.setShowSoftInputOnFocus(false);
        binding.btnPrint.setOnClickListener(view -> {
            Toast.makeText(ExerciseActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            checkPermission();
        });
        binding.btnExerSubmit.setOnClickListener(this);
        binding.chipNext.setOnClickListener(this);
        binding.chipPreQuestion.setOnClickListener(this);
        binding.btnExerReset.setOnClickListener(this);
        binding.exerEditResult.setOnClickListener(view -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

    }

    @SuppressLint("SetTextI18n")
    private void setCurProgress() {
        binding.exerTvCurSum.setText(this.curSum+1+" / "+this.calculatorSum);
    }

    private void initData(int calculatorSum, int maxNum, CalculatorUtil.Model model) {
        CalculatorUtil calculatorUtil = new CalculatorUtil(calculatorSum
                ,maxNum, model);
        calculatorUtil.generateExpression();
        expressions = calculatorUtil.getExpresionList();
    }

    private void initRecycleView() {
        exerAdapter= new ExerAdapter(ExerciseActivity.this,misExpressions,null);
        binding.recyclerViewMistake.setAdapter(exerAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewMistake.setLayoutManager(layoutManager);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.chip_next){
            String label = ((Chip)view).getText().toString();
            binding.chipPreQuestion.setVisibility(View.VISIBLE);
            if(label.equals(getResources().getString(R.string.nextQuestion))){
                if(curSum<this.calculatorSum-1){
                    saveAnswer();
                    setExerText(++this.curSum);
                    if(this.curSum == this.calculatorSum-1){
                        binding.chipNext.startAnimation(animationFactory.slideOut());
                        binding.chipNext.startAnimation(animationFactory.slideIn());
                        ((Chip)view).setText(getResources().getString(R.string.submit));
                    }
                    setEditAnswer();
                    setCurProgress();
                }
            }else if(label.equals(getResources().getString(R.string.submit))){
                subMit();
                ((Chip)view).setText(getResources().getString(R.string.reset));
            }else {
                reSet();
            }
            
        } else if (id ==R.id.chip_pre_question) {
            if(this.curSum>0){
                saveAnswer();
                setExerText(--this.curSum);
                if(this.curSum == 0) {
                    binding.chipPreQuestion.setVisibility(View.GONE);
                }
                setEditAnswer();
                setCurProgress();
            }
        } else if (id == R.id.btn_exer_submit) {
            subMit();
        } else if (id == R.id.btn_exer_reset) {
            reSet();
        } else if (id == R.id.key_delete) {
           if(answerBuilder.length()>=1){
               answerBuilder.setLength(answerBuilder.length()-1);
               binding.exerEditResult.setText(answerBuilder.toString());
               binding.exerEditResult.setSelection(answerBuilder.length());
           }
        }
    }

    private void subMit() {
        myTimer.cancel();
        binding.exerTvTime.setText("");
        answerBuilder.setLength(0);
        saveAnswer();
        calculateScores();
        binding.btnExerSubmit.startAnimation(animationFactory.slideOut());
        binding.btnExerSubmit.setVisibility(View.GONE);
        binding.btnExerReset.startAnimation(animationFactory.slideIn());
        binding.btnExerReset.setVisibility(View.VISIBLE);
        if(misExpressions.size()!=0){
            if(binding.recyclerViewMistake.getAdapter()!=null) {
                binding.recyclerViewMistake.getAdapter().notifyDataSetChanged();
            } else {
                initRecycleView();
            }
            binding.recyclerViewMistake.setVisibility(View.VISIBLE);
        }else {
            Glide.with(ExerciseActivity.this)
                            .load(R.drawable.all_right)
                                    .into(binding.exerImgAllright);
            binding.exerImgAllright.setVisibility(View.VISIBLE);
        }
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void reSet(){
        this.curSum = 0;
        this.calculatorSum =misExpressions.size();
        List<View> views = new ArrayList<>();
        binding.exerEditResult.setText("");
        binding.exerTvTime.setTextColor(ContextCompat.getColor(ExerciseActivity.this,R.color.md_theme_light_primary));
       // binding.chipPreQuestion.setVisibility(View.GONE);
        views.add(binding.chipPreQuestion);
        binding.chipNext.setText(getResources().getString(R.string.nextQuestion));
        //添加动画
        binding.linearScore.startAnimation(animationFactory.slideOut());
        binding.linearScore.setVisibility(View.GONE);

        views.add(binding.exerImgAllright);
        //binding.recyclerViewMistake.setVisibility(View.GONE);
        //binding.exerImgAllright.setVisibility(View.GONE);
        myTimer.start();
        binding.btnExerReset.startAnimation(animationFactory.slideOut());
        binding.btnExerReset.setVisibility(View.GONE);
        binding.btnExerSubmit.startAnimation(animationFactory.slideIn());
        binding.btnExerSubmit.setVisibility(View.VISIBLE);
        binding.relayExam.setVisibility(View.VISIBLE);
        expressions.clear();
        expressions.addAll(misExpressions);
        this.misExpressions.clear();
        Message message = new Message();
        if(expressions.size()==0){
            message.what = MSG_RESET;
        }else {
            message.what = MSG_START;
        }
        mHandler.sendMessage(message);
    }

    private void setViewVisible(List<View> view) {
        for(View view1 : view){
            if(view1.getVisibility() == View.VISIBLE){
                view1.startAnimation(animationFactory.slideOut());
                view1.setVisibility(View.GONE);
            }else {
                view1.startAnimation(animationFactory.slideIn());
                view1.setVisibility(View.VISIBLE);
            }
        }
    }

    private void saveAnswer() {
        String answer = binding.exerEditResult.getText().toString();
        //判空以及数值类型判断
        if(!"".equals(answer)){
            if(isInteger){
                expressions.get(this.curSum).setAnswer(Integer.parseInt(answer));
            }else {
                expressions.get(this.curSum).setAnswer(Double.valueOf(answer));
            }
            expressions.get(this.curSum).setAnswered();
            Log.d(TAG+"答案", expressions.get(this.curSum).getAnswer().toString());
        }
    }

    private void calculateScores() {
        int correct = 0;
        for(Expression<Number> expression : expressions){
            expression.setAnswered();
            if(expression.getAnswer()!=null){
                Double answer = Double.valueOf(String.valueOf(expression.getAnswer()));
                Double result = Double.valueOf(String.valueOf(expression.getResult()));
                if (result.equals(answer)){
                    correct++;
                }else {
                    addMisLists(expression, answer);
                }
            }else {
                addMisLists(expression, 0.0);
            }

        }

        binding.exerTvCorrect.setText(String.valueOf(correct));
        binding.exerTvIncorrect.setText(String.valueOf(this.calculatorSum-correct));
        binding.linearScore.setVisibility(View.VISIBLE);
        binding.relayExam.startAnimation(animationFactory.slideOut());
        binding.relayExam.setVisibility(View.GONE);
        Log.d(TAG+"答题情况","正确:"+correct+"错误:"+ (this.calculatorSum - correct));
    }

    private void addMisLists(Expression<Number> expression, Double answer) {
        MisExercise misExercise = new MisExercise(expression.getLeftNum().intValue()
        , expression.getRightNum().intValue(), expression.getOperator(), answer);
        RoomDataBase roomDataBase = RoomDataBase.getInstance(ExerciseActivity.this);
        MisExerciseDao misExerciseDao = roomDataBase.misExerciseDao();
        threadPoolExecutor.execute(()->{
            misExerciseDao.addMisExercise(misExercise);
        });
        misExpressions.add(expression);
    }
    private void setEditAnswer() {
        if(expressions.get(curSum).isAnswered()){
            binding.exerEditResult.setText(expressions.get(curSum).getAnswer().toString());
        }else {
            binding.exerEditResult.setText("");
        }
        binding.exerEditResult.setSelection(binding.exerEditResult
                .getText().length());
        answerBuilder.setLength(0);
    }

    private void setExerText(int position) {
        binding.exerExp.setText(expressions.get(position).getExpression());
    }
    private void initKeyBottomSheet() {
        bottomSheetBehavior =BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_key));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_EXPANDED){
                    Log.d(TAG,"展开");
                    binding.btnExerSubmit.startAnimation(AnimationUtils
                            .loadAnimation(ExerciseActivity.this,R.anim.slide_out));
                    binding.btnPrint.startAnimation(AnimationUtils.loadAnimation(ExerciseActivity.this
                            ,R.anim.slide_out));
                    binding.btnExerSubmit.setVisibility(View.GONE);
                    binding.btnPrint.setVisibility(View.GONE);
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    Log.d(TAG,"隐藏");
                    binding.btnExerSubmit.setVisibility(View.VISIBLE);
                    binding.btnPrint.setVisibility(View.VISIBLE);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    Log.d(TAG,"隐藏");
                    binding.btnExerSubmit.setVisibility(View.VISIBLE);
                    binding.btnPrint.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }
    public void checkPermission(){
        if(Build.VERSION.SDK_INT>=30){
            if(!Environment.isExternalStorageManager()){
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        }
        // 检查外部存储器权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        } else {
            savePaper();
        }
    }

    private void savePaper() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM_dd_mm_ss", Locale.CHINA);
        String filename = "习题"+dateFormat.format(System.currentTimeMillis())+".txt";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expressions.size(); i++) {
            sb.append(i).append(expressions.get(i).getExpression()).append("\n");
        }
        // 保存文件到指定目录
        new FileUtil(ExerciseActivity.this).saveFile(filename, FileUtil.TXT,sb.toString());
    }
    public void numKeyClick(View view){
        answerBuilder.append(((TextView)view).getText().toString());
        binding.exerEditResult.setText(answerBuilder.toString());
        binding.exerEditResult.setSelection(answerBuilder.length());
        Log.d(TAG,answerBuilder.toString());
    }

    private void initBottomSheet() {
        if(bottomSheetDialog==null){
            bottomSheetDialog = new BottomSheetDialog(ExerciseActivity.this);
        }
        bottomSheetDialog.setContentView(View.inflate(this,R.layout.keyboard,null));
    }
    class MyTimer extends CountDownTimer{

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
               int minutes = (int) (l/(60*1000));
               int seconds = (int)(l%(60*1000));
               String time;
               if(l<=5*1000){
                   binding.exerTvTime.setTextColor(Color.RED);
               }
            if(minutes==0) {
                time = ((seconds/1000)+"秒");
               }else {
                   if(seconds == 0){
                       time = (minutes+"分");
                   }else {
                       time = (minutes+"分"+(seconds/1000)+"秒");
                   }
               }
            binding.exerTvTime.setText(time);
        }

        @Override
        public void onFinish() {
               subMit();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == this.PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限已授予，保存文件到指定目录
                savePaper();
            } else {
                // 权限被拒绝
                Toast.makeText(this, "导出失败，请赋予软件读写权限!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}