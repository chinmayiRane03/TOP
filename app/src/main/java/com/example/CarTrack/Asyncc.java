package com.example.CarTrack;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Asyncc extends AsyncTask<String, String, String> {

    Click uploadfunc;
    Context ctx;
    ProgressDialog dialog;
    ProgressBar progressBar;
    byte[] imagearray;
    GPSTracker gps;
    int currentprogress=0;
    Bitmap image;
    Uri tempuri;
    String firebase_storage_picture;

    SharedPreferences pref;
    SharedPreferences.Editor editor;



    public String getFirebase_storage_picture() {
        return firebase_storage_picture;
    }




    public Asyncc(Context ctx, Uri dataBaos,ProgressBar progressBar) {
        this.ctx=ctx;
        uploadfunc= new Click();
        pref = ctx.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        this.progressBar=progressBar;
        progressBar.setMax(100);
        this.tempuri=dataBaos;
    }


    @Override
    protected String doInBackground(String... voids) {



        String upload_url=upload_in_background_storage();
        return upload_url;
    }


    private String upload_in_background_storage() {


        FirebaseStorage storage;
        final StorageReference storageRef;
        storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference().child("" + new Date().getTime());

        final UploadTask uploadTask = storageRef.putFile(tempuri);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
                currentprogress = (int) progress;

                publishProgress(currentprogress+"");
                progressBar.setProgress(currentprogress);
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ctx, "Please Connect To Internet", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("storage url", "onSuccess: uri= "+ uri.toString());
                        firebase_storage_picture=uri+"";
                        editor.putString("last_upload_url", uri+"");
                        editor.commit(); // commit changes

                    }
                });
            }
        });

        return firebase_storage_picture+"";

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);

        progressBar.setVisibility(View.GONE);

        firebase_storage_picture=aVoid;




    }


    @Override
    protected void onProgressUpdate(String... values) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(Integer.parseInt(values[0]));
       // Log.d("progg",values[0]+"");
        if(Integer.parseInt(values[0])==100)
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(ctx, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
          //  update_firestore();
            //
        }
        //super.onProgressUpdate(values);
    }





}

