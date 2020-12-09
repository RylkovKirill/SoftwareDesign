package com.example.timer.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.timer.App;
import com.example.timer.R;
import com.example.timer.Entities.Action;

import java.util.List;

public class AlertDialogAdapter extends ArrayAdapter<String>
{
    private List<String> actions;

    public AlertDialogAdapter(Context context, int resource, List<String> actions)
    {
        super(context, resource, actions);
        this.actions = actions;
    }
}
