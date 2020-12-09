package com.example.timer.DbContext;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.timer.Entities.Action;
import com.example.timer.Entities.Timer;

import com.example.timer.Dao.ActionDao;
import com.example.timer.Dao.TimerDao;


@Database(entities = {Action.class, Timer.class}, version = 1, exportSchema = false)
public abstract class DbContext extends RoomDatabase
{
    public abstract TimerDao timerDao();
    public abstract ActionDao actionDao();
}
