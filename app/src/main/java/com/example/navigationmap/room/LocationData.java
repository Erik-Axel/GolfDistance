package com.example.navigationmap.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "locationData")
public class LocationData {

    @PrimaryKey
    private int id;


    @ColumnInfo
    private String lat;

    @ColumnInfo
    private String lng;

    public LocationData(int id, String lat, String lng) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return this.id;
    }


    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
