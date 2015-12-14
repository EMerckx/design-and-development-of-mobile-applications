package be.ugent.oomt.labo3;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import be.ugent.oomt.labo3.contentprovider.MessageProvider;
import be.ugent.oomt.labo3.contentprovider.database.DatabaseContract;

/**
 * Created by elias on 12/01/15.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView titleTextView;
    private TextView summaryTextView;

    public String getShownContact() {
        return getArguments().getString("contact", "");
    }

    public static DetailFragment newInstance(String contact) {
        DetailFragment f = new DetailFragment();

        Bundle args = new Bundle();
        args.putString("contact", contact);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, this);

        // DONE: change DetailFragment to show selected user feed and initialize loader
        // get the textviews
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        titleTextView = (TextView) view.findViewById(R.id.detail_title);
        summaryTextView = (TextView) view.findViewById(R.id.detail_summary);

        // get the contact name
        String contactName = getShownContact();

        // change the text of the textviews
        titleTextView.setText(contactName);
        summaryTextView.setText("");

        return view;
    }


    // DONE: implement LoaderManager.LoaderCallbacks<Cursor> interface
    // DONE: on cursor load finish append all messages to text view
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // set the projection: message id and the message
        String[] projection = {
                DatabaseContract.Message._ID,
                DatabaseContract.Message.COLUMN_NAME_MESSAGE
        };

        // set the order by: date ascending
        String orderBy = DatabaseContract.Message.COLUMN_NAME_DATE + " ASC";

        // return the new CursorLoader
        return new CursorLoader(
                getActivity(),
                MessageProvider.MESSAGES_CONTENT_URL,
                projection,
                DatabaseContract.Message.COLUMN_NAME_CONTACT + "=?",
                new String[]{getShownContact()},
                orderBy
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        summaryTextView.setText("");
        while (data.moveToNext()) {
            summaryTextView.append(data.getString(
                    data.getColumnIndex(DatabaseContract.Message.COLUMN_NAME_MESSAGE)
            ) + "\n");
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

}
