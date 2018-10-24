package be.wienert.soundbird.service;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.wienert.soundbird.R;
import be.wienert.soundbird.model.Sound;
import be.wienert.soundbird.model.SoundBoard;

public class SoundBoardStaticService implements SoundBoardService {

    private List<SoundBoard> soundBoards = new ArrayList<>();
    private List<Sound> sounds = new ArrayList<>();
    private Map<Integer, Integer> resourceIds = new HashMap<>();

    public SoundBoardStaticService() {
        sounds.add(new Sound(1, "Kat", this));
        resourceIds.put(1, R.raw.im_loving_it);
        sounds.add(new Sound(2, "Hond", this));
        resourceIds.put(2, R.raw.im_loving_it);

        soundBoards.add(new SoundBoard(1, "Dieren", sounds));
    }


    @Override
    public List<SoundBoard> getBoards() {
        return soundBoards;
    }

    @Override
    public List<Sound> getSounds() {
        return sounds;
    }

    @Override
    public Uri getSoundUrl(Sound sound) {
        return Uri.parse("android.resource://be.wienert.soundbird/" + resourceIds.get(sound.getId()));
    }
}
