package be.wienert.soundbird.ui.sounds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.Objects;

import be.wienert.soundbird.data.model.Sound;

public class RemoteSoundsFragment extends SoundsFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getRemoteSounds().observe(this, sounds -> soundsRecyclerView.setSounds(sounds));

        soundsRecyclerView.setOnLongClickListener(this::add);
    }

    private void add(Sound sound) {
        viewModel.addRemoteToLocal(sound).observe(this, soundWrapper -> {
            assert soundWrapper != null;
            if (soundWrapper.exception != null) {
                return;
            }

            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()), "Sound added", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", v -> viewModel.delete(soundWrapper.sound));
            snackbar.show();
        });
    }
}
