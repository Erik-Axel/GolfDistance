package com.example.navigationmap.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.navigationmap.room.Club;

import java.util.List;

@Dao
public interface ClubDao {

    @Query("Select * from club")
    List<Club> getClubs();

    @Update
    void setLongestDistance(Club club);

    @Update
    void setAvgDistance(Club club);

    @Delete
    void removeClub(Club club);

    @Insert
    void addClub(Club club);

}
