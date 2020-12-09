package com.example.timer.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.timer.App;
import com.example.timer.Entities.Action;

import java.util.List;

public class ActionListViewModel extends ViewModel
{
    private LiveData<List<Action>> actions = App.getInstance().getActionDao().GetAllAsLiveDatList();

    public LiveData<List<Action>> getActions()
    {
        return actions;
    }

    public void setActions(LiveData<List<Action>> actions)
    {
        this.actions = actions;
    }
}
