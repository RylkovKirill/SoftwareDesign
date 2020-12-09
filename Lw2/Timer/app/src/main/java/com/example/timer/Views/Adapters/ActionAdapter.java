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

public class ActionAdapter  extends ArrayAdapter<Action>
{

    private LayoutInflater inflater;
    private int layout;
    private List<Action> actions;


    public ActionAdapter(Context context, int resource, List<Action> actions)
    {
        super(context, resource, actions);
        this.actions = actions;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }


    public View getView(int position, View convertView, ViewGroup parent)
    {

        View view=inflater.inflate(this.layout, parent, false);

        EditText actionName = (EditText) view.findViewById(R.id.nameEditText);
        View edit = view.findViewById(R.id.editAction);
        View plus = (View)view.findViewById(R.id.plusButton);
        View minus = (View)view.findViewById(R.id.minusButton);
        TextView seconds = (TextView)view.findViewById(R.id.secondsTextView);

        Action action = actions.get(position);

        actionName.setText(action.name);
        seconds.setText(Integer.toString(action.secondsNumber));

        plus.setOnClickListener(view1 -> {
            action.secondsNumber++;
            App.getInstance().getActionDao().Update(action);
        });

        minus.setOnClickListener(view2 -> {
            action.secondsNumber--;
            App.getInstance().getActionDao().Update(action);
        });

        edit.setOnClickListener(view3 -> {
            action.name = actionName.getText().toString();
            App.getInstance().getActionDao().Update(action);
        });
        return view;
    }

}
