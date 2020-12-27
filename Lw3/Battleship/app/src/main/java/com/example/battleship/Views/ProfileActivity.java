package com.example.battleship.Views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.battleship.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import com.example.battleship.Helpers.AnimationHelper;
import com.example.battleship.Helpers.FirebaseHelper;

import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity
{
    ImageView userImage;
    EditText username;
    Button  save;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        AnimationHelper.StartAnimation(findViewById(R.id.container));

        userImage = findViewById(R.id.userImage);
        save = findViewById(R.id.save);
        username = findViewById(R.id.username);

        FirebaseHelper firebaseHelper = new FirebaseHelper();

        storageReference = firebaseHelper.GetStorageReference("images");
        databaseReference = firebaseHelper.GetDatabaseReference("profiles");
        firebaseAuth = FirebaseAuth.getInstance();

        userImage.setOnClickListener(v -> openFileChooser());
        save.setOnClickListener(v ->
        {
            String name = username.getText().toString().trim();
            if(!name.equals(""))
            {
                databaseReference.child(Objects.requireNonNull(firebaseAuth.getUid())).child("username").setValue(name);
                Toasty.normal(ProfileActivity.this, R.string.changesSaved, Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference.child(Objects.requireNonNull(firebaseAuth.getUid())).addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                if (Objects.equals(snapshot.getKey(), "username"))
                {
                    username.setText(Objects.requireNonNull(snapshot.getValue()).toString());
                }
                if (Objects.equals(snapshot.getKey(), "ImagePath"))
                {
                    Picasso.get().load(Objects.requireNonNull(snapshot.getValue()).toString()).into(userImage);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Uri uri = data.getData();
            Picasso.get().load(uri).into(userImage);
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()+ "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri)));
            fileReference.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task ->
                    {
                        String upload = Objects.requireNonNull(task.getResult()).toString();
                        databaseReference.child(Objects.requireNonNull(firebaseAuth.getUid())).child("ImagePath").setValue(upload);
                    }));
        }
    }
}