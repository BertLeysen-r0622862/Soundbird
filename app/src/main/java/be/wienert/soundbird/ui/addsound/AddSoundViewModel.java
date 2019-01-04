package be.wienert.soundbird.ui.addsound;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.io.InputStream;

import be.wienert.soundbird.data.DataManager;

public class AddSoundViewModel extends AndroidViewModel {
    DataManager dataManager;

    public AddSoundViewModel(@NonNull Application application, DataManager dataManager) {
        super(application);
        this.dataManager = dataManager;
    }

    public LiveData<DataManager.SoundWrapper> addSound(String name, InputStream inputStream) {
        return dataManager.addLocalSound(name, inputStream);
    }
}
