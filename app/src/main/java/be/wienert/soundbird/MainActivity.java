package be.wienert.soundbird;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import be.wienert.soundbird.service.SoundBoardRestService;
import be.wienert.soundbird.service.SoundBoardService;

public class MainActivity extends AppCompatActivity {

    private SoundBoardService service;
    private FloatingActionButton toAddButton;
    public static CreateSoundsTask createSoundTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toAddButton = (FloatingActionButton) findViewById(R.id.toAddButton);
        toAddButton.setOnClickListener(buttonListener);

        service = new SoundBoardRestService();
        createSoundTask=new CreateSoundsTask(this);
        createSoundTask.execute(service);

        //Intent intentAddFile = new Intent(this,AddButtonActivity.class);
        //startActivity(intentAddFile);
    }

   private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.toAddButton:
                    startActivity(new Intent(MainActivity.this, AddButtonActivity.class));
                    break;
                    }
        }
    };
}
