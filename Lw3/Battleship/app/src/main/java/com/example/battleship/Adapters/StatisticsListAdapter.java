package com.example.battleship.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.battleship.Models.Game;
import com.example.battleship.R;

import java.util.List;

public class StatisticsListAdapter extends ArrayAdapter<Game>
{
    private LayoutInflater inflater;
    private int resource;
    private List<Game> statistics;

    public StatisticsListAdapter(Context context, int resource, List<Game> statistics)
    {
        super(context, resource, statistics);
        this.statistics = statistics;
        this.resource = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @SuppressLint({"ViewHolder", "SimpleDateFormat", "ResourceAsColor"})
    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent)
    {
        view = inflater.inflate(resource, parent, false);

        TextView  firstUser = view.findViewById(R.id.firstUser);
        TextView  firstUserScore = view.findViewById(R.id.firstUserScore);
        TextView  secondUser = view.findViewById(R.id.secondUser);
        TextView  secondUserScore = view.findViewById(R.id.secondUserScore);
        TextView  gameDate = view.findViewById(R.id.gameDate);

        Game statisticItem = this.statistics.get(position);

        if(statisticItem.getFirstUserResult() > statisticItem.getSecondUserResult())
        {
            secondUser.setTextColor(R.color.colorGray);
            secondUserScore.setTextColor(R.color.colorGray);
        }
        else
        {
            firstUser.setTextColor(R.color.colorGray);
            firstUserScore.setTextColor(R.color.colorGray);
        }

        firstUser.setText(statisticItem.getFirstUser());
        firstUserScore.setText(String.valueOf(statisticItem.getFirstUserResult()));
        secondUser.setText(statisticItem.getSecondUser());
        secondUserScore.setText(String.valueOf(statisticItem.getSecondUserResult()));
        gameDate.setText(new SimpleDateFormat("dd-M-yyyy hh:mm").format(statisticItem.getGameDate()));
        return view;
    }
}
