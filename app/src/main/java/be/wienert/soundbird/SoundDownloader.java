package be.wienert.soundbird;

import android.content.Context;
import android.net.Uri;

import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import be.wienert.soundbird.model.Sound;

public class SoundDownloader {
    private final Context context;

    public SoundDownloader(Context context) {
        this.context = context;
    }

    /**
     * Downloads sound and stores it in private storage.
     * If the file already exists it's not overwritten.
     * @param sound sound to download
     * @return uri to stored file
     * @throws IOException on network error
     */
    public Uri download(Sound sound) throws IOException {
        File file = new File(context.getFilesDir(), String.valueOf(sound.getId()) + ".mp3");

        if (!file.exists()) {
            try {
                Ion.with(context)
                        .load(sound.getUri().toString())
                        .write(file)
                        .get();
            } catch (ExecutionException | InterruptedException e) {
                throw new IOException(e);
            }
        }

        return Uri.fromFile(file);
    }
}
