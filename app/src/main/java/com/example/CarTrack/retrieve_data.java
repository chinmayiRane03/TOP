package com.example.CarTrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

public class retrieve_data extends AppCompatActivity {

    private FirestoreRecyclerAdapter<CarDetails, Car_Viewholer> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_data);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("Cars");

        Log.d("QUERY", query+"");
              //  .orderBy("TimeStamp", Query.Direction.ASCENDING);



        FirestoreRecyclerOptions<CarDetails> response = new FirestoreRecyclerOptions.Builder<CarDetails>()
                .setQuery(query, CarDetails.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<CarDetails, Car_Viewholer>(response) {

            @Override
            protected void onBindViewHolder(@NonNull Car_Viewholer holder, int position, @NonNull final CarDetails car) {
                holder.address.setText(car.getAddresss());
                holder.timestamp.setText(getDate(car.getTimeStamp()+""));
                holder.uploader.setText(car.getUploader()+"");
                Picasso.get().load(car.getImage_Url()+"").into(holder.car_picture);
                Log.d("URLLLLLLLLLLLL",car.getImage_Url()+"");
             //   Toast.makeText(retrieve_data.this, car.getAccuracy()+"", Toast.LENGTH_SHORT).show();
                holder.navigate_gmaps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // Toast.makeText(retrieve_data.this, car.getLatitude()+"", Toast.LENGTH_SHORT).show();
                        try {
                            Uri gmmIntentUri = Uri.parse("geo:"+car.getLatitude()+","+car.getLongitude()+"?q="+car.getAddresss());
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);

                        } catch (Exception e) {
                            e.printStackTrace();

                            Toast.makeText(retrieve_data.this, "Google Maps Not Installed", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }

            @NonNull
            @Override
            public Car_Viewholer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_details_card, parent, false);
                Log.d("inflate",view+"");
                return new Car_Viewholer(view);
            }
        };
        recyclerView.setAdapter(adapter);

    }

    private String getDate(String s) {

        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(Long.parseLong(s) * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }


    private class Car_Viewholer extends RecyclerView.ViewHolder {
        private View view;
        public TextView address;
        public TextView uploader;
        public TextView timestamp;
        public TextView accuracy;
        public ImageView navigate_gmaps;
        public ImageView car_picture;

    //    public TextView zoneInitials;

        Car_Viewholer(View itemView) {
            super(itemView);
            view = itemView;

            address = itemView.findViewById(R.id.address);
            uploader = itemView.findViewById(R.id.uploader);
            timestamp = itemView.findViewById(R.id.days);
            navigate_gmaps=itemView.findViewById(R.id.navigate_gps);
            car_picture=itemView.findViewById(R.id.car_pic);
        //    accuracy = itemView.findViewById(R.id.textView_site_id);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
