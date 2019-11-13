package dev.nadeldrucker.trafficswipe.dao.gestures;

import java.util.List;

public class GestureTrainingEntity {
    private Character character;
    private List<List<TouchCoordinate>> paths;

    public GestureTrainingEntity() {

    }

    public GestureTrainingEntity(Character character, List<List<TouchCoordinate>> paths) {
        this.character = character;
        this.paths = paths;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public List<List<TouchCoordinate>> getPaths() {
        return paths;
    }

    public void setPaths(List<List<TouchCoordinate>> paths) {
        this.paths = paths;
    }
}
