package com.emerckx.lab01;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "onCreate(Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set a click listener for the go get it button
        View buttonView = findViewById(R.id.button_go_get_it);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MainActivity", "onClick(View view)");

                // create an Intent to start an activity called DisplayMessageActivity
                Intent intent = new Intent(MainActivity.this, Hello.class);
                startActivity(intent);

                /*Activity helloActivity = new Hello();
                Intent intent = new Intent(helloActivity, MainActivity.class);
                startActivity(intent);*/
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("MainActivity", "onCreateOptionsMenu(Menu menu)");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("MainActivity", "onOptionsItemSelected(MenuItem item)");
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

    @Override
    protected void onStart() {
        Log.i("MainActivity", "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i("MainActivity", "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("MainActivity", "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("MainActivity", "onStop()");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.i("MainActivity", "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.i("MainActivity", "onDestroy()");
        super.onDestroy();
    }

    public void onClickButtonHelloWorld(View view) {
        Log.i("MainActivity", "onClickButtonHelloWorld(View view)");

        // get the variables for the toast message
        Context context = getApplicationContext();
        CharSequence text = "Hello world! (clicked)";
        int duration = Toast.LENGTH_SHORT;
        // create a toast message
        Toast toast = Toast.makeText(context, text, duration);
        // show the toast message on screen
        toast.show();

        // change the text of the Textview
        TextView textView = (TextView) findViewById(R.id.textview_hello_world);
        String helloWorld = getResources().getString(R.string.hello_world);
        String time = getTime();
        textView.setText(helloWorld + " " + time);
    }

    private String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Brussels"));
        Date date = new Date();
        return dateFormat.format(date);
    }
}
