package com.example.finallogin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class PlacesAround extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private  static final int ERROR_DIALOG_REQUEST=9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_around);

        if(isServicesOK())
        {
            init();
        }

    }
    private void init()
    {
        Button getMap=(Button) findViewById(R.id.getMap);
//        getMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(Explore.this,MapsActivity
//                        .class);
//                startActivity(intent);
//            }
//        });

        getMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PlacesAround.this,MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isServicesOK()
    {
        Log.d(TAG, "isServicesOK: checking google services version");
        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(PlacesAround.this);

        if(available== ConnectionResult.SUCCESS)
        {
            Log.d(TAG, "isServicesOK: Google play services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Log.d(TAG, "isServicesOK: An Error has occurred but we can fix it");
            Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(PlacesAround.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else
        {
            Toast.makeText(this, "You cant make map requests", Toast.LENGTH_LONG).show();

        }
        return false;

    }
}
