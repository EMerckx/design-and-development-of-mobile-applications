package com.emerckx.lab01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Hello extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Hello", "onCreate(Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        // get the intent
        Intent intent = getIntent();
        if (intent.hasExtra("message")) {
            // get the message from the intent
            String base_message = getResources().getString(R.string.started_from);
            String message = intent.getStringExtra("message");

            // set the message
            TextView textView = (TextView) findViewById(R.id.textview_message);
            textView.setText(base_message + " " + message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("Hello", "onCreateOptionsMenu(Menu menu)");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hello, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("Hello", "onOptionsItemSelected(MenuItem item)");
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

    public void onClickButtonSubmit(View view){
        Log.i("Hello", "onClickButtonSubmit(View view)");
        // get the name
        EditText editText = (EditText) findViewById(R.id.edittext_name);
        String name = editText.getText().toString();

        Log.d("Hello", name);

        // create intent
        Intent intent = getIntent();
        intent.putExtra("name", name);

        // finish activity and return the result
        this.setResult(RESULT_OK, intent);
        finish();
    }
}
