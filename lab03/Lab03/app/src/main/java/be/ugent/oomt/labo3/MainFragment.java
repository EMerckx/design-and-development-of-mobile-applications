package be.ugent.oomt.labo3;


import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by elias on 12/01/15.
 */
public class MainFragment extends ListFragment {

    boolean mDuelPane;
    int mCurCheckPosition = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: initialize asynchronous loader

        // TODO: Change ArrayAdapter to SimpleCursorAdapter to access the ContentProvider
        ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1,
                getResources().getStringArray(R.array.superheroes_names));
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

    // TODO: implement LoaderManager.LoaderCallbacks<Cursor> interface
}
