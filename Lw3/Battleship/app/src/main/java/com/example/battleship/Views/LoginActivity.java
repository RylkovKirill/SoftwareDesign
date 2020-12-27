package com.example.battleship.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.battleship.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import com.example.battleship.Helpers.AnimationHelper;


public class LoginActivity extends AppCompatActivity
{

    private EditText username;
    private EditText password;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AnimationHelper.StartAnimation(findViewById(R.id.container));

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loading = findViewById(R.id.loading);

        Button login = findViewById(R.id.login);
        login.setOnClickListener(this::SignInOrRegister);
    }

    private void SignInOrRegister(View view)
    {
        String email = this.username.getText().toString();
        String password = this.password.getText().toString();

        loading.setVisibility(View.VISIBLE);

        if(IsValidEmail(email))
        {
            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(t1 ->
                    {
                        if (t1.isSuccessful())
                        {
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(t2 ->
                                    {
                                        Intent intent = new Intent(this, MainActivity.class);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(t3 ->
                                    {
                                        loading.setVisibility(View.GONE);
                                        MakeToast(R.string.loginFailed);
                                    });
                        }
                    });
        }
        else
        {
            MakeToast(R.string.invalidEmail);
        }
    }

    private boolean IsValidEmail(String email)
    {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void MakeToast(int text)
    {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
