package com.realestate.testapplication.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Drinks.class},version = 2,exportSchema = false)
public abstract class MyAppDatabase extends RoomDatabase {

    public abstract DAO dao();
}



