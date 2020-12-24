package com.example.battleship.Views;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.battleship.Adapters.StatisticsListAdapter;
import com.example.battleship.Models.Game;
import com.example.battleship.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import com.example.battleship.Helpers.AnimationHelper;
import com.example.battleship.Helpers.FirebaseHelper;

import static android.view.View.GONE;

public class StatisticsActivity extends AppCompatActivity
{

    private ArrayList<Game> statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        AnimationHelper.StartAnimation(findViewById(R.id.container));

        statistics = new ArrayList<>();

        FirebaseHelper firebaseHelper = new FirebaseHelper();

        firebaseHelper.GetDatabaseReference("gameStatistic").addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {
                            for (Map.Entry<String, Object> entry : ((Map<String,Object>) Objects.requireNonNull(dataSnapshot.getValue())).entrySet())
                            {
                                Map stat = (Map) entry.getValue();
                                statistics.add(new Game((String)stat.get("firstUser"),  (long) Objects.requireNonNull(stat.get("firstUserResult")), (String)stat.get("secondUser"),  (long) Objects.requireNonNull(stat.get("secondUserResult")), new Date((Long)stat.get("gameDate"))));
                            }
                            StatisticsListAdapter statsAdapter = new StatisticsListAdapter(getBaseContext(), R.layout.statistics_list_item, statistics);
                            ListView statsListView = findViewById(R.id.statistics_list_view);
                            statsListView.setAdapter(statsAdapter);
                            ProgressBar progressBar = findViewById(R.id.progress_stat);
                            progressBar.setVisibility(GONE);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                    }
                });
    }
}
