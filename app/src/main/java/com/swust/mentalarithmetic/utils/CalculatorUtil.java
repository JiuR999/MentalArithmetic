package com.swust.mentalarithmetic.utils;

import android.util.Log;

import com.swust.mentalarithmetic.entity.Expression;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

/**
 * @author Zhangxu
 */
public class CalculatorUtil {
    private final String TAG = "Calculator";
    private final int ADD = 0;
    private final int SUB = 1;
    private final int MULTY = 2;
    private final int DEVIDE = 3;
    private List<Expression<Number>> expresionList;
    /**
     * 要生成的算式数量
     */
    private  int calculatorSum;
    /**
     *  生成算式的运算数范围
     */
    private int maxNum;
    public enum Model {
        /**
         * 代表支持几种运算
         * Add----加法
         * Sub----减法
         * Multi----乘法
         * Division----除法
         * AddSub----加减法
         * AddSubMul----加减乘法
         * All----加减乘除法
         */
         Add, AddSub, AddSubMul, All, Division, Multi, Sub}
    private final Model model;

    public CalculatorUtil(int calculatorSum, int maxNum, Model m) {
        expresionList = new ArrayList<>();
        this.calculatorSum = calculatorSum;
        this.maxNum = maxNum;
        this.model = m;
    }

    //生成随机表达式
    public void generateExpression() {
        while (this.removeSame()) {
            if (this.model == Model.Add) {
                generateAddExpression();
            }else if (this.model == Model.Sub) {
                generateSubtraction();
            }else if (this.model == Model.Multi) {
                generateMulty();
            }else if (this.model == Model.Division) {
                generateDivision();
            } else if (this.model == Model.AddSub) {
                generateSpecial(new Random().nextInt(2));
            } else if (this.model == Model.AddSubMul) {
                generateSpecial(new Random().nextInt(3));
            } else {
                generateSpecial(new Random().nextInt(4));
            }
        }
    }

    /**
     * 生成随机算式
     *
     * @param ope 随机算式种子
     */
    public void generateSpecial(int ope) {
        generateExpression(ope);
    }

    public void generateExpression(int ope) {
        if (ope == ADD) {
            generateAddExpression();
        } else if (ope == SUB) {
            generateSubtraction();
        } else if (ope == MULTY) {
            generateMulty();
        } else if (ope == DEVIDE) {
            generateDivision();
        }
    }


    public void generateSubtraction() {
        int left = new Random().nextInt(this.maxNum-1)+1;
        int right = new Random().nextInt(left)+1;
        Expression<Number> exp2 = new Expression<>(left, right, '-');
        exp2.setResult(left-right);
        this.expresionList.add(exp2);
    }

    public void generateAddExpression() {
        Log.d(TAG,this.maxNum+"");
        int left = new Random().nextInt(this.maxNum-1)+1;
        int right = new Random().nextInt(this.maxNum-left)+1;;
        Expression<Number> exp1 = new Expression<>(left, right, '+');
        exp1.setResult(left+right);
        this.expresionList.add(exp1);
    }

    public void generateMulty() {
        int left = new Random().nextInt(this.maxNum-1)+1;
        int right =new Random().nextInt(this.maxNum / left)+1;
        Expression<Number> exp3 = new Expression<>(left, right, '*');
        exp3.setResult(left*right);
        this.expresionList.add(exp3);
    }

    public void generateDivision() {
        int left = new Random().nextInt(this.maxNum-1);
        //暂时整数
        int right = findIntFactors(left);
        Expression<Number> exp4 = new Expression<>(left, right, '/');
        exp4.setResult(left/right);
        this.expresionList.add(exp4);
    }

    private int findIntFactors(int left) {
        List<Integer> factors = new ArrayList<>();
        for (int i = 1; i <= left ; i++) {
            if(left % 7 == 0){
                factors.add(i);
            }
        }
        int rand = new Random().nextInt(factors.size());
        return factors.get(rand);
    }

    public void DispExp() {
        for (int i = 0; i < this.expresionList.size(); i++) {
            this.expresionList.get(i).printExpression(i + 1);
            if ((i + 1) % 10 == 0) {
                System.out.println();
            }
        }
    }

    /**
     * 给表达式去重
     *
     * @return 时候生成足够表达式
     */
    public boolean removeSame() {
        LinkedHashSet<Expression<Number>> expresionLinkedHashSet =
                new LinkedHashSet<>(expresionList);
        expresionList = new ArrayList<>(expresionLinkedHashSet);
        return expresionLinkedHashSet.size() < this.calculatorSum;
    }

    public List<Expression<Number>> getExpresionList() {
        return expresionList;
    }
}
