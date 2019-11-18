package dev.nadeldrucker.trafficswipe.dao.gestures;

import java.util.List;

public class GestureTrainingEntity {
    private Character character;
    private List<List<TouchCoordinate>> paths;
    private String userId;

    public GestureTrainingEntity() {

    }

    public GestureTrainingEntity(Character character, List<List<TouchCoordinate>> paths, String userId) {
        this.character = character;
        this.paths = paths;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
