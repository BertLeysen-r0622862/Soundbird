package be.wienert.soundbird.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import be.wienert.soundbird.data.model.Sound;
import be.wienert.soundbird.util.FileUtil;

public class SoundDatabaseHelper {
    private final SoundDatabase db;
    private final Context context;

    public SoundDatabaseHelper(Context context) {
        db = Room.databaseBuilder(context, SoundDatabase.class, "soundboard").fallbackToDestructiveMigration().build();
        this.context = context;
    }

    public LiveData<List<Sound>> getSounds() {
        return db.soundDao().getSounds();
    }

    public Sound addSound(String name, InputStream inputStream) throws IOException {
        UUID uuid = UUID.randomUUID();
        File file = new File(context.getFilesDir(), uuid.toString() + ".mp3");
        long max_size = 1000000;
        long size = FileUtil.copy(inputStream, file, max_size);
        if (size > max_size) {
            throw new IllegalArgumentException("File too big");
        }

        Sound sound = new Sound(uuid, Uri.fromFile(file), name);
        db.soundDao().insert(sound);
        return sound;
    }

    public void delete(Sound sound) {
        db.soundDao().delete(sound);
        File file = new File(sound.getUri().getPath());
        file.delete();
    }


    public void update(Sound sound){
              db.soundDao().update(sound);
    }
}
