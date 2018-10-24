package be.wienert.soundbird.model;

import android.net.Uri;

import be.wienert.soundbird.service.SoundBoardService;

public class Sound {
    private final int id;
    private final String name;
    private SoundBoardService service;

    public Sound(int id, String name, SoundBoardService service) {
        this.id = id;
        this.name = name;
        this.service = service;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Uri getUri() {
        return service.getSoundUrl(this);
    }

    public void setService(SoundBoardService service) {
        this.service = service;
    }
}
