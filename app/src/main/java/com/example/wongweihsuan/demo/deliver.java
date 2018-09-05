package com.example.wongweihsuan.demo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static android.os.AsyncTask.SERIAL_EXECUTOR;
import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

public class deliver extends AppCompatActivity {
public TextView transactionAmount, cashbackAmount, transactionFee, total, timerequest, transactionid, process, userid, phnumber, funame;
//public TextView r,tid;
Button deliver;

    android.os.Handler mHandler;
    public BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //GET REQUEST

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver);



        transactionAmount=findViewById(R.id.transaction);
        cashbackAmount=findViewById(R.id.cashback);
        transactionFee=findViewById(R.id.fee);
        total=findViewById(R.id.total);
        timerequest=findViewById(R.id.time);
        transactionid=findViewById(R.id.transactionid);
        process=findViewById(R.id.processing);
        deliver=findViewById(R.id.deliver);
        //
        userid=findViewById(R.id.userId);
        funame=findViewById(R.id.username);
        phnumber=findViewById(R.id.phoneNumber);
        //

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override


            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);
                String timeDeliver = shared.getString("timeDelivered", null);

                switch(item.getItemId())
                {

                    case R.id.action_profile:

                Intent intent = new Intent(getApplicationContext(), profile.class);
                finish();
                startActivity(intent);
                return true;

                    case R.id.action_transaction:
                        if(timeDeliver != null && timeDeliver.length() > 5) {
                            Intent n = new Intent(deliver.this, transaction.class);
                            finish();
                            startActivity(n);
                            return true;
                        }else{
                            Toast.makeText(deliver.this,"Waiting for the Vendor...",Toast.LENGTH_SHORT).show();
                            return true;
                        }

                    case R.id.action_logoff:

                        if(timeDeliver != null && timeDeliver.length() > 5) {
                            SharedPreferences.Editor editor = shared.edit();

                            editor.clear();
                            editor.apply();
                            Intent m = new Intent(deliver.this, login.class);
                            finish();
                            startActivity(m);
                            return true;
                        }else{
                            Toast.makeText(deliver.this,"Waiting for the Vendor...",Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    default:
                        return deliver.super.onOptionsItemSelected(item);
                }

            }
        });

        SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);
        Integer Status = shared.getInt("status",0);
        Log.i("status","The status is"+Status.toString());
            if(Status==0) {
                //Customer
                //


                Integer userId = shared.getInt("userId", 0);
                String firstName = shared.getString("firstName", "");
                String lastName = shared.getString("lastName", "");
                String phoneNumber = shared.getString("phoneNumber", "");
                //
                String transid = shared.getString("transactionId", "");
                String transAmount = shared.getString("transactionAmount", "");
                String cashBack = shared.getString("cashBackAmount", "");
                String transFee = shared.getString("transactionFee", "");
                String Total = shared.getString("total", "");
                String timeRequest = shared.getString("timeRequested", "no save");
                String timeDeliver = shared.getString("timeDelivered", null);

                transactionid.setText(transid);
                transactionAmount.setText(transAmount);
                cashbackAmount.setText(cashBack);
                transactionFee.setText(transFee);
                total.setText(Total);
                timerequest.setText(timeRequest);
                userid.setText(Integer.toString(userId));
                funame.setText(lastName+" "+firstName);
                phnumber.setText(phoneNumber.toString());

                //get isVendor
                Integer status = shared.getInt("status", 0);

                process.setVisibility(View.VISIBLE);
                if (timeDeliver != null && timeDeliver.length() > 5) {
                    process.setText("Delivered!");
                    this.mHandler = new Handler();
                    super.onPause();
                    if (mHandler != null) {
                        mHandler.removeCallbacks(m_Runnable);
                    }

                    } else {

                        this.mHandler = new Handler();
                        m_Runnable.run();
                        process.setText("Pending...");
                    }



            }else{
                //Vendor
                deliver.setVisibility(View.VISIBLE);

                Integer userId = shared.getInt("userId", 0);
                String firstName = shared.getString("userFirstName", "");
                String lastName = shared.getString("userLastName", "");
                String phoneNumber = shared.getString("userPhoneNumber", "");
                String transid = shared.getString("transactionId", "");
                String transAmount = shared.getString("transactionAmount", "");
                String cashBack = shared.getString("cashBackAmount", "");
                String transFee = shared.getString("transactionFee", "");
                String Total = shared.getString("total", "");
                String timeRequest = shared.getString("timeRequested", "no save");
                String timeDeliver = shared.getString("timeDelivered", null);

                Log.i("firstname","first name is"+firstName);

                transactionid.setText(transid);
                transactionAmount.setText(transAmount);
                cashbackAmount.setText(cashBack);
                transactionFee.setText(transFee);
                total.setText(Total);
                timerequest.setText(timeRequest);
                userid.setText(userId.toString());
                funame.setText(firstName+" "+lastName);
                phnumber.setText(phoneNumber.toString());

            }
    }



    //refresh
    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {


            deliver.this.mHandler.postDelayed(m_Runnable,3000);

            new deliver.HttpGetRequest().execute();

            SharedPreferences shared1 = getSharedPreferences("info", Context.MODE_PRIVATE);
            String timeDeliver = shared1.getString("timeDelivered",null);
            if(timeDeliver!=null&&timeDeliver.length()>5){
                process.setText("Delivered!");
            }else{
                process.setText("Pending...");
            }

        }

    };

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mHandler != null) {
            mHandler.removeCallbacks(m_Runnable);
        }
    }

