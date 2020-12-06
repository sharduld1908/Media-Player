package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class OpeningScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(OpeningScreen.this, ListOfSong.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            OpeningScreen.this.startActivity(intent);
            OpeningScreen.this.finish();
        },2000);
    }
}