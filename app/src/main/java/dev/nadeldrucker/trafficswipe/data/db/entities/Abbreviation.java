package dev.nadeldrucker.trafficswipe.data.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Abbreviation {
    @PrimaryKey
    public String abbreviation;
    public String name;
}
