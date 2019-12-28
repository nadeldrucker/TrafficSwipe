package dev.nadeldrucker.trafficswipe.data.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Abbreviation {
    @PrimaryKey
    private String abbreviation;
    private String name;

    public Abbreviation(String abbreviation, String name) {
        this.abbreviation = abbreviation;
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        if (abbreviation.length() != 3) throw new IllegalArgumentException("Abbreviations have to have a length of 3 chars!");
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
