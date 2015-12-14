package be.ugent.oomt.labo3;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import be.ugent.oomt.labo3.contentprovider.MessageProvider;
import be.ugent.oomt.labo3.contentprovider.database.DatabaseContract;

/**
 * Created by elias on 12/01/15.
 */
public class MainFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    boolean mDuelPane;
    int mCurCheckPosition = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // DONE: initialize asynchronous loader
        LoaderManager loadermanager = getLoaderManager();
        loadermanager.initLoader(0, null, this);

        // DONE: Change ArrayAdapter to SimpleCursorAdapter to access the ContentProvider
        String[] from = new String[]{
                DatabaseContract.Contact.COLUMN_NAME_CONTACT,
                DatabaseContract.Contact.COLUMN_NAME_STATE
        };
        int[] to = new int[]{
                android.R.id.text1, android.R.id.text2
        };
        ListAdapter listAdapter = new SimpleCursorAdapter(
                getActivity(),                                      // Context
                android.R.layout.simple_list_item_activated_2,      // int layout
                null,                                               // Context
                from,                                               // String[] from
                to,                                                 // int[] to
                0                                                   // int flags
        );
        setListAdapter(listAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        View detailsFrame = getActivity().findViewById(R.id.detail_container);
        mDuelPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        if (mDuelPane) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showDetails(mCurCheckPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    private void showDetails(int index) {
        final Cursor cursor = (Cursor) getListView().getItemAtPosition(index);

        if(cursor != null){
            final String contact = cursor.getString(
                    cursor.getColumnIndex(DatabaseContract.Contact.COLUMN_NAME_CONTACT)
            );

            if(mDuelPane){
                getListView().setItemChecked(index, true);

                FragmentManager frag = getFragmentManager();
                DetailFragment detailFragment =
                        (DetailFragment) frag.findFragmentById(R.id.detail_container);

                if(detailFragment == null || detailFragment.getShownContact() != contact){
                    frag.beginTransaction().replace(
                            R.id.detail_container, DetailFragment.newInstance(contact)
                    ).commit();
                }
            }
            else {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("contact", contact);
                startActivity(intent);
            }
        }
    }

    // DONE: implement LoaderManager.LoaderCallbacks<Cursor> interface
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i("MainFragment", "onCreateLoader(int id, Bundle args)");

        // add the test data
        MessageProvider.addTestData(getActivity());

        // set the projection
        String[] projection = {
                DatabaseContract.Contact.COLUMN_NAME_CONTACT,
                DatabaseContract.Contact.COLUMN_NAME_STATE
        };

        // return the CursorLoader to get the content
        return new CursorLoader(getActivity(), MessageProvider.CONTACTS_CONTENT_URL, projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i("MainFragment", "onLoadFinished(Loader<Object> loader, Object data)");

        CursorAdapter cursorAdapter = (CursorAdapter) getListAdapter();
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i("MainFragment", "onLoaderReset(Loader<Object> loader)");

        CursorAdapter cursorAdapter = (CursorAdapter) getListAdapter();
        cursorAdapter.swapCursor(null);
    }
}
