package be.wienert.soundbird.ui.sounds;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import be.wienert.soundbird.R;
import be.wienert.soundbird.data.model.Sound;

public class LocalSoundsFragment extends SoundsFragment {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getLocalSounds().observe(this, sounds -> soundsRecyclerView.setSounds(sounds));

        registerForContextMenu(view);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteButton:
                delete(soundsRecyclerView.getLastClickedSound());
                break;
            case R.id.editButton:
                edit(soundsRecyclerView.getLastClickedSound());
                break;
            case R.id.uploadButton:
                upload(soundsRecyclerView.getLastClickedSound());
                break;
            case R.id.shareButton:
                share(soundsRecyclerView.getLastClickedSound());
                break;

        }
        return super.onContextItemSelected(item);
    }

    private void share(Sound sound) {
        Uri path = FileProvider.getUriForFile(Objects.requireNonNull(getContext()),
                "be.wienert.soundbird", new File(sound.getUri().getPath()));
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("audio/mp3");
        share.putExtra(Intent.EXTRA_STREAM, path);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share Sound File"));
    }

    private void upload(Sound sound) {
        viewModel.addLocalToRemote(sound).observe(this, sound2 -> {
            assert sound2 != null;
            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()), "Sound uploaded", Snackbar.LENGTH_LONG);
            snackbar.show();
        });
    }

    public String getFragmentTag(int pos) {
        return "android:switcher:" + R.id.pager + ":" + pos;
    }

    private void edit(Sound sound) {

        dialogPopup(sound);
    }


    public void dialogPopup(Sound sound) {
        List<Sound> sounds = new ArrayList<>(Objects.requireNonNull(viewModel.getLocalSounds().getValue()));
        final Dialog d = new Dialog(getActivity()); // initialize dialog
        d.setContentView(R.layout.dialog_layout);
        d.setTitle("Edit Button");
        final EditText ed1 = d.findViewById(R.id.soundNameEditText);
        ed1.setText(sound.getName());

        Button b = (Button) d.findViewById(R.id.confirmEditSoundButton);
        // button 1 click
        b.setOnClickListener(v -> {
            sound.setName(ed1.getText().toString());
            viewModel.update(sound).observe(getViewLifecycleOwner(), soundWrapper -> d.dismiss());
            d.dismiss();
        });

        d.show(); // show the dialog
    }

    private void delete(Sound sound) {
        // Fake delete
        List<Sound> sounds = new ArrayList<>(Objects.requireNonNull(
                viewModel.getLocalSounds().getValue()));
        sounds.remove(sound);
        soundsRecyclerView.setSounds(sounds);

        Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()), "Sound deleted", Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", v ->
                // Undo fake delete
                soundsRecyclerView.setSounds(viewModel.getLocalSounds().getValue()));

        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    viewModel.delete(sound);
                }

            }
        });
        snackbar.show();
    }
}
