package com.example.wongweihsuan.demo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.santalu.maskedittext.MaskEditText;

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
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public EditText fName, lName, mail, pass;
    public EditText phoneNum;
    public MaskEditText editText_phonenumber;
//    public TextView r;
    public ProgressBar simpleProgressBar;
    String FirstName;
    String LastName;
    String Email;
    String PhoneNumber;
    String Password;
    String maintoken;
    private ProgressDialog mProgressBar;




    //Perform the doInBackground method, passing in our url
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        mail = findViewById(R.id.email);
//        phoneNum = findViewById(R.id.phone);
        pass = findViewById(R.id.password);
//        r = findViewById(R.id.result);
        editText_phonenumber=findViewById(R.id.phone);

//        simpleProgressBar = findViewById(R.id.progressBar);
//        RadioButton customer = findViewById(R.id.radioButto_customer);
//        RadioButton vender = findViewById(R.id.radioButton_vender);
//        CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                switch (buttonView.getId()) {
//                    case R.id.radioButto_customer:
//                        SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = shared.edit();
//                        editor.putBoolean("isVendor",true);
//                        editor.commit();
//                        break;
//                    case R.id.radioButton_vender:
//                        SharedPreferences shared1 = getSharedPreferences("info", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor1 = shared1.edit();
//                        editor1.putBoolean("isVendor",false);
//                        editor1.commit();
//                        break;
//                }
//            }
//        };
//
//        customer.setOnCheckedChangeListener(mOnCheckedChangeListener);
//        vender.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }


    //format
    public static boolean isMailValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat1 = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat1.matcher(email).matches();
    }

    public static boolean isPhoneValid(String phone)
    {
        String phoneRegex = "^([0-9]{3})?([0-9]{3})?([0-9]{4})$";

        Pattern pat2 = Pattern.compile(phoneRegex);
        if (phone == null)
            return false;
        return pat2.matcher(phone).matches();
    }

    public static boolean isfNameValid(String fName)
    {
        String fNameRegex = "^([a-zA-Z0-9\\s-]{1,50})$";

        Pattern pat3 = Pattern.compile(fNameRegex);
        if (fName == null)
            return false;
        return pat3.matcher(fName).matches();
    }

    public static boolean islNameValid(String lName)
    {
        String lNameRegex = "^([a-zA-Z0-9\\s-]{1,50})$";

        Pattern pat4 = Pattern.compile(lNameRegex);
        if (lName == null)
            return false;
        return pat4.matcher(lName).matches();
    }

    public static boolean ispassValid(String pass)
    {
        String phoneRegex = "^([a-zA-Z0-9]{8,30})$";

        Pattern pat5 = Pattern.compile(phoneRegex);
        if (pass == null)
            return false;
        return pat5.matcher(pass).matches();
    }

    public void senddatatoserver(View v) throws ExecutionException, InterruptedException {
        FirstName = fName.getText().toString();
        LastName = lName.getText().toString();
        Email = mail.getText().toString().trim();
        PhoneNumber = editText_phonenumber.getText().toString().trim().replaceAll("[^\\w\\s]","");
        Password = pass.getText().toString().trim();
        Long phonenum = Long.parseLong(PhoneNumber);
        final JSONObject postData = new JSONObject();
        try {
            SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);
//            Boolean isVendor = true;
            if (!(isMailValid(Email))||Email==null) {
                Toast.makeText(getApplicationContext(),"Email format is not correct or empty",Toast.LENGTH_LONG).show();
            }  else if(!(isPhoneValid(PhoneNumber))||PhoneNumber==null){
                Toast.makeText(getApplicationContext(),"Phone Number format is not correct or empty",Toast.LENGTH_LONG).show();
            } else if(!(isfNameValid(FirstName))||FirstName==null){
                Toast.makeText(getApplicationContext(),"First Name format is not correct or empty",Toast.LENGTH_LONG).show();
            } else if(!(islNameValid(LastName))||LastName==null){
                Toast.makeText(getApplicationContext(),"Last Name format is not correct or empty",Toast.LENGTH_LONG).show();
            } else if(!(ispassValid(Password))||Password==null){
                Toast.makeText(getApplicationContext(),"Password format is not correct or less than 8 digits or empty",Toast.LENGTH_LONG).show();
            }else{//add condition for isVendor
                postData.put("email", mail.getText().toString().trim());
                postData.put("firstName", fName.getText().toString());
                postData.put("lastName", lName.getText().toString());
                postData.put("phoneNumber",phonenum);
                postData.put("password", pass.getText().toString().trim());
                postData.put("isVendor", false);
                Log.i("tag",postData.toString());
            }
        } catch (JSONException e) {

            e.printStackTrace();

        }
        if (postData.length() > 0) {

            new SendJsonDataToServer().execute(String.valueOf(postData));
            new HttpGetRequest().execute();
//            Intent i = new Intent(MainActivity.this, profile.class);
//            finish();
//            startActivity(i);


        }
    }


    public void onClickGoBack(View v){

        Intent intent = new Intent(
                this,login.class);
        finish();
        startActivity(intent);
    }


    public class SendJsonDataToServer extends AsyncTask<String, String, String>{

        @Override
        public String doInBackground(String... params) {

            String JsonResponse = null;
            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            int statusCode = 0;

            try {
                URL url = new URL("http://dev.vatm.io:8080/signup");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");


                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");

//set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
// json data
                writer.close();



                maintoken = urlConnection.getHeaderField("authorization");

                SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();

                editor.putString("token",maintoken);
                editor.commit();

//                String token = shared.getString("token","");
//                Log.i("tag",token);

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

    //get request
    public class HttpGetRequest extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... params){

            String result;
            String inputLine;

            try {
                //Create a URL object holding our url
                URL myUrl = new URL("http://dev.vatm.io:8080/profile");
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();


                //Set methods and timeouts
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);
                String token = shared.getString("token","");
                connection.setRequestProperty("authorization", token);


                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
//                StringBuilder stringBuilder = new StringBuilder();
                StringBuffer stringBuilder = new StringBuffer();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
                int serverstatusget = connection.getResponseCode();
                String serverstat = Integer.toString(serverstatusget);
                Log.i("tag",serverstat);
                if(serverstatusget==200) {
//                    r.setText(result);

                    //Read JSON response and print
                    JSONObject myResponse = null;
                    try {
                        myResponse = new JSONObject(stringBuilder.toString());
                        String f = myResponse.getString("firstName");
                        String l = myResponse.getString("lastName");
                        String p = myResponse.getString("phoneNumber");
                        String e = myResponse.getString("email");
                        Integer s = myResponse.getInt("isVendor");

                        SharedPreferences loginshared = getSharedPreferences("info", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = loginshared.edit();

                        editor.putString("firstName",f);
                        editor.putString("lastName",l);
                        editor.putString("phoneNumber",p);
                        editor.putString("email",e);
                        editor.putInt("status",s);
                        editor.apply();


                        Intent i = new Intent(MainActivity.this, profile.class);
                        finish();
                        startActivity(i);


                    } catch (JSONException e) {
                        e.printStackTrace();
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










