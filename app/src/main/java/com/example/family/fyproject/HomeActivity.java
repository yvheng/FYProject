package com.example.family.fyproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    private Intent intent;

    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    Boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if(!isConnected){
            Toast.makeText(getApplicationContext(), "No network connection", Toast.LENGTH_LONG).show();
        }
    }

    public void checking(View v){
        intent = new Intent(this, CheckingActivity.class);
        startActivity(intent);
    }

    public void translate(View v){
        intent = new Intent(this, TranslateActivity.class);
        startActivity(intent);
    }
}
