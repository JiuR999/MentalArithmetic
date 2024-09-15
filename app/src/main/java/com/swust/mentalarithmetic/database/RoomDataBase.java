package com.swust.mentalarithmetic.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.swust.mentalarithmetic.database.dao.MisExerciseDao;
import com.swust.mentalarithmetic.database.dao.UserDao;
import com.swust.mentalarithmetic.entity.MisExercise;
import com.swust.mentalarithmetic.entity.User;
import com.swust.mentalarithmetic.utils.Converters;

@Database(entities = {User.class, MisExercise.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class RoomDataBase extends RoomDatabase {
    public static RoomDataBase roomUserHelper;
    public abstract UserDao userDao();
    //public abstract ExpressionDao expressionDao();
    public abstract MisExerciseDao misExerciseDao();
    public static RoomDataBase getInstance(Context context){
           if(roomUserHelper==null){
               synchronized (RoomDataBase.class){
               roomUserHelper = Room.databaseBuilder(context.getApplicationContext(),
                       RoomDataBase.class,"Math").build();
               }
           }
           return roomUserHelper;
    }
}
