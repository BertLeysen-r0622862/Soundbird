package be.wienert.soundbird;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import be.wienert.soundbird.service.SoundBoardService;
import be.wienert.soundbird.service.SoundBoardStaticService;

public class MainActivity extends AppCompatActivity {

    private SoundBoardService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = new SoundBoardStaticService();
        new CreateSoundsTask(this).execute(service);
    }
}
