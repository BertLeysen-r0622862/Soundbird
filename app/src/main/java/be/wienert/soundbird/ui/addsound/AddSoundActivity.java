package be.wienert.soundbird.ui.addsound;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.FileNotFoundException;

import be.wienert.soundbird.R;
import be.wienert.soundbird.ui.ViewModelFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddSoundActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;

    @BindView(R.id.editText)
    EditText name;

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

    @OnClick(R.id.filedialogButton)
    public void selectFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @OnClick(R.id.saveButton)
    public void save(View view) {
        if (name.length() == 0) {
            name.setError("Enter a name");
            return;
        }

        try {
            viewModel.addSound(name.getText().toString(), getContentResolver().openInputStream(fileUri))
                    .observe(this, soundWrapper -> {
                        assert soundWrapper != null;
                        if (soundWrapper.exception != null) {
                            name.setError(soundWrapper.exception.getMessage());
                        } else {
                            finish();
                        }
                    });
        } catch (FileNotFoundException e) {
            name.setError("Could not open file");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && resultData != null) {
            fileUri = resultData.getData();
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
}