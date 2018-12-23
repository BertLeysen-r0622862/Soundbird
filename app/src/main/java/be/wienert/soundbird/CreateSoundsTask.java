package be.wienert.soundbird;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.*;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.TextViewCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import be.wienert.soundbird.model.Sound;
import be.wienert.soundbird.service.SoundBoardService;

public class CreateSoundsTask extends AsyncTask<SoundBoardService, Void, List<Sound>> {

    private WeakReference<Activity> activityRef;
    private SoundDownloader soundDownloader;
    public final SoundPlayer soundPlayer;

    CreateSoundsTask(Activity activity) {
        activityRef = new WeakReference<>(activity);
        soundDownloader = new SoundDownloader(activity.getApplicationContext());
        soundPlayer = new SoundPlayer(activity.getApplicationContext());
    }

    @Override
    protected List<Sound> doInBackground(SoundBoardService... soundBoardServices) {
        SoundBoardService service = soundBoardServices[0];

        try {
            List<Sound> sounds = service.getSounds();
            for (Sound sound : sounds) {
                sound.setUri(soundDownloader.download(sound));
            }
            return sounds;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    protected void onPostExecute(List<Sound> sounds) {
        super.onPostExecute(sounds);

        for (Sound sound : sounds) {
            addSound(sound);
        }
    }

    //Adding sound by Sound
    public void addSound(final Sound sound) {
        Activity activity = activityRef.get();
        if (activity == null)
            return;

        Button button = new Button(activity);
        button.setText(sound.getName());
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                try {
                    soundPlayer.play(sound);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        GridLayout gridLayout = activity.findViewById(R.id.grid_layout);
        GridLayout.LayoutParams parem = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f));
        button.setLayoutParams(parem);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(button, 1, 80, 10,TypedValue.COMPLEX_UNIT_DIP);
        button.setTextSize(5+40/button.getText().toString().length());
        button.setIncludeFontPadding(false);
        button.setPadding(0,0,0,0);
        gridLayout.addView(button);
    }

    //Adding sound by name and uri
    private void addSound(String name,Uri uri){
        Sound sound = new Sound(7,name,uri);
        addSound(sound);
    }
}
