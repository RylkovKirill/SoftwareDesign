package com.example.timer.Entities;

import android.graphics.Color;


import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

import java.util.Date;
import java.util.Random;

@Entity(tableName = "timers")
public class Timer
{
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name = "Timer Name";

    @ColumnInfo(name = "description")
    public String description = "Timer Description";

    @ColumnInfo(name = "createdDate")
    public String createdDate = new Date().toString();

    @ColumnInfo(name = "color")
    public int color = Color.rgb(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));

    public Timer()
    {
    }

    @Ignore
    public Timer(String name, String description, String createdDate, int color)
    {
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.color = color;
    }

    @Ignore
    public Timer(Timer timer)
    {
        this.name = timer.name;
        this.description = timer.description;
        this.color = timer.color;
    }
}

