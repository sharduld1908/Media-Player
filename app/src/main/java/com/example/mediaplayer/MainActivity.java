package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Button pause;
    private Button play;

    private MediaPlayer mediaPlayer;
    private double startTime;
    private double finishTime;

    private SeekBar bar;
    private TextView start;
    private TextView stop;

    private Handler handler;
    private int once = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, R.raw.song);

        LinearLayout seekBar = findViewById(R.id.seekBar);
        start = seekBar.findViewById(R.id.start);
        stop = seekBar.findViewById(R.id.stop);
        bar = seekBar.findViewById(R.id.bar);
        bar.setClickable(true);

        LinearLayout buttonPanel = findViewById(R.id.buttonPanel);

        handler = new Handler();

        play = buttonPanel.findViewById(R.id.play);
        pause = buttonPanel.findViewById(R.id.pause);pause.setEnabled(false);
        Button forward = buttonPanel.findViewById(R.id.forward);
        Button reverse = buttonPanel.findViewById(R.id.reverse);

        play.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(),"Play song",Toast.LENGTH_SHORT).show();
            mediaPlayer.start();

            finishTime = mediaPlayer.getDuration();
            startTime = mediaPlayer.getCurrentPosition();
            if(once == 0) {
                bar.setMax((int)finishTime);
                once = 1;
            }

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
}