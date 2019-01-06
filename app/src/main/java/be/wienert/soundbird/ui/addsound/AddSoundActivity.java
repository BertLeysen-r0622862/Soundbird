package be.wienert.soundbird.ui.addsound;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import be.wienert.soundbird.R;
import be.wienert.soundbird.ui.ViewModelFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddSoundActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;

    @BindView(R.id.soundNameEditText)
    EditText soundNameEditText;

    @BindView(R.id.selectedFileTextView)
    TextView selectedFileTextView;

    @BindView(R.id.playStopButton)
    Button playStopButton;

    @BindView(R.id.startEditText)
    EditText startEditText;

    @BindView(R.id.endEditText)
    EditText endEditText;

    AddSoundViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sound_layout);
        ButterKnife.bind(this);

        viewModel = getViewModel();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel.getPlayingSound().observe(this, sound -> {
            int icon;
            if (sound == null) {
                icon = R.drawable.ic_play_arrow_black_24dp;
            } else {
                icon = R.drawable.ic_stop_black_24dp;
            }
            playStopButton.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        });

        viewModel.getFileUri().observe(this, fileUri -> {
            if (fileUri == null) {
                selectedFileTextView.setText("none");
            } else {
                selectedFileTextView.setText(getFileName(fileUri));
            }
            startEditText.setText("0");
            endEditText.setText(Integer.toString(viewModel.getDuration()));
        });
    }

    @OnClick(R.id.selectFileButton)
    public void selectFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @OnClick(R.id.saveSoundButton)
    public void save(View view) {
        viewModel.stop();

        try {
            viewModel.setSoundName(soundNameEditText.getText().toString());
        } catch (IllegalArgumentException e) {
            soundNameEditText.setError(e.getMessage());
            return;
        }

        viewModel.trimFile(getStart(), getEnd()).observe(this, uri -> {
            try {
                InputStream stream = getContentResolver().openInputStream(uri);
                viewModel.addLocalSound(stream).observe(this, soundWrapper -> {
                    assert soundWrapper != null;
                    if (soundWrapper.exception != null) {
                        soundNameEditText.setError(soundWrapper.exception.getMessage());
                    } else {
                        finish();
                    }
                });
            } catch (NullPointerException | FileNotFoundException e) {
                soundNameEditText.setError("File not found");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && resultData != null) {
                viewModel.setFileUri(resultData.getData());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    public AddSoundViewModel getViewModel() {
        return ViewModelFactory.getInstance(getApplication()).create(AddSoundViewModel.class);
    }

    private String getFileName(Uri uri) {
        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        }
        return "";
    }

    @OnClick(R.id.playStopButton)
    public void playStop() {
        if (viewModel.getPlayingSound().getValue() == null) {
            viewModel.trimFile(getStart(), getEnd()).observe(this, uri -> {
                if (uri != null) {
                    viewModel.play(uri);
                }
            });
        } else {
            viewModel.stop();
        }
    }

    public int getStart() {
        int start = 0;
        try {
            start = Integer.parseInt(startEditText.getText().toString());
        } catch (NumberFormatException ignored) {
        }
        return start;
    }

    public int getEnd() {
        int end = 0;
        try {
            end = Integer.parseInt(endEditText.getText().toString());
        } catch (NumberFormatException ignored) {
        }
        return end;
    }
}