package be.wienert.soundbird.model;

import android.net.Uri;

public class Sound {
    //ID TYPE
    private int id;
    private String name;
    private Uri uri;

    public Sound(int id, String name, Uri uri) {
        this.id = id;
        this.name = name;
        this.uri = uri;
    }

    public Sound(Sound sound) {
        this(sound.id, sound.name, sound.uri);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
