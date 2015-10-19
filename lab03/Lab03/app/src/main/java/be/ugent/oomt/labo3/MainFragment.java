package be.ugent.oomt.labo3;


import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import be.ugent.oomt.labo3.contentprovider.database.DatabaseContract;

/**
 * Created by elias on 12/01/15.
 */
public class MainFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Object> {

    boolean mDuelPane;
    int mCurCheckPosition = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // DONE: initialize asynchronous loader
        // public abstract Loader<D> initLoader (int id, Bundle args, LoaderCallbacks<D> callback)
        getLoaderManager().initLoader(0, null, this);

        // DONE: Change ArrayAdapter to SimpleCursorAdapter to access the ContentProvider
        // public SimpleCursorAdapter (Context context, int layout, Cursor c, String[] from,
        //                              int[] to, int flags)
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
        mCurCheckPosition = index;

        if (mDuelPane) {
            getListView().setItemChecked(index, true);

            DetailFragment details = (DetailFragment) getFragmentManager().findFragmentById(R.id.detail_container);
            if (details == null || details.getShownIndex() != index) {
                details = DetailFragment.newInstance(index);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detail_container, details)
                        .commit();
            }
        } else {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }

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

    // TODO: implement LoaderManager.LoaderCallbacks<Cursor> interface
}
