package be.wienert.soundbird.ui.sounds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LocalSoundsFragment extends SoundsFragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getLocalSounds().observe(this, sounds -> soundsRecyclerView.setSounds(sounds));

        soundsRecyclerView.setOnLongClickListener(sound -> {
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> viewModel.delete(sound));
        });
    }
}
