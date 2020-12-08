package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Player extends AppCompatActivity {
    private Button pause;
    private Button play;

    private MediaPlayer mediaPlayer;
    private double startTime;
    private double finishTime;
    public static Uri musicUri;
    public static String name;

    private SeekBar bar;
    private TextView start;
    private TextView stop;
    private TextView songName;

    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        songName = findViewById(R.id.songName);
        songName.setText(name);
        mediaPlayer = MediaPlayer.create(this, musicUri);

        LinearLayout seekBar = findViewById(R.id.seekBar);
        start = seekBar.findViewById(R.id.start);
        stop = seekBar.findViewById(R.id.stop);
        bar = seekBar.findViewById(R.id.bar);
        bar.setClickable(true);

        RelativeLayout buttonPanel = findViewById(R.id.buttonPanel);

        handler = new Handler();

        play = buttonPanel.findViewById(R.id.play);
        play.setEnabled(false);
        pause = buttonPanel.findViewById(R.id.pause);
        Button forward = buttonPanel.findViewById(R.id.forward);
        Button reverse = buttonPanel.findViewById(R.id.reverse);

        mediaPlayer.start();
        finishTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        bar.setMax((int)finishTime);
        start.setText(convert((long)startTime));
        stop.setText(convert((long)finishTime));

        bar.setProgress((int)startTime);
        handler.postDelayed(runnable,100);


        play.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(),"Play song",Toast.LENGTH_SHORT).show();
            mediaPlayer.start();

            finishTime = mediaPlayer.getDuration();
            startTime = mediaPlayer.getCurrentPosition();

            start.setText(convert((long)startTime));
            stop.setText(convert((long)finishTime));

            bar.setProgress((int)startTime);
            handler.postDelayed(runnable,100);
            play.setEnabled(false);
            pause.setEnabled(true);
        });

        pause.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(),"Pause song",Toast.LENGTH_SHORT).show();
            mediaPlayer.pause();

            play.setEnabled(true);
            pause.setEnabled(false);
        });

        forward.setOnClickListener(view -> {
            int temp = (int)startTime;

            if(temp + 5000 <= finishTime) {
                startTime = startTime + 5000;
                mediaPlayer.seekTo((int) startTime);
            }
        });

        reverse.setOnClickListener(view -> {
            int temp = (int)startTime;

            if(temp - 5000 > 0) {
                startTime = startTime - 5000;
                mediaPlayer.seekTo((int) startTime);
            }
        });

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b) {
                    mediaPlayer.seekTo(i);
                }
                start.setText(convert(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

        });

        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            play.setEnabled(true);
            pause.setEnabled(false);
            mediaPlayer.seekTo(0);
        });
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer.isPlaying()) {
                startTime = mediaPlayer.getCurrentPosition();
                start.setText(convert((long)startTime));
                bar.setProgress((int)startTime);
                handler.postDelayed(this, 100);
            }
        }
    };

    private String convert(long time) {
        return String.format(Locale.ENGLISH,"%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }
}