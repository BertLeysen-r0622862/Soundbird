package be.wienert.soundbird.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.koushikdutta.ion.Ion;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executors;

import be.wienert.soundbird.data.local.SoundDatabaseHelper;
import be.wienert.soundbird.data.model.Sound;
import be.wienert.soundbird.data.remote.RestApi;

public class DataManager {
    private final Context context;
    private SoundDatabaseHelper localDb;
    private RestApi restApi;

    public DataManager(Context context) {
        localDb = new SoundDatabaseHelper(context);
        restApi = new RestApi();
        this.context = context;
    }

    private static LiveData<SoundWrapper> doAsync(AsyncSoundTask task) {
        MutableLiveData<DataManager.SoundWrapper> data = new MutableLiveData<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                data.postValue(new SoundWrapper(task.execute()));
            } catch (Exception e) {
                data.postValue(new SoundWrapper(e));
            }
        });
        return data;
    }

    public LiveData<List<Sound>> getLocalSounds() {
        return localDb.getSounds();
    }

    public LiveData<List<Sound>> getRemoteSounds() {
        return restApi.getSounds();
    }

    public LiveData<SoundWrapper> addLocalSound(String name, InputStream inputStream) {
        return doAsync(() -> localDb.addSound(name, inputStream));
    }

    public LiveData<SoundWrapper> deleteLocalSound(Sound sound) {
        return doAsync(() -> {
            localDb.delete(sound);
            return sound;
        });
    }

    public LiveData<SoundWrapper> addRemoteToLocal(Sound sound) {
        return doAsync(() -> {
            Ion.getDefault(context).getConscryptMiddleware().enable(false);
            InputStream stream = Ion.with(context).load(sound.getUri().toString()).asInputStream().get();
            return localDb.addSound(sound.getName(), stream);
        });
    }

    public LiveData<Sound> addLocalToRemote(Sound sound) {
        return restApi.addSound(sound);
    }

    private interface AsyncSoundTask {
        Sound execute() throws Exception;
    }

    public static class SoundWrapper {
        public Sound sound;
        public Exception exception;

        SoundWrapper(Sound sound) {
            this.sound = sound;
            this.exception = null;
        }

        SoundWrapper(Exception exception) {
            this.exception = exception;
            this.sound = null;
        }

    }
}
