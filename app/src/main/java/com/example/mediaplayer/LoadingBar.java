package com.example.mediaplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingBar {
    Activity activity;
    AlertDialog dialog;

    public LoadingBar(Activity activity) {
        this.activity = activity;
    }

    void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_layout,null));
        dialog = builder.create();
        dialog.show();
    }

    void hideDialog() {
        dialog.dismiss();
    }
}
