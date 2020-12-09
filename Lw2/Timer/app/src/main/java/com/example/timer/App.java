package com.example.timer;

import android.app.Application;

import androidx.room.Room;

import com.example.timer.Dao.ActionDao;
import com.example.timer.Dao.TimerDao;
import com.example.timer.DbContext.DbContext;

public class App extends Application {
    private DbContext database;
    private TimerDao timerDao;
    private ActionDao actionDao;

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;

        database = Room.databaseBuilder(getApplicationContext(), DbContext.class, "timerDb").allowMainThreadQueries().build();
        timerDao = database.timerDao();
        actionDao = database.actionDao();
    }

    public DbContext getDatabase() {
        return database;
    }

    public void setDatabase(DbContext database) {
        this.database = database;
    }

    public TimerDao getTimerDao() {
        return timerDao;
    }

    public void setTimerDao(TimerDao timerDao) {
        this.timerDao = timerDao;
    }

    public ActionDao getActionDao() {
        return actionDao;
    }

    public void setActionDao(ActionDao actionDao) {
        this.actionDao = actionDao;
    }
}
