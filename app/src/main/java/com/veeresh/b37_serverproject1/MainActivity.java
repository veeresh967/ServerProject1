package com.veeresh.b37_serverproject1;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    //6. declare all variables
    TextView tv;
    Button b;
    MyTask m;

    //5. go to MainActivity java file, write Asynctask inner class
    public class MyTask extends AsyncTask<String, Void, String>{
        //9. declare all variables required for server connection
        URL myurl;
        HttpURLConnection con;
        InputStream is;
        InputStreamReader reader;
        BufferedReader br;
        String str;
        StringBuilder sb;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "ABOUT TO CONNECT", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                //a. prepare website url
                myurl = new URL(strings[0]);
                //b. open connection with server
                con = (HttpURLConnection) myurl.openConnection();
                //c. open connection for reading purpose (inputstream)
                is = con.getInputStream();
                //d. open input stream reader
                reader = new InputStreamReader(is);
                //e. open buffered reader
                br = new BufferedReader(reader);
                //f. now using a do-while loop read data from bufr reader
                str = null;
                sb = new StringBuilder();
                do{
                    str = br.readLine();
                    sb.append(str);
                }while(str != null);
                //g. now we have complete server data in stringbuilder
                //convert stringbuilder to string and give to onpost execute
                return sb.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d("B37","URL PROBLEM");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("B37","NETWORK PROBLEM.."+e.getMessage());
            }
            return "SOME THING WENT WRONG";
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv.setText(s);
        }
    }
    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        if(connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected() == true){
                return true; //we have internet connection
            }
        }
        return false; //we don't have internet connection
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //7. initialize all variables
        tv = (TextView) findViewById(R.id.textView1);
        b = (Button) findViewById(R.id.button1); //NOT REQUIRED.
        //button click is not required
    }

    public void buttonClick(View view) {
        if(isNetworkAvailable() == false){
            Toast.makeText(this, "NO NETWORK TRY AGAIN", Toast.LENGTH_SHORT).show();
            return;
        }
        //8. in button click start async task
        m = new MyTask();
        m.execute("http://techpalle.com"); //pass website url to async task.
    }
}
