# Lab 1

## Question 1

Which lifecycle callbacks are called in the following scenarios: 

* when pressing the back button

```
com.emerckx.lab01 I/MainActivity﹕ onPause()
com.emerckx.lab01 I/MainActivity﹕ onStop()
com.emerckx.lab01 I/MainActivity﹕ onDestroy()
```

* removing a task from recent apps (after back button)

```
nothing happens
```

* when pressing the home button

```
com.emerckx.lab01 I/MainActivity﹕ onPause()
com.emerckx.lab01 I/MainActivity﹕ onStop()
```

* removing a task from recent apps (after home button)

```
? I/MainActivity﹕ onDestroy()
```

* on device rotation?

```
To rotate the emulator: CTRL F12

com.emerckx.lab01 I/MainActivity﹕ onPause()
com.emerckx.lab01 I/MainActivity﹕ onStop()
com.emerckx.lab01 I/MainActivity﹕ onDestroy()

com.emerckx.lab01 I/MainActivity﹕ onCreate(Bundle savedInstanceState)
com.emerckx.lab01 I/MainActivity﹕ onStart()
com.emerckx.lab01 I/MainActivity﹕ onCreateOptionsMenu(Menu menu)
com.emerckx.lab01 I/MainActivity﹕ onResume()
```

## Extra info

### Linear layout

Vertical

```
<LinearLayout
	android:orientation="vertical">
</LinearLayout>
```

Horizontal

```
<LinearLayout
	android:orientation="horizontal">
</LinearLayout>
```

### Center item in parent

```
android:layout_gravity="center"
```

### On click listener

#### In xml and activity

in xml file

```
android:onClick="onClickButtonClicked"
```

in activity file

```java
public void onClickButtonClicked(View view){ }
```

#### Only in activity

```java
// set a click listener for the go get it button
View buttonView = findViewById(R.id.button_id);
buttonView.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View view) { }
});
```

### Toast message

This is a message shown on screen

```java
private void showToastMessage(String message){
	// get the variables for the toast message
	Context context = getApplicationContext();
	int duration = Toast.LENGTH_SHORT;

	// create the toast message
	Toast toast = Toast.makeText(context, message, duration);

	// show the toast message on screen
	toast.show();
}
```

### Datetime

This gets the local datetime as a string (e.g. "13/12/2015 16:29:59")

```java
private String getTime() {
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Brussels"));
	Date date = new Date();
	return dateFormat.format(date);
}
```

### Edit TextView

This edits the text of a TextView on the screen

```
android:id="@+id/textview_id"
```

```java
String origText = getResources().getString(R.string.origText);
String time = getTime();

TextView textView = (TextView) findViewById(R.id.textview_id);
textView.setText(origText + " " + time);
```

### Activity with Intent

#### In MainActivity

Add value as a field

```java
private final int REQUEST_ID = 1;
```

Code

```java
// create an Intent to start an activity called DisplayMessageActivity
Intent intent = new Intent(MainActivity.this, Hello.class);
intent.putExtra("message", getClass().getName());

// to start the activity for a result, 
// add the intent and an id to identify the request!
startActivityForResult(intent, REQUEST_ID);
```

#### In Hello

In the following method

```java
@Override
protected void onCreate(Bundle savedInstanceState) { }
```

Add the following

```java
// get the intent
Intent intent = getIntent();
if (intent.hasExtra("message")) { }
```

#### Return intent

```java
// create intent
Intent intent = getIntent();
intent.putExtra("name", name);

// finish activity and return the result
this.setResult(RESULT_OK, intent);
finish();
```

#### In MainActivity

```java
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
	// if status code is OK
	if (resultCode == RESULT_OK) {
		if (requestCode == REQUEST_ID && data.hasExtra("name")) {
			// get the data from the Intent
			String name = data.getExtras().getString("name");
			// do something with the name
		}
	}
}
```
