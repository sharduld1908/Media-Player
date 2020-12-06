package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

public class ListOfSong extends AppCompatActivity {

    FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_song);

        addButton = findViewById(R.id.addSong);
        addButton.setOnClickListener(view -> {
            File file = new File(Environment.getExternalStorageDirectory(),
                    "Download");

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName() + ".provider",file);
            intent.setDataAndType(uri,"*/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        });
    }
}