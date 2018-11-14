package be.wienert.soundbird.service;

import java.io.IOException;
import java.util.List;

import be.wienert.soundbird.model.Sound;
import be.wienert.soundbird.model.SoundBoard;

public interface SoundBoardService {
    List<SoundBoard> getBoards() throws IOException;
    List<Sound> getSounds() throws IOException;
}
