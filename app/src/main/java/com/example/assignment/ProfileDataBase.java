package com.example.assignment;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MatchModel.class}, version = 1)
public abstract class ProfileDataBase extends RoomDatabase {
    public abstract ProfileDao taskDao();
}
