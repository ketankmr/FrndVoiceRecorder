package com.sensy.frndvoicetest.Data;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sensy.frndvoicetest.Model.AudioModel;


@Database(entities = {AudioModel.class}, version = 1)
public abstract class AudioDatabase extends RoomDatabase {

    public abstract DataAcessObject AudioDao();
    private static AudioDatabase INSTANCE;

    public static AudioDatabase getDatabase(Context context){

        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AudioDatabase.class, "Audio_database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }

        return INSTANCE;

    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
