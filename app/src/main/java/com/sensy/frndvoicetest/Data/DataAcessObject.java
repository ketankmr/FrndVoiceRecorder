package com.sensy.frndvoicetest.Data;




import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sensy.frndvoicetest.Model.AudioModel;


@Dao
public interface DataAcessObject  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     void insertAudio(AudioModel Model);

    @Query("SELECT * From Audio_Table")
    AudioModel[] getListofAudios();

    @Update
    public void updateAudioModel(AudioModel... audioModels);

}