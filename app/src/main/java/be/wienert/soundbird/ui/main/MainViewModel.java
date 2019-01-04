package be.wienert.soundbird.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import be.wienert.soundbird.data.model.Sound;
import be.wienert.soundbird.util.SoundPlayer;

public class MainViewModel extends AndroidViewModel {
    private SoundPlayer soundPlayer;

    public MainViewModel(@NonNull Application application, SoundPlayer soundPlayer) {
        super(application);
        this.soundPlayer = soundPlayer;
    }

    public LiveData<Sound> getPlayingSound() {
        return soundPlayer.getCurrentSound();
    }

    public void stopSound() {
        soundPlayer.stop();
    }
}