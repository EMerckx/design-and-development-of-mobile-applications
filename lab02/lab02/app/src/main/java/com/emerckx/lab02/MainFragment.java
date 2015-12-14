package com.emerckx.lab02;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends ListFragment {

    private boolean isDualPane = false;
    private int currentCheckedPosition = 0;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("MainFragment", "onCreateView(LayoutInflater inflater, ViewGroup container, " +
                "Bundle savedInstanceState)");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        Log.i("MainFragment", "onAttach(Context context)");
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("MainFragment", "onCreate(Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                getResources().getStringArray(R.array.superheroes_names));
        setListAdapter(arrayAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i("MainFragment", "onActivityCreated(Bundle savedInstanceState)");
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            currentCheckedPosition = savedInstanceState.getInt("index2", 0);
        }

        // get the FrameLayout
        Activity activity = getActivity();
        View frameLayoutHistories = activity.findViewById(R.id.framelayout_histories);

        // check if the FrameLayout is visible
        isDualPane = (frameLayoutHistories != null) &&
                (frameLayoutHistories.getVisibility() == View.VISIBLE);

        //
        if (isDualPane) {
            // let the user select only one list item
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            // show the history of the current selected item
            showTheHistory(currentCheckedPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index2", currentCheckedPosition);
    }

    @Override
    public void onStart() {
        Log.i("MainFragment", "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i("MainFragment", "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i("MainFragment", "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i("MainFragment", "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.i("MainFragment", "onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i("MainFragment", "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.i("MainFragment", "onDetach()");
        super.onDetach();
    }

    // ListFragment method

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("MainFragment", "onListItemClick(ListView l, View v, int position, long id)");
        showTheHistory(position);
    }

    private void showTheHistory(int currentListIndex) {
        currentCheckedPosition = currentListIndex;

        Log.i("MainFragment", "currentListIndex = " + currentListIndex);

        if (isDualPane) {
            // set the current list item to selected
            getListView().setItemChecked(currentListIndex, true);

            // get the DetailFragment
            FragmentManager fragmentManager = getFragmentManager();
            DetailFragment detailFragment =
                    (DetailFragment) fragmentManager.findFragmentById(R.id.framelayout_histories);


            Log.i("MainFragment", "detailFragment.getShownIndex() = " + detailFragment);

            // if the shown index is not the new index
            if (detailFragment != null && detailFragment.getShownIndex() != currentListIndex) {
                // create a new DetailFragment
                detailFragment = DetailFragment.newInstance(currentListIndex);

                // replace the DetailFragment
                fragmentManager.beginTransaction().replace(
                        R.id.framelayout_histories,
                        detailFragment
                ).commit();
            }
        } else {
            // if the layout is not dual pane, start the activity with an intent
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("index", currentListIndex);
            startActivity(intent);
        }
    }
}
