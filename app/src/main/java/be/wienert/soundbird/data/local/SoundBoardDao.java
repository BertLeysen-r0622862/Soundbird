package be.wienert.soundbird.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import be.wienert.soundbird.data.model.Sound;

@Dao
public interface SoundBoardDao {
    @Query("SELECT * FROM sound")
    LiveData<List<Sound>> getSounds();

    @Insert
    void insert(Sound... sounds);

    @Update
    void update(Sound... sounds);

    @Delete
    void delete(Sound... sounds);
}
