package com.swust.mentalarithmetic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swust.mentalarithmetic.R;
import com.swust.mentalarithmetic.entity.Expression;
import com.swust.mentalarithmetic.entity.MisExercise;

import java.util.List;

/**
 * @author Zhangxu
 */
public class ExerAdapter extends RecyclerView.Adapter<ExerAdapter.ViewHolder> {
    private Context context;
    private List<MisExercise> misExerciseList;
    private List<Expression<Number>> expressions;
    private final int ITEM_SUM = 5;
    public ExerAdapter(Context context, List< Expression<Number>> expressions,List<MisExercise> expressions2) {
        this.context = context;
        if(expressions!=null)this.expressions =  expressions;
        if(expressions2!=null)this.misExerciseList = expressions2;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.exercise_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
        int margin = (int)context.getResources().getDimension(R.dimen.activity_horizontal_margin);
        int density = (int) context.getResources().getDisplayMetrics().density;
        layoutParams.height = (parent.getHeight()-ITEM_SUM*margin*density)/ITEM_SUM;
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExerAdapter.ViewHolder holder, int position) {
           if(expressions!=null){
               holder.tv.setText(position+1+"、"+expressions.get(position).getExpression());
               holder.ed.setText(String.valueOf(expressions.get(position).getAnswer()));
           } else {
               holder.tv.setText(position+1+"、"+misExerciseList.get(position).getExpression());
               holder.ed.setText(String.valueOf(misExerciseList.get(position).getAnswer()));
           }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (expressions != null) {
            return expressions.size();
        } else {
            return misExerciseList.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        EditText ed;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_expression);
            ed = itemView.findViewById(R.id.edit_result);
        }
    }
}
