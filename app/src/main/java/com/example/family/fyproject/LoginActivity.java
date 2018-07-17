package com.example.family.fyproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.String;

public class LoginActivity extends AppCompatActivity {
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    Boolean isConnected;

    private EditText editTextStudentID;
    private Intent intent;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        editTextStudentID = (EditText) findViewById(R.id.editTextStudentID);

        if(!isConnected){
            Toast.makeText(getApplicationContext(), "No network connection", Toast.LENGTH_LONG).show();
        }
    }

    public void proceed(View v){
        String studentID;
        studentID = String.valueOf(editTextStudentID.getText());
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("studentID", studentID);
        editor.apply();

        intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
