package be.wienert.soundbird.ui.addsound;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;
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
    private MediatorLiveData<Uri> trimmedUri = new MediatorLiveData<>();
    private MutableLiveData<String> soundName = new MutableLiveData<>();
    private int duration = 0;

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

    public void play(Uri uri) {
        try {
            soundPlayer.play(new Sound(UUID.randomUUID(), uri, ""));
        } catch (IOException ignored) {
        }
    }

    public void stop() {
        soundPlayer.stop();
    }

    public LiveData<Uri> getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri.setValue(fileUri);
        duration = soundTrimmer.getDuration(fileUri);
    }

    public LiveData<String> getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) throws IllegalArgumentException {
        if (soundName.trim().isEmpty()) {
            throw new IllegalArgumentException("Enter a name");
        }
        this.soundName.setValue(soundName);
    }

    public LiveData<Uri> trimFile(int start, int end) {
        end = Math.max(Math.min(end, duration), 0);
        start = Math.max(Math.min(start, end), 0);
        Uri fileUri = this.fileUri.getValue();
        if (trimmedUri.getValue() != null) {
            new File(trimmedUri.getValue().getPath()).delete();
        }
        trimmedUri.addSource(soundTrimmer.trim(fileUri, start, end), uri -> trimmedUri.setValue(uri));
        return trimmedUri;
    }

    public int getDuration() {
        return duration;
    }
}
