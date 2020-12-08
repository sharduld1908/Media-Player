package com.example.mediaplayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ListOfSong extends AppCompatActivity {
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private RecyclerView listOfSongs;
    private SongAdapter songAdapter;
    private LoadingBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_song);
        loadingBar = new LoadingBar(this);
        loadingBar.showDialog();
        FloatingActionButton addButton = findViewById(R.id.addSong);
        addButton.setOnClickListener(view -> openFileChooser());
        listOfSongs = findViewById(R.id.list_of_songs);

        mStorageRef = FirebaseStorage.getInstance().getReference("music");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("music");

        populate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingBar.hideDialog();
            }
        },3000);
    }
    private void populate() {
        Query query = FirebaseDatabase.getInstance()
                .getReference().child("music");

        FirebaseRecyclerOptions<Song> options =
                new FirebaseRecyclerOptions.Builder<Song>()
                        .setQuery(query, Song.class)
                        .build();

        songAdapter = new SongAdapter(options);
        listOfSongs.setHasFixedSize(true);
        listOfSongs.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listOfSongs.setAdapter(songAdapter);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,Constants.PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onStart() {
        super.onStart();
        songAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        songAdapter.stopListening();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadingBar.showDialog();
        if(requestCode == Constants.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            int idx = fileUri.toString().indexOf("%2F");
            String name = fileUri.toString().substring(idx + 3);

            StorageReference musicRef = mStorageRef.child(System.currentTimeMillis() + "_" + name + "/");
            UploadTask uploadTask = musicRef.putFile(fileUri);

            Task<Uri> urlTask = uploadTask
            .continueWithTask(task -> {
                if(!task.isSuccessful()) {
                    throw task.getException();
                }
                return musicRef.getDownloadUrl();
            })
            .addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Music Uploaded",Toast.LENGTH_SHORT).show();
                    Uri downloadUri = task.getResult();
                    Song song = new Song(downloadUri,name);
                    String uploadId = mDatabaseRef.push().getKey();
                    if(uploadId != null) {
                        mDatabaseRef.child(uploadId).setValue(song);
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                }
                loadingBar.hideDialog();
            });
        }
    }
}