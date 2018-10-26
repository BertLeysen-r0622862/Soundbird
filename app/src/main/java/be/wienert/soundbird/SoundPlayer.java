package be.wienert.soundbird;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

import be.wienert.soundbird.model.Sound;

public class SoundPlayer {
    enum STATE {
        NOT_PREPARED,
        PREPARING,
        PREPARED,
        ERROR,
    }

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private STATE state = STATE.NOT_PREPARED;

    public SoundPlayer(Sound sound, Context context) throws IOException {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(context, sound.getUri());
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                state = STATE.PREPARED;
                mp.start();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                state = STATE.ERROR;
                return false;
            }
        });
    }

    public void start() {
        switch (state) {
            case NOT_PREPARED:
                mediaPlayer.prepareAsync();
                state = STATE.PREPARING;
                break;
            case PREPARED:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }
}
