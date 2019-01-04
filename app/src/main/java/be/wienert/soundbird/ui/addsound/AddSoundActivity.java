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
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;

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

    Uri fileUri;

    AddSoundViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sound_layout);
        ButterKnife.bind(this);

        viewModel = getViewModel();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        if (soundNameEditText.length() == 0) {
            soundNameEditText.setError("Enter a name");
            return;
        }

        try {
            viewModel.addLocalSound(soundNameEditText.getText().toString(), getContentResolver().openInputStream(fileUri))
                    .observe(this, soundWrapper -> {
                        assert soundWrapper != null;
                        if (soundWrapper.exception != null) {
                            soundNameEditText.setError(soundWrapper.exception.getMessage());
                        } else {
                            finish();
                        }
                    });
        } catch (FileNotFoundException e) {
            soundNameEditText.setError("Could not open file");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && resultData != null) {
                fileUri = resultData.getData();
                assert fileUri != null;
                selectedFileTextView.setText(getFileName(fileUri));
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
}