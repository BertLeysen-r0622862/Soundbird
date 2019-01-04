package be.wienert.soundbird.ui.addsound;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

import be.wienert.soundbird.data.DataManager;

public class AddSoundViewModel extends AndroidViewModel {
    DataManager dataManager;

    public AddSoundViewModel(@NonNull Application application, DataManager dataManager) {
        super(application);
        this.dataManager = dataManager;
    }

    public void addSound(String name, InputStream inputStream) throws IOException {
        dataManager.addLocalSound(name, inputStream);
    }
}
