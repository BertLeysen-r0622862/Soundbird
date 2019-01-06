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

    private LiveData<List<Sound>> localSounds;

    public SoundsViewModel(@NonNull Application application, DataManager dataManager, SoundPlayer soundPlayer) {
        super(application);
        this.dataManager = dataManager;
        this.soundPlayer = soundPlayer;
    }

    public LiveData<List<Sound>> getLocalSounds() {
        if (localSounds == null) {
            localSounds = dataManager.getLocalSounds();
        }
        return localSounds;
    }

    public LiveData<List<Sound>> getRemoteSounds() {
        return dataManager.getRemoteSounds();
    }

    public void play(Sound sound) throws IOException {
        soundPlayer.play(sound);
    }

    public LiveData<DataManager.SoundWrapper> delete(Sound sound) {
        return dataManager.deleteLocalSound(sound);
    }

    public LiveData<DataManager.SoundWrapper> addRemoteToLocal(Sound sound) {
        return dataManager.addRemoteToLocal(sound);
    }

    public LiveData<Sound> addLocalToRemote(Sound sound) {
        return dataManager.addLocalToRemote(sound);
    }

    public LiveData<DataManager.SoundWrapper> update(Sound sound) {
        return dataManager.updateLocalSound(sound);
    }
}
