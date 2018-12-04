package be.wienert.soundbird;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.content.Intent.ACTION_GET_CONTENT;
import static android.content.Intent.ACTION_OPEN_DOCUMENT;
import static android.content.Intent.ACTION_PICK;

public class AddButtonActivity extends AppCompatActivity {
    protected static final int REQUEST_PICK_AUDIO = 1;
    private Button fileDialogButton;
    private Uri audioUri;
    private Button saveButton;
    private Button cancelButton;
    private ImageView mImageView;
    private String buttonName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_button);

        fileDialogButton = (Button) findViewById(R.id.filedialogButton);
        fileDialogButton.setOnClickListener(buttonListener);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(buttonListener);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(buttonListener);
    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.filedialogButton:
                    /*Intent pickAudioIntent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    );
                    startActivityForResult(pickAudioIntent, REQUEST_PICK_AUDIO);*/
                    Intent intent_upload = new Intent();
                    intent_upload.setType("audio/*");
                    intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent_upload,1);
                    break;

                case R.id.saveButton:

                    EditText buttonNameEdit = (EditText) findViewById(R.id.editText);
                    buttonName = buttonNameEdit.getText().toString();
                    Log.i("saveButton",buttonName);
                    break;

                case R.id.cancelButton:
                    finish();
                break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        if(requestCode == 1){

            if(resultCode == RESULT_OK){

                //the selected audio.
                Uri uri = data.getData();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
                if (RESULT_OK == resultCode) {
                    audioUri = intent.getData();
                }
    }*/
}



