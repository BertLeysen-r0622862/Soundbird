package be.wienert.soundbird.ui.sounds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RemoteSoundsFragment extends SoundsFragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getRemoteSounds().observe(this, sounds -> soundsRecyclerView.setSounds(sounds));

        soundsRecyclerView.setOnLongClickListener(sound -> {
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                try {
                    viewModel.addRemoteToLocal(sound);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
