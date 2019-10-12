package com.sensy.frndvoicetest.ViewModel;

import android.app.Application;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.sensy.frndvoicetest.Model.AudioModel;
import com.sensy.frndvoicetest.Data.Api;
import com.sensy.frndvoicetest.Data.AudioDatabase;
import com.sensy.frndvoicetest.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AudioViewModel extends AndroidViewModel {

    MutableLiveData<ArrayList<AudioModel>> audio_array ;
    Context context ;
    private AudioDatabase audioDatabase;
    private MediaRecorder myAudioRecorder;
   private AudioModel currentRecording;


    public AudioViewModel(@NonNull Application application) {
        super(application);
        audio_array = new MutableLiveData<>();
        context = getApplication().getBaseContext();
        audioDatabase = audioDatabase.getDatabase(application);
    }


    public LiveData<ArrayList<AudioModel>> getList()
    {
        return audio_array ;
    }


    public  void  sendAudio(AudioModel audioModel){
           Toast.makeText(context,"Uploading Audio",Toast.LENGTH_LONG).show();

        final File audiofile=new File(audioModel.getRecordedFile());


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Api.IMAGE_UPLOAD_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));

                            String url = obj.getJSONObject("data").getString("media");

                            if(currentRecording!=null){
                                currentRecording.setAudioFileUrl(url);
                                updateAudioObject(currentRecording);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                try {
                    byte[] bytesArray = new byte[(int) audiofile.length()];
                    FileInputStream fis = new FileInputStream(audiofile);
                    fis.read(bytesArray); //read file into bytes[]
                    fis.close();
                    params.put("media", new DataPart(audiofile.getName() + ".3gp",bytesArray));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(context).add(volleyMultipartRequest);

    }

    public void addToDatabase(AudioModel audioModel){
        audioDatabase.AudioDao().insertAudio(audioModel);
        getAudioList();
    }

    public void getAudioList(){
      AudioModel[] audioModels=audioDatabase.AudioDao().getListofAudios();
        audio_array.setValue( new ArrayList<AudioModel>(Arrays.asList(audioModels)));
    }

    public void  updateAudioObject(AudioModel audioModel){
        audioDatabase.AudioDao().updateAudioModel(audioModel);
    }

    public void recordAudio() {

        try {

            currentRecording = initializeAudioRecoder();
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (Exception ise) {
            // make something ...
            Log.e("Intialize error",ise.getMessage(),ise);
        }
        Toast.makeText(context, "Recording started", Toast.LENGTH_LONG).show();

    }

    private AudioModel initializeAudioRecoder() {
        Long audioID = System.currentTimeMillis();
        String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/recording" + audioID + ".3gp";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
        return new AudioModel(audioID, outputFile);
    }


    public void stopRecording() {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
        Toast.makeText(context, "Audio Recorded", Toast.LENGTH_LONG).show();
        sendAudio(currentRecording);
        addToDatabase(currentRecording);


    }
}
