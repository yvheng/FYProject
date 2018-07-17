package com.example.family.fyproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class TranslateActivity extends AppCompatActivity {
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    Boolean isConnected;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    Word word = new Word();
    ResponseDB response = new ResponseDB();

    TextView textViewWord;
    EditText editTextAnswer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        textViewWord = (TextView)findViewById(R.id.textViewWord);
        editTextAnswer = (EditText)findViewById(R.id.editTextAnswer);

        if(isConnected){
            readWord();
            refresh();
        }else{
            Toast.makeText(getApplicationContext(),"No network connection.", Toast.LENGTH_LONG).show();
        }
    }

    public void refresh(){
        textViewWord.setText("ID :"+String.valueOf(word.getID())+", "+String.valueOf(word.getOriginal()));
        editTextAnswer.setText("");
    }

    public void updateStatus(int editing, Word word){
        String url = getApplicationContext().getString(R.string.editingStatusURL);
        makeServiceCallEditing(this, url, word, editing);
    }

    public void readWord() {
        try {
            // Check availability of network connection.
            if (isConnected) {
                getWord(getApplicationContext(), getResources().getString(R.string.selectWordURL));
                updateStatus(1,word);
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

    public void getWord(Context context, String url) {

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject recordResponse = (JSONObject) response.get(0);
                            int ID = Integer.parseInt(recordResponse.getString("ID"));
                            String original = recordResponse.getString("OriginalW");
                            int editing = Integer.parseInt(recordResponse.getString("Editing"));
                            int translated = Integer.parseInt(recordResponse.getString("Translated"));
                            int checked = Integer.parseInt(recordResponse.getString("Checked"));

                            /*
                            word.setID(ID);
                            word.setOriginal(original);
                            word.setTranslatedW(translatedW);
                            word.setCheckedW(checkedW);
                            word.setEditing(editing);
                            word.setTranslated(translated);
                            word.setChecked(checked);
                            word.setStudentID(studentID);
                            */
                            word = new Word(ID, original, translated, checked, editing);
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

    public void makeServiceCallTranslate(Context context, String url, final ResponseDB response) {
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
                    params.put("TranslatedW", response.getTranslatedW());
                    params.put("StudentIDT", response.getStudentIDT());

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

    public void nextWord(View v) {
        Word lastWord = new Word(word);
        //word.clear();

        readWord();
        refresh();

        updateStatus(0,lastWord);
    }

    @Override
    public void onBackPressed() {
        updateStatus(0,word);
        super.onBackPressed();
    }

    public void insertResponse(){
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String studentID = prefs.getString("studentID", "No value"); //No value is default value
        response.setID(word.getID());
        response.setStudentIDT(studentID);
        String url = getApplicationContext().getString(R.string.insertTranslateURL);
        makeServiceCallTranslate(this, url, response);
        url = getApplicationContext().getString(R.string.updateWordURL);
        word.setTranslated(1);
        makeServiceCallUpdateWord(this, url, word);
    }

    public void finish(View v){
        String answer = String.valueOf(editTextAnswer.getText());
        response.setTranslatedW(answer);

        insertResponse();

        readWord();
        refresh();
    }
}
