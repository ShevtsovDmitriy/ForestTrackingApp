package com.example.foresttrackingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class MainActivity extends ActionBarActivity {

    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (TextView) findViewById(R.id.text_view_info);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    MyTask task;
    public void doSomething(View v){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()){
            System.out.print("\n Что-то происходит \n");
        }
        else {
            System.out.print("\n Что-то не то происходит \n");
        }
        task = new MyTask();
        task.execute();
    }

    class MyTask extends AsyncTask<Void, Void, String> {
        public MyTask() {
            super();
        }
        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&sensor=true");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.connect();
                int response = connection.getResponseCode();
                Log.d("i don`t know...", "The response is: " + response);
                InputStream is = connection.getInputStream();
                Scanner scanner = new Scanner(is);
                String str = "";
                str = scanner.nextLine();
                while(scanner.hasNext()) {
                    str = str.concat(scanner.nextLine());
                }
                System.out.print("\n" + str + "\n");
                JSONObject object = new JSONObject(str);
                JSONArray names = object.names();
                return str;
            } catch (java.io.IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "notWorking";
        }

        @Override
        protected void onPostExecute(String str) {
            info.setText(str);
        }
    }


}
