package be.wienert.soundbird.ui.addsound;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import be.wienert.soundbird.data.DataManager;
import be.wienert.soundbird.data.model.Sound;
import be.wienert.soundbird.util.SoundPlayer;
import be.wienert.soundbird.util.SoundTrimmer;

public class AddSoundViewModel extends AndroidViewModel {
    private final SoundTrimmer soundTrimmer;
    private DataManager dataManager;
    private final SoundPlayer soundPlayer;
    private MutableLiveData<Uri> fileUri = new MutableLiveData<>();
    private MutableLiveData<String> soundName = new MutableLiveData<>();

    public AddSoundViewModel(@NonNull Application application, DataManager dataManager, SoundPlayer soundPlayer, SoundTrimmer soundTrimmer) {
        super(application);
        this.dataManager = dataManager;
        this.soundPlayer = soundPlayer;
        this.soundTrimmer = soundTrimmer;
    }

    public LiveData<DataManager.SoundWrapper> addLocalSound(InputStream inputStream) {
        return dataManager.addLocalSound(soundName.getValue(), inputStream);
    }

    public LiveData<Sound> getPlayingSound() {
        return soundPlayer.getCurrentSound();
    }

    public void playStop() {
        try {
            if (getPlayingSound().getValue() == null && fileUri.getValue() != null) {
                soundPlayer.play(new Sound(UUID.randomUUID(), fileUri.getValue(), ""));
            } else {
                soundPlayer.stop();
            }
        } catch (IOException ignored) {
        }
    }

    public LiveData<Uri> getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri.setValue(fileUri);
    }

    public LiveData<String> getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) throws IllegalArgumentException {
        if (soundName.length() == 0) {
            throw new IllegalArgumentException("Enter a name");
        }
        this.soundName.setValue(soundName);
    }
}
