package com.emerckx.lab02;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "onCreate(Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    protected void onDestroy() {
        Log.i("MainActivity", "onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.i("MainActivity", "onRestart()");
        super.onRestart();
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
}