////put
    public void deliverdatatoserver(View v) throws ExecutionException, InterruptedException {
        // put request current time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String delivertime = dateFormat.format(date);
        //save request time
        SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("timeDelivered",delivertime);
        editor.commit();
        final JSONObject postData = new JSONObject();
        try {

            String transID = shared.getString("transactionId","");
            String transAmount = shared.getString("transactionAmount","");
            String cashBack = shared.getString("cashBackAmount","");
            String transFee = shared.getString("transactionFee","");
            String Total=shared.getString("total","");
            String  timeDeliver= shared.getString("timeDelivered","no save");
            String requesttime = shared.getString("timeRequested","no save");
            String confirmationCode =shared.getString("confirmationCode","");
            Integer cancel = shared.getInt("isCancelled",0);
            Log.i("tag",transID);
            Log.i("tag",transAmount);
            Log.i("tag",cashBack);
            Log.i("tag",transFee);
            Log.i("tag",Total);
            Log.i("tag",requesttime);
            Log.i("tag",timeDeliver);
            Log.i("tag",confirmationCode);
                postData.put("transactionId", transID);
                postData.put("transactionAmount", transAmount);
                postData.put("cashBackAmount", cashBack);
                postData.put("transactionFee", transFee);
                postData.put("total", Total);
                postData.put("timeRequested", requesttime);
                postData.put("timeDelivered", timeDeliver);
                postData.put("confirmationCode", confirmationCode);
                postData.put("isCancelled", false);
Log.i("tag",String.valueOf(postData));

        } catch (JSONException e) {

            e.printStackTrace();

        }
        if (postData.length() > 0) {

            new deliver.PutJsonDataToServer().execute(String.valueOf(postData));
            String  timeDeliver= shared.getString("timeDelivered","no save");
            if(timeDeliver!=null){
                Toast.makeText(deliver.this,"Successfully Delivered !",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, profile.class);
                startActivity(i);


            }else{
                Toast.makeText(deliver.this,"Deliverer fail!",Toast.LENGTH_SHORT).show();

            }


        }

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
            if(isVendor==0) {
                connection.setRequestProperty("isVendor", "false");
                connection.setRequestProperty("transactionId", tranid);
            }else{
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
                    String request=requested.substring(0,10)+" "+requested.substring(11,19);

                    String delivered = myResponse.getString("timeDelivered");
                    String confirm = myResponse.getString("confirmationCode");
                    Integer cancel = myResponse.getInt("isCancelled");
                    Log.i("tag",result);
                    Log.i("tag",delivered);

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
                URL url = new URL("http://dev.vatm.io:8080/transaction");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            SharedPreferences shared = getSharedPreferences("info", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            String token = shared.getString("token","");
            editor.commit();

            urlConnection.setRequestProperty("Authorization", token);

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
