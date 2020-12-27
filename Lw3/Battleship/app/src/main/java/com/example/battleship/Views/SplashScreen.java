package com.example.battleship.Views;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(isAnonymous())
        {
            startActivity( new Intent(this, LoginActivity.class));
        }
        else
        {
            startActivity( new Intent(this, MainActivity.class));
        }

        finish();
    }

    private boolean isAnonymous()
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null)
        {
            return true;
        }
        else
        {
            return currentUser.isAnonymous();
        }
    }
}