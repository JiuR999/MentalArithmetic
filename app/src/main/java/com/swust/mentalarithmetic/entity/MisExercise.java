package com.swust.mentalarithmetic.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MisExercise extends Expression<Integer>{
    @PrimaryKey(autoGenerate = true)
    public int id;
    public MisExercise() {
        super();
    }

    public MisExercise(int leftNum, int rightNum, char ope,Number answer) {
        super(leftNum, rightNum, ope);
        this.setAnswer(answer);
    }
}
