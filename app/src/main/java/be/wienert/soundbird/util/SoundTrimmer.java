package be.wienert.soundbird.util;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler;
import nl.bravobit.ffmpeg.FFmpeg;

public class SoundTrimmer {

    private Context context;
    private FFmpeg ffmpeg;

    public SoundTrimmer(Context context) {
        this.context = context;
        ffmpeg = FFmpeg.getInstance(context);
        if (!ffmpeg.isSupported()) {
            ffmpeg = null;
        }
    }

    public LiveData<Uri> trim(InputStream stream, int start, int end) {
        MutableLiveData<Uri> data = new MutableLiveData<>();
        if (ffmpeg == null) {
            data.setValue(null);
            return data;
        }

        File input;
        try {
            input = File.createTempFile(UUID.randomUUID().toString(), null, context.getCacheDir());
//            output = File.createTempFile(UUID.randomUUID().toString(), null, context.getCacheDir());
        } catch (IOException e) {
            data.setValue(null);
            return data;
        }

        Uri output = Uri.fromFile(new File(context.getCacheDir(), UUID.randomUUID().toString()));

        String[] cmd = {
                "-ss", Integer.toString(start),
                "-to", Integer.toString(end),
                "-i", input.getAbsolutePath(),
                "-acodec", "copy",
                "-f", "mp3",
                output.getPath()};

        try {
            FileUtil.copy(stream, input);
//            ffmpeg.setTimeout(5000);
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onProgress(String message) {}

                @Override
                public void onFailure(String message) {
                    data.setValue(null);
                }

                @Override
                public void onSuccess(String message) {
                    input.delete();
                    data.setValue(output);
                }

                @Override
                public void onFinish() {

                }
            });
        } catch (IOException e) {
            input.delete();
            data.setValue(null);
        }

        return data;
    }

    public LiveData<Uri> trim(Uri uri, int start, int end) {
        try {
            InputStream stream = context.getContentResolver().openInputStream(uri);
            return trim(stream, start, end);
        } catch (FileNotFoundException | NullPointerException e) {
            MutableLiveData<Uri> data = new MutableLiveData<>();
            data.setValue(null);
            return data;
        }
    }

    public int getDuration(Uri uri) {
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(context, uri);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            return Integer.parseInt(durationStr) / 1000;
        } catch (Exception ignored) {
            return 0;
        }
    }
}
