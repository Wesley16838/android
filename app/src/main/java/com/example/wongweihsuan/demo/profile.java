package com.example.wongweihsuan.demo;



import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;

public class profile extends AppCompatActivity {
    public BottomNavigationView bottomNavigationView;
    public TextView fName, eMail, pNumber,status;
    public Button editB;
//    public TextView re;


    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);




        fName=findViewById(R.id.textView_fName);
        editB=findViewById(R.id.button_edit);
        eMail=findViewById(R.id.textView_email);
        pNumber=findViewById(R.id.textView_pNumber);
        status=findViewById(R.id.Status);




            SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);

            String FirstName = shared.getString("firstName","");
            String LastName = shared.getString("lastName","");
            String PhoneNumber = shared.getString("phoneNumber","");
            String Email = shared.getString("email","");
            Integer Status = shared.getInt("status",0);

            if(Status==0) {
                fName.setText(FirstName + " " + LastName);
                status.setText("Customer");
                pNumber.setText(PhoneNumber);
                eMail.setText(Email);
            }else{
                fName.setText(FirstName + " " + LastName);
                status.setText("Vendor");
                pNumber.setText(PhoneNumber);
                eMail.setText(Email);
            }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override


            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);
                Integer Status = shared.getInt("status",0);
                String timeDeliver = shared.getString("timeDelivered", null);
                String transid = shared.getString("transactionId","");
                switch(item.getItemId())
                {

                    case R.id.action_profile:
                        // Intent intent = new Intent(getApplicationContext(),profile.class);
                        //startActivity(intent);
                        return true;

                    case R.id.action_transaction:
                        if(Status==0) {

                            Intent n = new Intent(profile.this, transaction.class);
                            finish();
                            startActivity(n);
                            return true;

                        }else{

                            new profile.HttpGetRequest().execute();
                            Intent n = new Intent(profile.this, deliver.class);
                            finish();
                            startActivity(n);
                            return true;

                        }
                    case R.id.action_logoff:


                        SharedPreferences.Editor editor = shared.edit();

                        editor.clear();
                        editor.commit();

                        Intent m = new Intent(profile.this, login.class);
                        finish();
                        startActivity(m);
                        return true;
                    default:
                        return profile.super.onOptionsItemSelected(item);
                }

            }
        });

    }


    public void onClickStartNewActivity(View v){
        Toast.makeText(profile.this,"Edit Address Coming soon",Toast.LENGTH_SHORT).show();
    }

    //get
//get request
    public class HttpGetRequest extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... params){

            String result;
            String inputLine;

            try {
                //Create a URL object holding our url
                URL myUrl = new URL("http://dev.vatm.io:8080/transaction");
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();


                //Set methods and timeouts
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);

                //get token and transactionId


                String token = shared.getString("token","no save");
                connection.setRequestProperty("Authorization", token);
                String tranid = shared.getString("transactionId", "");
                Integer isVendor=shared.getInt("status",0);
                if(isVendor==0) {//customer
                    connection.setRequestProperty("isVendor", "false");
                    connection.setRequestProperty("transactionId", tranid);
                }else{//vendor
                    connection.setRequestProperty("isVendor", "true");
                    connection.setRequestProperty("transactionId", "null");
                }

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
                int serverstatus = connection.getResponseCode();
                if(serverstatus==200) {

                    if(isVendor==0){//customer
                        //Read JSON response and print

                    JSONObject myResponse = null;
                    try {
                        myResponse = new JSONObject(stringBuilder.toString());
                        //
                        Integer userId = myResponse.getInt("userId");
                        String firstName = myResponse.getString("firstName");
                        String lastName = myResponse.getString("lastName");
                        Long phoneNumber = myResponse.getLong("phoneNumber");
                        String phonen=Long.toString(phoneNumber);
                        //
                        String id = myResponse.getString("transactionId");
                        String transactionAmount = myResponse.getString("transactionAmount");
                        String cashback = myResponse.getString("cashBackAmount");
                        String fee = myResponse.getString("transactionFee");
                        String total = myResponse.getString("total");
                        String requested = myResponse.getString("timeRequested");
                        String delivered = myResponse.getString("timeDelivered");
                        String confirm = myResponse.getString("confirmationCode");
                        Integer cancel = myResponse.getInt("isCancelled");
                        String request=requested.substring(0,10)+" "+requested.substring(11,19);
                        Log.i("tag",result);
                        Log.i("tag",delivered);
                        Log.i("tag",request);
                        SharedPreferences shared1 = getSharedPreferences("info", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = shared1.edit();

                        editor.putInt("userId",userId);
                        editor.putString("firstName",firstName);
                        editor.putString("lastName",lastName);
                        editor.putString("phoneNumber",phonen);
                        editor.putString("transactionId",id);
                        editor.putString("transactionAmount",transactionAmount);
                        editor.putString("cashBackAmount",cashback);
                        editor.putString("transactionFee",fee);
                        editor.putString("total",total);
                        editor.putString("timeRequested",request);
                        editor.putString("timeDelivered",delivered);
                        editor.putString("confirmationCode",confirm);
                        editor.putInt("isCancelled",cancel);
                        editor.apply();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }else{//vendor

                        //Read JSON response and print
                        JSONObject myResponse = null;
                        try {
                            myResponse = new JSONObject(stringBuilder.toString());
                            //
                            Integer userId = myResponse.getInt("userId");
                            String firstName = myResponse.getString("firstName");
                            String lastName = myResponse.getString("lastName");
                            Long phoneNumber = myResponse.getLong("phoneNumber");
                            String phonen=Long.toString(phoneNumber);
                            //
                            String id = myResponse.getString("transactionId");
                            String transactionAmount = myResponse.getString("transactionAmount");
                            String cashback = myResponse.getString("cashBackAmount");
                            String fee = myResponse.getString("transactionFee");
                            String total = myResponse.getString("total");
                            String requested = myResponse.getString("timeRequested");
                            String delivered = myResponse.getString("timeDelivered");
                            String confirm = myResponse.getString("confirmationCode");
                            Integer cancel = myResponse.getInt("isCancelled");
                            String request=requested.substring(0,10)+" "+requested.substring(11,19);
                            Log.i("tag",result);
                            Log.i("tag",delivered);
                            Log.i("tag",request);
                            SharedPreferences shared1 = getSharedPreferences("info", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = shared1.edit();
//
                            editor.putInt("userId",userId);
                            editor.putString("userFirstName",firstName);
                            editor.putString("userLastName",lastName);
                            editor.putString("userPhoneNumber",phonen);
                            //
                            editor.putString("transactionId",id);
                            editor.putString("transactionAmount",transactionAmount);
                            editor.putString("cashBackAmount",cashback);
                            editor.putString("transactionFee",fee);
                            editor.putString("total",total);
                            editor.putString("timeRequested",request);
                            editor.putString("timeDelivered",delivered);
                            editor.putString("confirmationCode",confirm);
                            editor.putInt("isCancelled",cancel);
                            editor.apply();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }else{

                }
            }
            catch(IOException e){

                e.printStackTrace();
                result = null;
            }
            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }
}
