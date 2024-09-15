package com.swust.mentalarithmetic.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.swust.mentalarithmetic.entity.MisExercise;

import java.util.List;

@Dao
public interface MisExerciseDao {
    @Insert
    void addMisExercise(MisExercise misExercises);
    @Query("select * from MisExercise where answer !=operator")
    List<MisExercise> selectAllMisExercise();
}
