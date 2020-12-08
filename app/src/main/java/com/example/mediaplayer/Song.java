package com.example.mediaplayer;

import android.net.Uri;

public class Song {
    private String uri;
    private String name;

    Song() {}

    public Song(final Uri uri, final String name) {
        this.uri = uri.toString();
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setName(String name) {
        this.name = name;
    }
}
