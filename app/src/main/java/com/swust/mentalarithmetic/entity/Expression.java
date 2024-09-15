package com.swust.mentalarithmetic.entity;

import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.swust.mentalarithmetic.utils.Converters;

import java.util.Objects;

/**
 * @author JIUR
 */
@Entity
@TypeConverters({Converters.class})
public  class Expression<T extends Number> {
    @PrimaryKey(autoGenerate = true)
    int id;
    private  T leftNum;
    private T rightNum;
    private char operator;
    private String expression;
    private Number answer;
    private Number result;
    private boolean isAnswered;
    public Expression(T leftNum, T rightNum, char ope) {
        this.leftNum = leftNum;
        this.rightNum = rightNum;
        this.operator = ope;
        this.isAnswered = false;
        setExpression();
        checkOprator();
    }

    public Expression() {

    }

    public void setExpression(){
        this.expression = String.valueOf(leftNum)+operator+ rightNum +"=";
    }
    public void setResult(Number result){
        this.result = result;
    }
    public void printExpression(int order){
        setExpression();
        System.out.printf("%2d、%2d %c %2d = \t",order,(int)this.leftNum,this.operator,(int)this.rightNum);
    }

    private void checkOprator() {
             Double  l = Double.valueOf(String.valueOf(this.leftNum));
             Double  r = Double.valueOf(String.valueOf(this.rightNum));
        if(this.operator == '+'){
            setResult(l + r);
        } else if (this.operator == '-') {
            setResult(l - r);
        } else if (this.operator == '*') {
            setResult(l * r);
        }else if(this.operator == '/'){
            setResult(l / r);
        }else {
            Log.d("Expression","操作符异常");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        } else if (!(obj instanceof Expression)) {
            return false;
        }
        Expression<Number> expression1 = (Expression<Number>) obj;
        if((this.operator == '+'&&expression1.operator == '+')
                ||(this.operator == '*'&&expression1.operator == '*')){
            if((this.leftNum.equals(expression1.leftNum) && this.rightNum.equals(expression1.rightNum))
                    || this.leftNum.equals(expression1.rightNum) && this.rightNum.equals(expression1.leftNum)){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public void setAnswered() {
        isAnswered = !this.isAnswered;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public Number getAnswer() {
        return answer;
    }

    public void setAnswer(Number answer) {
        this.answer = answer;
    }

    public T getLeftNum() {
        return leftNum;
    }

    public void setLeftNum(T leftNum) {
        this.leftNum = leftNum;
    }

    public T getRightNum() {
        return rightNum;
    }

    public void setRightNum(T rightNum) {
        this.rightNum = rightNum;
    }

    public char getOperator() {
        return operator;
    }

    public void setOperator(char operator) {
        this.operator = operator;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Number getResult() {
        return result;
    }

    @Override
    public int hashCode() {
        int left = Integer.parseInt(String.valueOf(leftNum));
        int right = Integer.parseInt(String.valueOf(rightNum));
        return Objects.hash(left+right,left+right,this.operator);
    }
}
