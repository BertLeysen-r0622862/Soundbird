package be.wienert.soundbird.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import be.wienert.soundbird.data.model.Sound;

@Database(entities = {Sound.class}, version = 3, exportSchema = false)
public abstract class SoundDatabase extends RoomDatabase {
    public abstract SoundBoardDao soundDao();
}
