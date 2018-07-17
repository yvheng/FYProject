package com.example.family.fyproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.family.fyproject.Model.ResponseDB.ResponseDB;
import com.example.family.fyproject.Model.Word.Word;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.family.fyproject.TranslateActivity.MY_PREFS_NAME;

public class CheckingActivity extends AppCompatActivity {
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    Boolean isConnected;
    Word word = new Word();
    ResponseDB responseDB = new ResponseDB();
    static final int request_code = 12345;

    private Intent intent;
    private TextView textViewOriginal, textViewAnswer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking);
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        textViewOriginal = (TextView)findViewById(R.id.textViewOriginal);
        textViewAnswer = (TextView)findViewById(R.id.textViewAnswer);

        if(isConnected){
            readResponse();
            refresh();

            textViewOriginal.setText(word.getOriginal());
        }else{
            Toast.makeText(getApplicationContext(),"No network connection.", Toast.LENGTH_LONG).show();
        }
    }

    public void refresh(){
        textViewOriginal.setText(""+String.valueOf(word.getOriginal()));
        if(responseDB.getTranslatedW()==null||responseDB.getTranslatedW()==""){
            textViewAnswer.setText("No answer yet.");
        }else{
            textViewAnswer.setText(""+responseDB.getTranslatedW());
        }
    }

    public void updateStatus(int editing, Word word){
        String url = getApplicationContext().getString(R.string.editingStatusURL);
        makeServiceCallEditing(this, url, word, editing);
    }

    public void readResponse(){
        try {
            // Check availability of network connection.
            if (isConnected) {
                getResponse(getApplicationContext(), getResources().getString(R.string.selectResponseURL));
                updateStatus(1, word);
            } else {
                Toast.makeText(getApplicationContext(), "Network is NOT available",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "Error reading record:" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void getResponse(Context context, String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject recordResponse = (JSONObject) response.get(0);
                            int ID = Integer.parseInt(recordResponse.getString("ID"));
                            String originalW = recordResponse.getString("OriginalW");
                            String translatedW = recordResponse.getString("TranslatedW");
                            String studentIDT = recordResponse.getString("StudentIDT");

                            responseDB = new ResponseDB(ID, translatedW, null, studentIDT, null);
                            word = new Word(responseDB.getID(), originalW, 1, 0, 1);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error 1:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error 2:" + volleyError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void makeServiceCallCheck(Context context, String url, final ResponseDB response) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Successfully updated.", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("ID", String.valueOf(response.getID()));
                    params.put("CheckedW", response.getCheckedW());
                    params.put("StudentIDC", response.getStudentIDC());

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeServiceCallEditing(Context context, String url, final Word word, final int editing) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //do something after done
                            //Toast.makeText(getApplicationContext(), "Status updated. Editing:"+, Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("ID", String.valueOf(word.getID()));
                    params.put("Editing", String.valueOf(editing));

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeServiceCallUpdateWord(Context context, String url, final Word word) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //do something after done
                            //Toast.makeText(getApplicationContext(), "Status updated. Editing:"+, Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("ID", String.valueOf(word.getID()));
                    params.put("Translated", String.valueOf(word.getTranslated()));
                    params.put("Checked", String.valueOf(word.getChecked()));

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        updateStatus(0,word);
        super.onBackPressed();
    }

    public void insertResponse(){
        //insert new response data and update current word data
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String studentID = prefs.getString("studentID", "No value"); //No value is default value
        responseDB.setID(word.getID());
        responseDB.setStudentIDC(studentID);
        String url = getApplicationContext().getString(R.string.updateCheckURL);
        makeServiceCallCheck(this, url, responseDB);
        url = getApplicationContext().getString(R.string.updateWordURL);
        word.setChecked(1);
        makeServiceCallUpdateWord(this, url, word);
    }

    @Override
    protected void onResume() {
        readResponse();
        super.onResume();
    }

    public void nextWord(View v) {
        Word lastWord = new Word(word);

        readResponse();
        refresh();

        updateStatus(0, lastWord);
    }

    public void enterCheck(View v){
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("ID", responseDB.getID());
        editor.putString("originalW", word.getOriginal());
        editor.putString("translatedW", responseDB.getTranslatedW());
        editor.apply();

        intent = new Intent(this, ConfirmCheckActivity.class);
        startActivityForResult(intent, request_code);
    }

    public void finishCheck(View v){
        responseDB.setCheckedW(responseDB.getTranslatedW());

        insertResponse();
    }
}
