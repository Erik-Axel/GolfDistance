package com.example.navigationmap.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.navigationmap.room.LocationData;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

@Dao
public interface LocationDataDao {

    @Insert
    void addNewMarkerLocation(LocationData locationData);

    @Update
    void updateLocationData(LocationData locationData);

    @Query("select * from locationData where id = 1")
    LocationData getLocationData();

    @Delete
    void deleteMarkerLocation(LocationData locationData);
}
