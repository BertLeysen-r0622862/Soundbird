package be.wienert.soundbird.data;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public LiveData<List<Sound>> getLocalSounds() {
        return localDb.getSounds();
    }

    public LiveData<List<Sound>> getRemoteSounds() {
        return restApi.getSounds();
    }

    public void addLocalSound(String name, InputStream inputStream) throws IOException {
        localDb.addSound(name, inputStream);
    }

    public void deleteLocalSound(Sound sound) {
        localDb.delete(sound);
    }

    public void addRemoteToLocal(Sound sound) throws IOException {
        try {
            Ion.getDefault(context).getConscryptMiddleware().enable(false);
            InputStream stream = Ion.with(context).load(sound.getUri().toString()).asInputStream().get();
            addLocalSound(sound.getName(), stream);
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException(e);
        }
    }

    public LiveData<Sound> addLocalToRemote(Sound sound) {
        return restApi.addSound(sound);
    }
}
