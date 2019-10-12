package com.sensy.frndvoicetest;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sensy.frndvoicetest.Model.AudioModel;

import java.util.ArrayList;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioItemViewHolder> {
    Context context;
    ArrayList<AudioModel> audioModelArrayList;

    public AudioListAdapter(Context context, ArrayList<AudioModel> audioModelArrayList) {
        this.context = context;
        this.audioModelArrayList = audioModelArrayList;
    }

    @NonNull
    @Override
    public AudioItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio, parent, false);
        return new AudioItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioItemViewHolder holder, final int position) {
        holder.audioTitle.setText(audioModelArrayList.get(position).getId()+".3gp");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio(audioModelArrayList.get(position).audioFilePath);
            }
        });

    }

    @Override
    public int getItemCount() {
        return audioModelArrayList.size();
    }

    public class AudioItemViewHolder  extends  RecyclerView.ViewHolder{
        TextView audioTitle;
        CardView cardView;
        public AudioItemViewHolder(@NonNull View itemView) {
            super(itemView);
            audioTitle = itemView.findViewById(R.id.audio_title);
            cardView =itemView.findViewById(R.id.cv_main);
        }
    }

        private void playAudio(String outputFile){

                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(context, "Playing Audio", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // make something
                }
    }
}
