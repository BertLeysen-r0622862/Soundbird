package be.wienert.soundbird.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import be.wienert.soundbird.data.model.Sound;

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
        long size = copy(inputStream, file, max_size);
        if (size > max_size) {
            throw new IllegalArgumentException("File too big");
        }

        Sound sound = new Sound(uuid, Uri.fromFile(file), name);
        db.soundDao().insert(sound);
        return sound;
    }

    private long copy(InputStream inputStream, File dst, long maxSize) throws IOException {
        long size = 0;
        try (FileOutputStream outStream = new FileOutputStream(dst)) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0 && size < maxSize) {
                outStream.write(buf, 0, len);
                size += len;
            }
        } finally {
            inputStream.close();
            if (size >= maxSize) {
                dst.delete();
            }
        }
        return size;
    }

    public void delete(Sound sound) {
        db.soundDao().delete(sound);
        File file = new File(sound.getUri().getPath());
        file.delete();
    }

    public void editSound(Sound sound){
        db.soundDao().update(sound);
    }
}
