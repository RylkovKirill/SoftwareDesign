package com.example.timer.Views.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.timer.Entities.Timer;
import com.example.timer.R;

import java.util.List;

public class TimerListAdapter extends ArrayAdapter<Timer>
{
    private LayoutInflater inflater;
    private int layout;
    private List<Timer> timers;
    private Context context;

    public TimerListAdapter(Context context, int resource, List<Timer> timers)
    {
        super(context, resource, timers);
        this.context = context;
        this.timers = timers;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Timer timer = timers.get(position);

        View view=inflater.inflate(this.layout, parent, false);
        view.setBackgroundResource(R.drawable.timer_list_item_style);

        TextView timerName = (TextView)view.findViewById(R.id.timerName);
        TextView timerDescription = (TextView)view.findViewById(R.id.timerDescription);
        TextView timerCreatedDate = (TextView)view.findViewById(R.id.timerCreatedDate);

        timerName.setText(timer.name);
        timerDescription.setText(timer.description);
        timerCreatedDate.setText(timer.createdDate);

        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.timerListViewItem);
        linearLayout.setBackgroundTintList(ColorStateList.valueOf(timer.color));

        return view;
    }
}
