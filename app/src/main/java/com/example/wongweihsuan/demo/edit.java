package com.example.wongweihsuan.demo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class edit extends AppCompatActivity {

    public Spinner s1, s2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        s1=findViewById(R.id.statespinner1);
        s2=findViewById(R.id.statespinner1);

        //address1 state
        ArrayAdapter<CharSequence> nAdapter = ArrayAdapter.createFromResource(
                this,R.array.state,android.R.layout.simple_spinner_item
        );
        nAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(nAdapter);

        //address2 state
        ArrayAdapter<CharSequence> nAdapter1 = ArrayAdapter.createFromResource(
                this,R.array.state,android.R.layout.simple_spinner_item
        );
        nAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(nAdapter1);

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = s1.getSelectedItem().toString();
                editor.putString("state1",text);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = s1.getSelectedItem().toString();
                editor.putString("state2",text);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onClickGoBack(View v){

        Intent intent = new Intent(
                this,profile.class);
        finish();
        startActivity(intent);
    }

    public void deliverdatatoserver(View v) throws ExecutionException, InterruptedException {

        final JSONObject postData = new JSONObject();
        try {
           //put the data in the postData

            postData.put("", "");
            postData.put("", "");
            postData.put("", "");
            postData.put("", "");
            postData.put("", "");
            postData.put("", "");
            


        } catch (JSONException e) {

            e.printStackTrace();

        }
        if (postData.length() > 0) {

            new edit.PutJsonDataToServer().execute(String.valueOf(postData));

            //Perform the doInBackground method, passing in our url

        }

    }


    //put
    public class PutJsonDataToServer extends AsyncTask<String, String, String>{

        @Override
        public String doInBackground(String... params) {

            String JsonResponse = null;
            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            int statusCode = 0;

            try {
                URL url = new URL("http://dev.vatm.io:8080/profile");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json");

                //save request time
                SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                String token = shared.getString("token","");
                editor.commit();

                urlConnection.setRequestProperty("Authorization", token);
                Log.i("tag",token);
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
// json data
                writer.close();

                InputStream inputStream = urlConnection.getInputStream();
//input stream
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)

                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = buffer.toString();
//response data
                Log.i("TAG", JsonResponse);

                try {
//send to post execute
                    return JsonResponse;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            } catch (IOException e) {

                e.printStackTrace();


            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();

                }
                if (reader != null) {
                    try {
                        reader.close();

                    } catch (final IOException e) {
                        Log.e("TAG", "Error closing stream", e);
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {

        }
    }

}
