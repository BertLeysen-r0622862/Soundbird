package be.wienert.soundbird.ui.sounds;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import be.wienert.soundbird.data.DataManager;
import be.wienert.soundbird.data.model.Sound;
import be.wienert.soundbird.util.SoundPlayer;

public class SoundsViewModel extends AndroidViewModel {
    private DataManager dataManager;
    private SoundPlayer soundPlayer;

    public SoundsViewModel(@NonNull Application application, DataManager dataManager, SoundPlayer soundPlayer) {
        super(application);
        this.dataManager = dataManager;
        this.soundPlayer = soundPlayer;
    }

    public LiveData<List<Sound>> getLocalSounds() {
        return dataManager.getLocalSounds();
    }

    public LiveData<List<Sound>> getRemoteSounds() {
        return dataManager.getRemoteSounds();
    }

    public void play(Sound sound) throws IOException {
        soundPlayer.play(sound);
    }

    public void delete(Sound sound) {
        dataManager.deleteLocalSound(sound);
    }

    public void addRemoteToLocal(Sound sound) throws IOException {
        dataManager.addRemoteToLocal(sound);
    }

    public LiveData<Sound> addLocalToRemote(Sound sound) {
        return dataManager.addLocalToRemote(sound);
    }
}
