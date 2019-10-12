package com.sensy.frndvoicetest.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.File;

@Entity(tableName = "Audio_Table")
public class AudioModel {

    @PrimaryKey
    @NonNull
    public long id;

    @ColumnInfo
   public String audioFilePath;

    @ColumnInfo
    public String audioFileUrl;


    public AudioModel() {
    }

    public AudioModel(long id, String audioFilePath) {
        this.id = id;
        this.audioFilePath = audioFilePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecordedFile() {
        return audioFilePath;
    }

    public void setRecordedFile(String recordedFile) {
        this.audioFilePath = recordedFile;
    }

    public String getAudioFileUrl() {
        return audioFileUrl;
    }

    public void setAudioFileUrl(String audioFileUrl) {
        this.audioFileUrl = audioFileUrl;
    }
}
