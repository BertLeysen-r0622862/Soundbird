package be.wienert.soundbird.ui;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import be.wienert.soundbird.data.DataManager;
import be.wienert.soundbird.ui.addsound.AddSoundViewModel;
import be.wienert.soundbird.ui.main.MainViewModel;
import be.wienert.soundbird.ui.sounds.SoundsViewModel;
import be.wienert.soundbird.util.SoundPlayer;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory instance;

    private final Application application;
    private final DataManager dataManager;
    private final SoundPlayer soundPlayer;

    private ViewModelFactory(Application application) {
        this.application = application;
        dataManager = new DataManager(application);
        soundPlayer = new SoundPlayer(application);
    }

    public static ViewModelFactory getInstance(Application application) {

        if (instance == null) {
            synchronized (ViewModelFactory.class) {
                if (instance == null) {
                    instance = new ViewModelFactory(application);
                }
            }
        }
        return instance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddSoundViewModel.class)) {
            //noinspection unchecked
            return (T) new AddSoundViewModel(application, dataManager);
        } else if (modelClass.isAssignableFrom(MainViewModel.class)) {
            //noinspection unchecked
            return (T) new MainViewModel(application, soundPlayer);
        } else if (modelClass.isAssignableFrom(SoundsViewModel.class)) {
            //noinspection unchecked
            return (T) new SoundsViewModel(application, dataManager, soundPlayer);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
