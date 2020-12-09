package com.example.timer.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.OnConflictStrategy;

import com.example.timer.Entities.Timer;

import java.util.List;

@Dao
public interface TimerDao
{
    @Query("SELECT * FROM TIMERS WHERE id LIKE :id LIMIT 1")
    Timer Get(int id);

    @Query("SELECT * FROM TIMERS")
    List<Timer> GetAll();

    @Query("SELECT * FROM TIMERS")
    LiveData<List<Timer>> GetAllAsLiveDatList();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void Insert(Timer timer);

    @Update
    void Update(Timer timer);

    @Delete
    void Delete(Timer timer);

    @Query("DELETE  FROM TIMERS")
    void DeleteAll();
}
