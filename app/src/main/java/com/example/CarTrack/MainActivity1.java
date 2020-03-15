package com.example.CarTrack;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.CarTrack.R;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity1 extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new EntriesFragment()).commit();
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;

                    case R.id.entries:
                        fragment = new EntriesFragment();
                        break;

                    case R.id.settings:
                        fragment = new Settings_Fragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                return true;
            }
        });

    }

   /* @Override
    public void onBackPressed() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage("Do you want to Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finish();

                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                   AlertDialog alertDialog = builder.create();
                   alertDialog.show();


    }*/
}

