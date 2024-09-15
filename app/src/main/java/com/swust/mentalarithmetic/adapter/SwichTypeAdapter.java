package com.swust.mentalarithmetic.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.swust.mentalarithmetic.R;
import com.swust.mentalarithmetic.activity.SwitchActivity;
import com.swust.mentalarithmetic.impl.EnterClick;

import java.util.Arrays;
import java.util.List;

public class SwichTypeAdapter extends RecyclerView.Adapter<SwichTypeAdapter.ViewHolder> {
   private Context context;
   private List<String> strings;
   private int grade;
   private EnterClick enterClick;
   private int sum;
    public SwichTypeAdapter(Context context, List<String> strings
            , int grade, EnterClick enterClick) {
        this.context = context;
        this.strings = strings;
        this.grade = grade;
        this.enterClick = enterClick;
        this.sum = 10;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.exercise_type_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
           holder.tv.setText(strings.get(position));
           if(grade!=1){
               holder.itemView.findViewById(R.id.btn_type_enter).setOnClickListener(view -> enterClick.onItemClick(position,
                       Integer.parseInt(holder.spinner.getText().toString())));
           }else {
               holder.itemView.findViewById(R.id.btn_type_enter).setOnClickListener(view -> enterClick.onItemClick(position,
                       SwitchActivity.DEFAULT_SUM/2));
           }
        holder.spinner.setOnItemSelectedListener(((materialSpinner, i, l, o) -> {
            this.sum = Integer.parseInt(holder.spinner.getText().toString());
        }));
        Log.d("SwitchTypeAdapter-题目数量",sum+"");
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
        notifyDataSetChanged();
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public EnterClick getEnterClick() {
        return enterClick;
    }

    public void setEnterClick(EnterClick enterClick) {
        this.enterClick = enterClick;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        MaterialSpinner spinner;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_title);
            spinner = itemView.findViewById(R.id.spinner_sum);
            if (grade ==1){
                spinner.setVisibility(View.GONE);
                itemView.findViewById(R.id.tv_type_item_sum).setVisibility(View.VISIBLE);
            }else {
                spinner = itemView.findViewById(R.id.spinner_sum);
                spinner.setItems(Arrays.asList("10","20","30","40","50","60"));
            }
        }


    }
}
