package be.wienert.soundbird;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

import be.wienert.soundbird.model.Sound;

public class SoundPlayer {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Context context;

    public SoundPlayer(Context context) {
        this.context = context;
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }

    public void play(Sound sound) throws IOException {
        mediaPlayer.reset();
        mediaPlayer.setDataSource(context, sound.getUri());
        mediaPlayer.prepareAsync();
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
