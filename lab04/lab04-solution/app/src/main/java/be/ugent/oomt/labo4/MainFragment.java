package be.ugent.oomt.labo4;


import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import be.ugent.oomt.labo4.contentprovider.MessageProvider;
import be.ugent.oomt.labo4.contentprovider.database.DatabaseContract;

/**
 * Created by elias on 12/01/15.
 */
public class MainFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private boolean mDuelPane;
    private static final String TAG = "MainFragment";

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mCurCheckPosition = 0;//ListView.INVALID_POSITION;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(0, null, this);
        // Fields from the database (projection)
        String[] from = new String[] {
                DatabaseContract.Contact.COLUMN_NAME_CONTACT,
                DatabaseContract.Contact.COLUMN_NAME_STATE,
        };
        // Fields on the UI to which we map
        int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
        ListAdapter listAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_activated_2, null, from, to, 0);
        setListAdapter(listAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View detailsFrame = getActivity().findViewById(R.id.detail_container);

        mDuelPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            mCurCheckPosition = savedInstanceState.getInt(STATE_ACTIVATED_POSITION, mCurCheckPosition);
        }

        if (mDuelPane) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_ACTIVATED_POSITION, mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    private void showDetails(int index) {
        final Cursor c = (Cursor) getListView().getItemAtPosition(index);
        if (c != null) {
            final String contact = c.getString(c.getColumnIndex(DatabaseContract.Contact.COLUMN_NAME_CONTACT));

            // TODO: subscribe to topic of the selected user with mqtt
            /*MqttHandler mqtthandler = MqttHandler.getInstance();
            try {
                mqtthandler.getClient().subscribe("/users/"+contact,2);
            } catch (MqttException e) {
                e.printStackTrace();
            }*/
            if (mDuelPane) {
                getListView().setItemChecked(index, true);

                DetailFragment details = (DetailFragment) getFragmentManager().findFragmentById(R.id.detail_container);

                if (details == null || details.getShownContact() != contact) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.detail_container, DetailFragment.newInstance(contact))
                            .commit();
                }
            } else {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("contact", contact);
                startActivity(intent);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DatabaseContract.Contact.COLUMN_NAME_CONTACT,
                DatabaseContract.Contact.COLUMN_NAME_STATE,
        };
        String orderBy = DatabaseContract.Contact.COLUMN_NAME_CONTACT + " = \"" + MqttHandler.clientId +  "\" DESC," + DatabaseContract.Contact.COLUMN_NAME_CONTACT + " ASC";
        return new CursorLoader(getActivity(), MessageProvider.CONTACTS_CONTENT_URL, projection, null, null, orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((CursorAdapter)getListAdapter()).swapCursor(data);
        if (mDuelPane) {
            handler.sendEmptyMessage(2);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((CursorAdapter)getListAdapter()).swapCursor(null);
    }

    private Handler handler = new Handler()  { // handler for commiting fragment after data is loaded
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 2) {
                Log.d(TAG, "Onload finished : handler called. setting the fragment.");
                // commit the fragment
                showDetails(mCurCheckPosition);
            }
        }
    };
}
