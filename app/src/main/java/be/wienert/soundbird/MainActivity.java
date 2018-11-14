package be.wienert.soundbird;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import be.wienert.soundbird.service.SoundBoardRestService;
import be.wienert.soundbird.service.SoundBoardService;

public class MainActivity extends AppCompatActivity {

    private SoundBoardService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = new SoundBoardRestService();
        new CreateSoundsTask(this).execute(service);
        Intent intentAddFile = new Intent(this,AddButtonActivity.class);
        startActivity(intentAddFile);
    }
}
