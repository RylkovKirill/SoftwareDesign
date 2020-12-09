package com.example.timer.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.timer.App;
import com.example.timer.Entities.Timer;

import java.util.List;

public class TimerListViewModel extends ViewModel
{
    private LiveData<List<Timer>> timers = App.getInstance().getTimerDao().GetAllAsLiveDatList();

    public LiveData<List<Timer>> getTimers()
    {
        return timers;
    }

    public void setTimers(LiveData<List<Timer>> timers)
    {
        this.timers = timers;
    }
}
