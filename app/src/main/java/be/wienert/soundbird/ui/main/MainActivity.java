package be.wienert.soundbird.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import be.wienert.soundbird.R;
import be.wienert.soundbird.ui.ViewModelFactory;
import be.wienert.soundbird.ui.addsound.AddSoundActivity;
import be.wienert.soundbird.ui.sounds.LocalSoundsFragment;
import be.wienert.soundbird.ui.sounds.RemoteSoundsFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.stopSoundButton)
    FloatingActionButton stopSoundButton;

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    MainViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.bind(this);

        viewModel = getViewModel();
        viewModel.getPlayingSound().observe(this, sound -> {

            if (sound != null) {
                stopSoundButton.show();
            } else {
                stopSoundButton.hide();
            }
        });

        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }

    @OnClick(R.id.toAddButton)
    public void addNewSound(View view) {
        startActivity(new Intent(MainActivity.this, AddSoundActivity.class));
    }

    @OnClick(R.id.stopSoundButton)
    public void stopSound(View view) {
        viewModel.stopSound();
    }

    @NonNull
    public MainViewModel getViewModel() {
        return ViewModelFactory.getInstance(getApplication()).create(MainViewModel.class);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new LocalSoundsFragment();
                case 1:
                    return new RemoteSoundsFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Offline";
                case 1:
                    return "Online";

                default:
                    return null;
            }
        }
    }
}
