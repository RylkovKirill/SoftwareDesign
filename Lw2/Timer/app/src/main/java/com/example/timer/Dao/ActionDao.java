package com.example.timer.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.OnConflictStrategy;

import com.example.timer.Entities.Action;

import java.util.List;

@Dao
public interface ActionDao
{
    @Query("SELECT * FROM ACTIONS WHERE id LIKE :id LIMIT 1")
    Action Get(int id);

    @Query("SELECT * FROM ACTIONS")
    List<Action> GetAll();

    @Query("SELECT * FROM ACTIONS")
    LiveData<List<Action>> GetAllAsLiveDatList();

    @Query("SELECT * FROM ACTIONS WHERE timerId = :timerId")
    List<Action> GetAllByTimerId(int timerId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void Insert(Action action);

    @Update
    void Update(Action action);

    @Delete
    void Delete(Action action);

    @Query("DELETE  FROM ACTIONS WHERE timerId = :timerId")
    void DeleteAllByTimerId(int timerId);
}
