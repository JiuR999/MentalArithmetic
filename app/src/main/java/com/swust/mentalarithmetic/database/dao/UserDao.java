package com.swust.mentalarithmetic.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.swust.mentalarithmetic.entity.User;

@Dao
public interface UserDao{
    @Query("select * from User where account=(:account)")
    User selectByAccount(String account);
    @Update
    void resetPassword(User n_user);
    @Insert
    void addUse(User user);
}
