package com.example.wongweihsuan.demo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Window;
import android.view.WindowManager;
import android.app.ProgressDialog;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class transaction extends AppCompatActivity implements View.OnClickListener{
    public Button comfirm;
    private Button[] btn = new Button[4];
    public EditText inputcashback;
//    public TextView tid;
    private Button btn_unfocus;
    private int[] btn_id = {R.id.btn10,R.id.btn20,R.id.btn50,R.id.btn100};
    public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        inputcashback=findViewById(R.id.cashbackinput);
//        tid=findViewById(R.id.tranid);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        for(int i = 0; i < btn.length; i++){
            btn[i] = findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }
        btn_unfocus = btn[0];


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override


            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch(item.getItemId())
                {


                    case R.id.action_profile:

                        Intent intent = new Intent(getApplicationContext(),profile.class);
                        finish();
                        startActivity(intent);
                        return true;

                    case R.id.action_transaction:

                        Intent n = new Intent(transaction.this, transaction.class);
                        finish();
                        startActivity(n);
                        return true;

                    case R.id.action_logoff:

                        SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();
                        editor.clear();
                        editor.commit();
                        Intent m = new Intent(transaction.this, login.class);
                        finish();
                        startActivity(m);
                        return true;
                    default:
                        return transaction.super.onOptionsItemSelected(item);
                }

            }
        });
    }





    @Override
    public void onClick(View v) {
        SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
//setForcus(btn_unfocus, (Button) findViewById(v.getId()));
        //Or use switch
        switch (v.getId()){
            case R.id.btn10 :
                double cashback10 = 10.00;
                setFocus(btn_unfocus, btn[0]);
                String cash10 = String.valueOf(cashback10);
                editor.putString("cashback",cash10);
                editor.commit();
                inputcashback.setText("10");
                break;

            case R.id.btn20 :
                double cashback20 = 20.00;
                setFocus(btn_unfocus, btn[1]);
                String cash20 = String.valueOf(cashback20);
                editor.putString("cashback",cash20);
                editor.commit();
                inputcashback.setText("20");
                break;

            case R.id.btn50 :
                double cashback50 = 50.00;
                setFocus(btn_unfocus, btn[2]);
                String cash50 = String.valueOf(cashback50);
                editor.putString("cashback",cash50);
                editor.commit();
                inputcashback.setText("50");
                break;

            case R.id.btn100 :
                double cashback100 = 100.00;
                setFocus(btn_unfocus, btn[3]);
                String cash100 = String.valueOf(cashback100);
                editor.putString("cashback",cash100);
                editor.commit();
                inputcashback.setText("100");
                break;

        }
    }
    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.rgb(255, 255, 255));
        btn_unfocus.setBackgroundColor(Color.rgb(75, 98, 221));
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(Color.rgb(63, 81, 181));

        this.btn_unfocus = btn_focus;
    }

    public class HttpPostRequest extends AsyncTask<String, String, String>{

        @Override
        public String doInBackground(String... params) {
            String JsonResponse = null;
            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

//            // get request current time
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = new Date();
//            String requesttime = dateFormat.format(date);


            //save request time
            SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
//            editor.putString("timeRequested",requesttime);
            editor.commit();

            //take token
            String token = shared.getString("token","");

            try {
                URL url = new URL("http://dev.vatm.io:8080/transaction");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", token);

//set headers and method
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

                //save serverstatus
                int serverstatus = urlConnection.getResponseCode();
                Log.i("TAG", Integer.toString(serverstatus));

                //Read JSON response and print
if(serverstatus==200) {
    JSONObject myResponse;
    try {
        myResponse = new JSONObject(JsonResponse);
        String id = myResponse.getString("transactionId");

        Log.i("TAG", JsonResponse);
        Log.i("TAG", id);

        SharedPreferences shared2 = getSharedPreferences("info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = shared2.edit();
        editor2.putString("transactionId", id);
        editor2.apply();
        String transactionId = shared2.getString("transactionId", "");

        Log.i("TAG", transactionId);
    } catch (JSONException e) {
        e.printStackTrace();
    }
//response data
}
                try {
//send to post execute
                    return JsonResponse;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {

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
                URL myUrl = new URL("http://dev.vatm.io:8080/transaction");
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();

                //Set methods and timeouts
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);

                //get token and transactionId
                String tranid = shared.getString("transactionId","");
                Log.i("TAG", tranid);
                String token = shared.getString("token","no save");
                Log.i("TAG", token);
                Integer isVendor=shared.getInt("status",0);
                connection.setRequestProperty("Authorization", token);

                    connection.setRequestProperty("isVendor", "false");
                    connection.setRequestProperty("transactionId", tranid);

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
                    if(tranid!=null) {
                        //Read JSON response and print
                        JSONObject myResponse = null;
                        try {
                            myResponse = new JSONObject(stringBuilder.toString());
                            //
                            Integer userId = myResponse.getInt("userId");
                            String firstName = myResponse.getString("firstName");
                            String lastName = myResponse.getString("lastName");
                            Long phoneNumber = myResponse.getLong("phoneNumber");
                            String pnumber = Long.toString(phoneNumber);
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
                            Log.i("tag", result);
                            Log.i("tag", delivered);

                            SharedPreferences shared1 = getSharedPreferences("info", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = shared1.edit();
                            //
                            editor.putInt("userId", userId);
                            editor.putString("firstName", firstName);
                            editor.putString("lastName", lastName);
                            editor.putString("phoneNumber", pnumber);
                            //
                            editor.putString("transactionId", id);
                            editor.putString("transactionAmount", transactionAmount);
                            editor.putString("cashBackAmount", cashback);
                            editor.putString("transactionFee", fee);
                            editor.putString("total", total);
                            editor.putString("timeRequested", requested);
                            editor.putString("timeDelivered", delivered);
                            editor.putString("confirmationCode", confirm);
                            editor.putInt("isCancelled", cancel);
                            editor.apply();

                            Intent i = new Intent(transaction.this, deliver.class);
                            finish();
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        //Read JSON response and print
                        JSONObject myResponse = null;
                        try {
                            myResponse = new JSONObject(stringBuilder.toString());
                            //
                            Integer userId = myResponse.getInt("userId");
                            String firstName = myResponse.getString("firstName");
                            String lastName = myResponse.getString("lastName");
                            Long phoneNumber = myResponse.getLong("phoneNumber");
                            String pnumber = Long.toString(phoneNumber);
                            //
                            Integer id = myResponse.getInt("transactionId");
                            Integer transactionAmount = myResponse.getInt("transactionAmount");
                            Integer cashback = myResponse.getInt("cashBackAmount");
                            Integer fee = myResponse.getInt("transactionFee");
                            Integer total = myResponse.getInt("total");
                            String requested = myResponse.getString("timeRequested");
                            String delivered = myResponse.getString("timeDelivered");
                            String confirm = myResponse.getString("confirmationCode");
                            Integer cancel = myResponse.getInt("isCancelled");
                            Log.i("tag", result);
                            Log.i("tag", delivered);

                            SharedPreferences shared1 = getSharedPreferences("info", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = shared1.edit();
                            //
                            editor.putInt("userId", userId);
                            editor.putString("firstName", firstName);
                            editor.putString("lastName", lastName);
                            editor.putString("phoneNumber", pnumber);
                            //
                            editor.putString("transactionId", Integer.toString(id));
                            editor.putString("transactionAmount", Integer.toString(transactionAmount));
                            editor.putString("cashBackAmount", Integer.toString(cashback));
                            editor.putString("transactionFee", Integer.toString(fee));
                            editor.putString("total", Integer.toString(total));
                            editor.putString("timeRequested", requested);
                            editor.putString("timeDelivered", delivered);
                            editor.putString("confirmationCode", confirm);
                            editor.putInt("isCancelled", cancel);
                            editor.apply();

                            Intent i = new Intent(transaction.this, deliver.class);
                            finish();
                            startActivity(i);

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


    //get request
    public class HttpGetNullRequest extends AsyncTask<String, Void, String> {
        @RequiresApi(api = Build.VERSION_CODES.O)


        private ProgressDialog dialog = new ProgressDialog(transaction.this);

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
                URL myUrl = new URL("http://dev.vatm.io:8080/transaction");
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();


                //Set methods and timeouts
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);

                //get token and isVendor

                String token = shared.getString("token","no save");
                connection.setRequestProperty("Authorization", token);
                Integer isVendor=shared.getInt("status",0);

                connection.setRequestProperty("isVendor", "false");
                connection.setRequestProperty("transactionId", "null");


                //Connect to our url
                connection.connect();


                //save trans server status into sharepreference
                int serverstatus = connection.getResponseCode();
                SharedPreferences.Editor editor = shared.edit();
                editor.putInt("serverstatustrans",serverstatus);
                editor.commit();

                Integer server=shared.getInt("serverstatustrans",0);
                Log.i("line.514",Integer.toString(server));


                //Create a new InputStreamReader
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());

                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);

                StringBuilder stringBuilder = new StringBuilder();

                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null){



                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();

//                //save trans server status into sharepreference
//                int serverstatus = connection.getResponseCode();
//                SharedPreferences.Editor editor = shared.edit();
//                editor.putInt("serverstatustrans",serverstatus);



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

            SharedPreferences shared1 = getSharedPreferences("info", Context.MODE_PRIVATE);
            Integer server=shared1.getInt("serverstatustrans",0);
            Log.i("line.624",Integer.toString(server));
            if(server==200) {
                Intent i = new Intent(transaction.this, deliver.class);
                finish();
                startActivity(i);
            }else{
                Toast.makeText(getApplicationContext(),"Order some cash !",Toast.LENGTH_LONG).show();
            }

            super.onPostExecute(result);

        }

    }




    //send get request to server with null transId
    public void sentnull(View v){
        new HttpGetNullRequest().execute();
        //


    }






    //send get request to server with transId
    public void senddatatoserver(View v) throws ExecutionException, InterruptedException {
        SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);

        // get request current time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String requesttime = dateFormat.format(date);

        //save request time
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("timeRequested",requesttime);
        editor.commit();
        if(inputcashback!=null) {
        String input = inputcashback.getText().toString();

    int inputnumber = Integer.parseInt(input);
    if (inputnumber <= 200 && inputnumber > 0) {

        String cash = String.valueOf(inputnumber);
        editor.putString("input", cash);
        editor.commit();

        //take time from sharepreference
        String requestTime = shared.getString("timeRequested", "");
        String cashstring = shared.getString("input", "");
        double cashback = Double.parseDouble(cashstring);
        double total = 40 + 4 + cashback;


        JSONObject postData = new JSONObject();
        try {

            postData.put("transactionId", null);
            postData.put("transactionAmount", 40);
            postData.put("cashBackAmount", cashback);
            postData.put("transactionFee", 4);
            postData.put("total", total);
            postData.put("timeRequested", requestTime);
            postData.put("timeDelivered", null);
            postData.put("confirmationCode", null);
            postData.put("isCancelled", false);

        } catch (JSONException e) {

            e.printStackTrace();

        }
        if (postData.length() > 0) {

            new HttpPostRequest().execute(String.valueOf(postData));
            new HttpGetRequest().execute();
        }

    } else if (inputnumber > 200) {
        Toast.makeText(getApplicationContext(), "Amount must be less than 200.", Toast.LENGTH_LONG).show();
    } else if (inputnumber < 0) {
        Toast.makeText(getApplicationContext(), "Amount must be mroe than 0.", Toast.LENGTH_LONG).show();
    } else {
        Toast.makeText(getApplicationContext(), "Amount is not correct.", Toast.LENGTH_LONG).show();
    }
}else if(inputcashback==null){
    Toast.makeText(getApplicationContext(), "Please input number!", Toast.LENGTH_LONG).show();
}else{
            Toast.makeText(getApplicationContext(), "Please input number!", Toast.LENGTH_LONG).show();
        }


    }
}


