package be.wienert.soundbird.model;

import java.util.List;

public class SoundBoard {
    private int id;
    private String name;
    private List<Sound> sounds;

    public SoundBoard(int id, String name, List<Sound> sounds) {
        this.id = id;
        this.name = name;
        this.sounds = sounds;
    }

    public List<Sound> getSounds() {
        return sounds;
    }

    public void addSound(Sound sound) {
        sounds.add(sound);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
