package be.wienert.soundbird.util;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class SoundTrimmer {

    private Context context;
    private FFmpeg ffmpegInstance = null;

    public SoundTrimmer(Context context) {
        this.context = context;
        FFmpeg ffmpeg = FFmpeg.getInstance(context);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {
                    ffmpegInstance = ffmpeg;
                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException ignored) {
        }
    }

    public LiveData<File> trim(InputStream stream, int start, int end) {
        MutableLiveData<File> data = new MutableLiveData<>();
        if (ffmpegInstance == null) {
            data.setValue(null);
            return data;
        }

        File input, output;
        try {
            input = File.createTempFile(UUID.randomUUID().toString(), null, context.getCacheDir());
            output = File.createTempFile(UUID.randomUUID().toString(), null, context.getCacheDir());
        } catch (IOException e) {
            data.setValue(null);
            return data;
        }

        String[] cmd = {"-ss " + start + " -t " + end + " -i " + input.getAbsolutePath() + " -acodec copy " + output.getAbsolutePath()};

        try {
            FileUtil.copy(stream, input);
            ffmpegInstance.setTimeout(10000);
            ffmpegInstance.execute(cmd, new ExecuteBinaryResponseHandler() {

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
                public void onFinish() {}
            });
        } catch (FFmpegCommandAlreadyRunningException | IOException e) {
            input.delete();
            output.delete();
            data.setValue(null);
        }

        return data;
    }
}
