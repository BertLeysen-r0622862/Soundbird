package be.wienert.soundbird.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.wienert.soundbird.model.Sound;
import be.wienert.soundbird.model.SoundBoard;

public class SoundBoardStaticService implements SoundBoardService {

    private List<SoundBoard> soundBoards = new ArrayList<>();
    private List<Sound> sounds = new ArrayList<>();

    public SoundBoardStaticService() {
        sounds.add(new Sound(1, "Kat"));
        sounds.add(new Sound(2, "Hond"));

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
}
