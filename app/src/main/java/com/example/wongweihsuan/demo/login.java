
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
        import android.telephony.PhoneNumberUtils;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.EditText;
        import android.widget.RadioButton;
        import android.widget.TextView;
        import android.widget.Toast;


        import com.santalu.maskedittext.MaskEditText;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.BufferedWriter;
        import java.io.DataOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;
        import java.io.Writer;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.util.Base64;
        import java.util.concurrent.ExecutionException;
        import java.util.regex.Pattern;

        import javax.net.ssl.HttpsURLConnection;

public class login extends AppCompatActivity {
    public EditText phoneNum, pass;
    public Button Login, Register;
    public MaskEditText editText_phonenumber;
    String PhoneNumber;
    String Password;
    String maintoken;




    //Perform the doInBackground method, passing in our url
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editText_phonenumber=findViewById(R.id.phonenum);
//        phoneNum = findViewById(R.id.phonenum);
        pass = findViewById(R.id.password);
        Login=findViewById(R.id.login);
        Register=findViewById(R.id.register);

    }





    public class SendJsonDataToServer extends AsyncTask<String, String, String> {

        @Override
        public String doInBackground(String... params) {
            String JsonResponse = null;
            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            int statusCode = 0;

            try {
                URL url = new URL("http://dev.vatm.io:8080/login");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");


                urlConnection.setRequestProperty("Content-Type", "application/json");


//set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
// json data
                writer.close();



                maintoken = urlConnection.getHeaderField("authorization");

                SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                int serverstatus = urlConnection.getResponseCode();
                String maintoken = urlConnection.getHeaderField("authorization");
                Log.i("tagserver",Integer.toString(serverstatus));
                editor.putInt("serverstatuslogin",serverstatus);
                editor.putString("token",maintoken);
                editor.commit();


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


        private ProgressDialog dialog = new ProgressDialog(login.this);

        @RequiresApi(api = Build.VERSION_CODES.O)
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @TargetApi(Build.VERSION_CODES.O)
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
                Log.i("1","1");

                //Set methods and timeouts
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);
                String token = shared.getString("token","");
                connection.setRequestProperty("authorization", token);

                Log.i("2","2");
                //Connect to our url
                connection.connect();

                Log.i("3","3");

                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                Log.i("4","4");
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                Log.i("5","5");
//                StringBuilder stringBuilder = new StringBuilder();
                StringBuffer stringBuilder = new StringBuffer();
                //Check if the line we are reading is not null

                Log.i("6","6");

                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                Log.i("7","7");
                result = stringBuilder.toString();

                Log.i("8","8");

                int serverstatusget = connection.getResponseCode();
                String serverstat = Integer.toString(serverstatusget);
                Log.i("getserverstat",serverstat);
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

                            Intent i = new Intent(login.this, profile.class);
                            finish();
                            startActivity(i);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(login.this,
                            "Account is not correct! Please register for free.", Toast.LENGTH_LONG).show();
                }
            }
            catch(IOException e){
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                e.printStackTrace();
                result = null;
            }
            return result;
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        protected void onPostExecute(String result){
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            super.onPostExecute(result);
        }
    }
    public void onClickStartNewActivity(View v){

        Intent intent = new Intent(
                this,MainActivity.class);
        startActivity(intent);

    }

    public void senddatatoserver(View v) throws ExecutionException, InterruptedException {

        PhoneNumber = editText_phonenumber.getText().toString().trim().replaceAll("[^\\w\\s]","");
        Password = pass.getText().toString();
//        Long phone = Long.parseLong(PhoneNumber);
        final JSONObject postData = new JSONObject();
        try {


            if (PhoneNumber==null) {
                Toast.makeText(getApplicationContext(),"PhoneNumber is empty",Toast.LENGTH_LONG).show();
            }  else if(Password==null){
                Toast.makeText(getApplicationContext(),"Password is empty",Toast.LENGTH_LONG).show();
            } else{//add condition for isVendor

                postData.put("phoneNumber", PhoneNumber);
                postData.put("password", Password);
            }

        } catch (JSONException e) {

            e.printStackTrace();

        }
        if (postData.length() > 0) {
            new SendJsonDataToServer().execute(String.valueOf(postData));
//            SharedPreferences shared1 = getSharedPreferences("info", Context.MODE_PRIVATE);
//            Integer server=shared1.getInt("serverstatuslogin",0);
//            if(server==200) {
                new login.HttpGetRequest().execute();
//            }else{
//                Toast.makeText(getApplicationContext(),"Account is not correct!",Toast.LENGTH_LONG).show();
//            }



        }

    }
}










