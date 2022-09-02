package com.example.assignment;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface ProfileDao {

    @Insert
    void insert(MatchModel model);

    @Update
    void update(MatchModel model);

    @Delete
    void delete(MatchModel model);
}
