package be.wienert.soundbird.util;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

import be.wienert.soundbird.data.model.Sound;

public class SoundPlayer {

    private Context context;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private MutableLiveData<Sound> currentSound = new MutableLiveData<>();

    public SoundPlayer(Context context) {
        this.context = context;
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(MediaPlayer::start);
        mediaPlayer.setOnErrorListener((mp, what, extra) -> false);
        mediaPlayer.setOnCompletionListener(mp -> currentSound.setValue(null));
    }

    public void play(Sound sound) throws IOException {
        mediaPlayer.reset();
        mediaPlayer.setDataSource(context, sound.getUri());
        mediaPlayer.prepareAsync();
        currentSound.setValue(sound);
    }

    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            currentSound.setValue(null);
        }
    }

    public MutableLiveData<Sound> getCurrentSound() {
        return currentSound;
    }
}
