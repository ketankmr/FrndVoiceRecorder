package com.sensy.frndvoicetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sensy.frndvoicetest.Model.AudioModel;
import com.sensy.frndvoicetest.ViewModel.AudioViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button stop, record;
    AudioViewModel audioViewModel;
    ArrayList<AudioModel> audioModelArrayList = new ArrayList<>();
    RecyclerView audioListRecyclerView;
    AudioListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        stop = (Button) findViewById(R.id.stop);
        record = (Button) findViewById(R.id.record);
        audioListRecyclerView = findViewById(R.id.rv_main);
        audioListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        audioViewModel = ViewModelProviders.of(this).get(AudioViewModel.class);

        stop.setEnabled(false);
        adapter = new AudioListAdapter(this,audioModelArrayList);
        audioListRecyclerView.setAdapter(adapter);



        audioViewModel.getList().observe(this, new Observer<ArrayList<AudioModel>>() {
            @Override
            public void onChanged(ArrayList<AudioModel> audioModels) {
                audioModelArrayList.clear();
                audioModelArrayList.addAll(audioModels);
                adapter.notifyDataSetChanged();
            }
        });

        audioViewModel.getAudioList();

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasPermission()){
                    return;
                }
               if(audioViewModel!=null) {
                   audioViewModel.recordAudio();
                   record.setEnabled(false);
                   stop.setEnabled(true);
               }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(audioViewModel!=null) {
                    audioViewModel.stopRecording();
                    record.setEnabled(true);
                    stop.setEnabled(false);
                }
            }
        });
    }

    public boolean  hasPermission(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED
         || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE}, 25);

        }else {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       if(requestCode==25){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if(audioViewModel!=null) {
                    audioViewModel.recordAudio();
                    record.setEnabled(false);
                    stop.setEnabled(true);
                }

            }
        }
    }
}


