package be.wienert.soundbird;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.ThreadLocalRandom;

import be.wienert.soundbird.model.Sound;

public class AddButtonActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    MainActivity mac;
    Uri dataUri;
    Uri tempUri;
    String buttonName= "fout";

    public AddButtonActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_button);

        //back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button fileDialogButton = findViewById(R.id.filedialogButton);
        fileDialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("audio/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, READ_REQUEST_CODE); }

        });

        Button saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean big;
                int length =1001;
                try { InputStream inputStream = getContentResolver().openInputStream(dataUri);
                   length = inputStream.available();
                   System.out.println("je lengte" + length);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                EditText buttonNameEdit = findViewById(R.id.editText);
                if (buttonNameEdit.length() <= 0){
                    buttonNameEdit.setError("Enter a Name");
                } else if (buttonNameEdit.length() > 10){
                    buttonNameEdit.setError("Button Name is too long");
                } else if (length<0 ||length>1021960){
                    buttonNameEdit.setError("File is too big");
                }
                else {
                    buttonName = ((EditText) findViewById(R.id.editText)).getText().toString();
                    Log.i("saveButton", buttonName + dataUri);
                    if (dataUri != null) {
                        addSound(buttonName, dataUri);
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                 dataUri = resultData.getData();
                 System.out.println("Dit is de dataUri"+ dataUri);
                 //buttonName = ((EditText) findViewById(R.id.editText)).getText().toString();
            }
        }
    }

    private void addSound(String name, Uri uri) {
        final Sound sound = new Sound(ThreadLocalRandom.current().nextInt(1, 100000), name, uri);
        Log.i("SoundBird", "Added sound Name: " + name + ", Uri: " + uri.toString());

        MainActivity.createSoundTask.addSound(sound);
        finish();

    }

}



