# Lab 2: ListView with Fragments

Change the orientation in the emulator: CTRL F12

## Question 1

What is the correlation between the lifecycle callbacks of the fragment and its encompassing activity?

```
The lifecycle of the activity in which the fragment lives directly affects the 
lifecycle of the fragment, such that each lifecycle callback for the activity 
results in a similar callback for each fragment.

For example, when the activity receives onPause(), each fragment in the activity 
receives onPause().
```

## Question 2

Which lifecycle methods of the activity and the fragment are called on device rotation?	

```
com.emerckx.lab02 I/MainFragment﹕ onPause()
com.emerckx.lab02 I/MainActivity﹕ onPause()
com.emerckx.lab02 I/MainFragment﹕ onStop()
com.emerckx.lab02 I/MainActivity﹕ onStop()
com.emerckx.lab02 I/MainFragment﹕ onDestroyView()
com.emerckx.lab02 I/MainFragment﹕ onDestroy()
com.emerckx.lab02 I/MainFragment﹕ onDetach()
com.emerckx.lab02 I/MainActivity﹕ onDestroy()

com.emerckx.lab02 I/MainActivity﹕ onCreate(Bundle savedInstanceState)
com.emerckx.lab02 I/MainFragment﹕ onCreate(Bundle savedInstanceState)
com.emerckx.lab02 I/MainFragment﹕ onCreateView(LayoutInflater inflater, 
									ViewGroup container, Bundle savedInstanceState)
com.emerckx.lab02 I/MainFragment﹕ onActivityCreated(Bundle savedInstanceState)
com.emerckx.lab02 I/MainActivity﹕ onStart()
com.emerckx.lab02 I/MainFragment﹕ onStart()
com.emerckx.lab02 I/MainActivity﹕ onCreateOptionsMenu(Menu menu)
com.emerckx.lab02 I/MainActivity﹕ onResume()
com.emerckx.lab02 I/MainFragment﹕ onResume()
```

## Question 3

To test your current set-up, create a new instance of DetailFragment and put it in the FrameLayout container of the MainActivity when the activity is created. Don’t do this by adding a fragment tag to the XML layout file, instead use the FragmentManager.

Why don’t we use the XML approach here?

```
study the difference between the add and update methods of the FragmentManager

the FrameLayout is only available in landscape mode! In portrait mode, 
you will encounter null pointer exceptions if you try to put a fragment 
in this container.

???
```

## Question 4

There are two possibilities to prevent these null pointer exceptions. Either you 
check the presence of the container element, either you check for the device 
orientation. What is the most appropriate strategy and why?

```
We will check for the presence of the container element.
Or in other words, we will check the XML file that is being used.
That way, the application is more extensible.
```

## Question 5

What is the most appropriate location in your code to perform this check?

```
The check must be preformed when an activity stops and a new one is started.
So we will implement the check in the onCreate() method.
```

## Question 6

Finish this activity when it is viewed in landscape orientation, to avoid that a large detail fragment is shown. Instead, switch back to the main activity.

There is one specific case where this behavior happens. Which one?

```
When we change the orientation of the phone.
If we change it, all the fragments will be terminated.
```

