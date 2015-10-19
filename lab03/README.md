# Lab 3

## Exercise

### 1 

Add the provided content provider to your app manifest file. This will make sure the SQLLite database is created when your application is first run.

``` xml
<provider
	android:name="be.ugent.oomt.labo3.contentprovider.MessageProvider"
	android:authorities="be.ugent.oomt.labo3.contentprovider.MessageProvider"
	android:exported="false" />
```

### 2

Update the list view code of last week to take input from the content provider. The ListAdapter of MainFragment must be replaced with a SimpleCursorAdapter. This adapter needs some extra parameters: the cursor which can be null at first but needs to be swapped with the correct cursor, an array of column names from the cursor (accessible through DatabaseContract.Contact|Message class and the array of ids to map the columns to text views of the selected layout file. Use the android.R.layout.simple_list_ item_activated_2 which has 2 text views for the name and the state (android.R.id.text1|text2).

<b>Constructor: public SimpleCursorAdapter (Context context, int layout, Cursor c, String[] from, int[] to, int flags)</b>
* <b>context</b>: The context where the ListView associated with this SimpleListItemFactory is running
* <b>layout</b>: resource identifier of a layout file that defines the views for this list item. The layout file should include at least those named views defined in "to"
* <b>c</b>: The database cursor. Can be null if the cursor is not available yet.
* <b>from</b>: A list of column names representing the data to bind to the UI. Can be null if the cursor is not available yet.
* <b>to</b>: The views that should display column in the "from" parameter. These should all be TextViews. The first N views in this list are given the values of the first N columns in the from parameter. Can be null if the cursor is not available yet.
* <b>flags</b>: Flags used to determine the behavior of the adapter, as per CursorAdapter(Context, Cursor, int).

``` java
// DONE: Change ArrayAdapter to SimpleCursorAdapter to access the ContentProvider
String[] from = new String[]{
    DatabaseContract.Contact.COLUMN_NAME_CONTACT,
    DatabaseContract.Contact.COLUMN_NAME_STATE
};
int[] to = new int[]{
    android.R.id.text1,
    android.R.id.text2
};
ListAdapter listAdapter = new SimpleCursorAdapter(this.getActivity(),
    android.R.layout.simple_list_item_activated_2,
    null,
    from,
    to,
    0);
setListAdapter(listAdapter);
```

### 3

Load the cursor by an asynchronous LoadManager. This will make sure your data is always up-to-date and you do not have to re-query if data is added, updated or removed. We will update the DetailFragment later with the messages received from an other client (for now, just comment out the show details part).

<b>public abstract Loader<D> initLoader (int id, Bundle args, LoaderCallbacks<D> callback)</b>: Ensures a loader is initialized and active. If the loader doesn't already exist, one is created and (if the activity/fragment is currently started) starts the loader. Otherwise the last created loader is re-used. In either case, the given callback is associated with the loader, and will be called as the loader state changes. If at the point of call the caller is in its started state, and the requested loader already exists and has generated its data, then callback onLoadFinished(Loader, D) will be called immediately (inside of this function), so you must be prepared for this to happen.
* <b>id</b>: A unique identifier for this loader. Can be whatever you want. Identifiers are scoped to a particular LoaderManager instance.
* <b>args</b>: Optional arguments to supply to the loader at construction. If a loader already exists (a new one does not need to be created), this parameter will be ignored and the last arguments continue to be used.
* <b>callback</b>: Interface the LoaderManager will call to report about changes in the state of the loader. Required.

``` java
// DONE: initialize asynchronous loader
getLoaderManager().initLoader(0, null, this);
```

We need to implement the callbacks for the loader. Alt Enter on "this" should generate following code.

``` java
public class MainFragment 	extends ListFragment 
							implements LoaderManager.LoaderCallbacks<Object> {

	//...

	@Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}
```


