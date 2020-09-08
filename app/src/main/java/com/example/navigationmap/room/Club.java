package com.example.navigationmap.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "club")
public class Club {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String club;

    @ColumnInfo
    private int avgDistance;

    @ColumnInfo
    private int longestDistance;

    private boolean selected = false;

    public Club(String club) {
        this.club = club;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLongestDistance(int distance){
        this.longestDistance = distance;
    }

    public void setAvgDistance(int distance) {
        this.avgDistance = distance;
    }

    public String getClub() {
        return club;
    }

    public int getAvgDistance() {
        return avgDistance;
    }

    public int getLongestDistance() {
        return longestDistance;
    }

    public void setSelected(boolean value) {
        selected = value;
    }

    public boolean getSelected() {
        return selected;
    }



}