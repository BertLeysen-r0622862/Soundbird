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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import be.wienert.soundbird.R;
import be.wienert.soundbird.data.model.Sound;
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
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        swipeContainer.setEnabled(false);

        RecyclerView recyclerView = view.findViewById(R.id.sounds_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        soundsRecyclerView = new SoundsRecyclerView();
        recyclerView.setAdapter(soundsRecyclerView);

        viewModel = getViewModel();
        final Animation myAnim = AnimationUtils.loadAnimation(this.getActivity(), R.anim.milkshake);




        soundsRecyclerView.setOnClickListener(sound -> {
            /*int pos =0;
            boolean found = false;
            for (Sound s : soundsRecyclerView.getSounds()){
                if (!found) {
                    if (s != sound) {
                        pos++;
                    }

                }
            }
            View buttonview = recyclerView.findViewHolderForAdapterPosition(pos).itemView;*/
            try {
                //myAnim.setDuration(viewModel.getDuration(sound));
                //buttonview.setAnimation(myAnim);
                //buttonview.animate();
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
