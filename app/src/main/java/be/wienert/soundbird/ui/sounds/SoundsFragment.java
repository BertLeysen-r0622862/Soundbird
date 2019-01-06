package be.wienert.soundbird.ui.sounds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import be.wienert.soundbird.R;
import be.wienert.soundbird.ui.ViewModelFactory;
import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class SoundsFragment extends Fragment {

    protected SoundsRecyclerView soundsRecyclerView;
    protected SoundsViewModel viewModel;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sounds_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        swipeContainer.setEnabled(false);

        RecyclerView recyclerView = view.findViewById(R.id.sounds_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        soundsRecyclerView = new SoundsRecyclerView();
        recyclerView.setAdapter(soundsRecyclerView);

        viewModel = getViewModel();

        soundsRecyclerView.setOnClickListener(sound -> {
            try {
                viewModel.play(sound);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @NonNull
    protected SoundsViewModel getViewModel() {
        return ViewModelFactory.getInstance(getActivity().getApplication()).create(SoundsViewModel.class);
    }
}
