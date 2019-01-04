package be.wienert.soundbird.ui.sounds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import be.wienert.soundbird.data.model.Sound;

public class LocalSoundsFragment extends SoundsFragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getLocalSounds().observe(this, sounds -> soundsRecyclerView.setSounds(sounds));

        soundsRecyclerView.setOnLongClickListener(this::delete);
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
