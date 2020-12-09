package com.example.timer.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.timer.Enums.TimerStatus;

public class TimerViewModel extends ViewModel
{
    private MutableLiveData<Integer> position = new MutableLiveData<Integer>();
    private MutableLiveData<Integer> time = new MutableLiveData<Integer>();
    private TimerStatus actionStatus = TimerStatus.WAITING;

    public LiveData<Integer> getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position.setValue(position);
    }

    public LiveData<Integer> getTime()
    {
        return time;
    }

    public void setTime(int time)
    {
        this.time.setValue(time);
    }

    public void setTimerStatus(TimerStatus status) {
        this.actionStatus = status;
    }

    public TimerStatus getActionStatus()
    {
        return actionStatus;
    }

    public boolean IsActionStart() 
    {
        if(actionStatus == TimerStatus.STARTED)
        {
            return true;
        }
        else 
        {
            return false;
        }
    }


}
