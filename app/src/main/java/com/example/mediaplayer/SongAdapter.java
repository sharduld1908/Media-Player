package com.example.mediaplayer;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class SongAdapter extends FirebaseRecyclerAdapter<Song, SongAdapter.MusicViewHolder> {

    public SongAdapter(@NonNull FirebaseRecyclerOptions<Song> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MusicViewHolder holder, int position, @NonNull Song model) {
        holder.nameOfTheSong.setText(model.getName());
        holder.songUri = Uri.parse(model.getUri());
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_card,parent,false);
        return new MusicViewHolder(view);
    }

    public static class MusicViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameOfTheSong;
        private Uri songUri;
        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOfTheSong = itemView.findViewById(R.id.name_of_song);
            Button playButton = itemView.findViewById(R.id.play_button);

            playButton.setOnClickListener(view -> {
                Player.musicUri = songUri;
                Intent intent = new Intent(itemView.getContext(),Player.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
