package com.example.battleship.Helpers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseHelper
{
    private final FirebaseDatabase database;
    private final FirebaseStorage storage;

    public FirebaseHelper()
    {
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public DatabaseReference GetDatabaseReference(String path)
    {
        return database.getReference(path);
    }

    public StorageReference GetStorageReference(String location)
    {
        return storage.getReference(location);
    }
}
