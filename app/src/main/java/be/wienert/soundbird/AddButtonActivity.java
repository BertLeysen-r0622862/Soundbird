package be.wienert.soundbird;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import be.wienert.soundbird.model.Sound;

public class AddButtonActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    MainActivity mac;

    public AddButtonActivity(MainActivity mainActivity){
        this.mac = mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_button);

        Button fileDialogButton = findViewById(R.id.filedialogButton);
        fileDialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("audio/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText buttonNameEdit = findViewById(R.id.editText);
                String buttonName = buttonNameEdit.getText().toString();
                Log.i("saveButton", buttonName);
            }
        });

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri audioUri = resultData.getData();
                String name = ((EditText) findViewById(R.id.editText)).getText().toString();
                addSound(name, audioUri);
            }
        }
    }

    private void addSound(String name, Uri uri) {
        final Sound sound = new Sound(ThreadLocalRandom.current().nextInt(1, 100000), name, uri);

        Log.i("SoundBird", "Added sound Name: " + name + ", Uri: " + uri.toString());

        Button button = new Button(mac);
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
        gridLayout.addView(button);
    }

}



