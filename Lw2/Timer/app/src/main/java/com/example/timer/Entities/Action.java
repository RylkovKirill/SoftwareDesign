package com.example.timer.Entities;

import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

import androidx.room.Ignore;
import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "actions", foreignKeys = @ForeignKey(entity = Timer.class, parentColumns = "id", childColumns = "timerId", onDelete = CASCADE))
public class Action
{
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name = "Action Name";

    @ColumnInfo(name = "secondsNumber")
    public int secondsNumber = 10;

    @ColumnInfo(name = "timerId")
    public int timerId;

    public Action()
    {

    }

    @Ignore
    public Action(String name, int secondsNumber, int timerId)
    {
        this.name = name;
        this.secondsNumber = secondsNumber;
        this.timerId = timerId;
    }

    @Ignore
    public Action(Action action)
    {
        this.name = action.name;
        this.secondsNumber = action.secondsNumber;
        this.timerId = action.timerId;
    }
}
