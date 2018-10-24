package be.wienert.soundbird;

import android.app.Activity;
import android.os.AsyncTask;
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

    CreateSoundsTask(Activity activity) {
        activityRef = new WeakReference<>(activity);
    }

    @Override
    protected List<Sound> doInBackground(SoundBoardService... soundBoardServices) {
        SoundBoardService service = soundBoardServices[0];

        try {
            return service.getSounds();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    protected void onPostExecute(List<Sound> sounds) {
        super.onPostExecute(sounds);

        for (Sound sound : sounds) {
            try {
                addSound(sound);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addSound(Sound sound) throws IOException {
        Activity activity = activityRef.get();
        if (activity == null)
            return;

        final SoundPlayer soundPlayer = new SoundPlayer(sound);

        Button button = new Button(activity);
        button.setText(sound.getName());
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                soundPlayer.start();
            }
        });

        GridLayout gridLayout = activity.findViewById(R.id.grid_layout);
        gridLayout.addView(button);
    }
}