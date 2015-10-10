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