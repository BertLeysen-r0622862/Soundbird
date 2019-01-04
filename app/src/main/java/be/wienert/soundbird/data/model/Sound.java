package be.wienert.soundbird.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.UUID;

import be.wienert.soundbird.data.local.UUIDConverter;
import be.wienert.soundbird.data.local.UriConverter;

@Entity
public class Sound {
    @PrimaryKey
    @NonNull
    @TypeConverters(UUIDConverter.class)
    private UUID uuid;

    @TypeConverters(UriConverter.class)
    private Uri uri;

    private String name;

    public Sound(@NonNull UUID uuid, Uri uri, String name) {
        this.uuid = uuid;
        this.uri = uri;
        this.name = name;
    }

    @NonNull
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull UUID uuid) {
        this.uuid = uuid;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
