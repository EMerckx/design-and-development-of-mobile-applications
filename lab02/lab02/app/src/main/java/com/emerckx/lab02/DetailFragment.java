package com.emerckx.lab02;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // get and set the content of the TextViews for the title and history
        TextView titleTextView = (TextView) view.findViewById(R.id.textview_name);
        TextView historyTextView = (TextView) view.findViewById(R.id.textview_history);
        titleTextView.setText(getResources().getStringArray(R.array.superheroes_names)[getShownIndex()]);
        historyTextView.setText(getResources().getStringArray(R.array.superheroes_history)[getShownIndex()]);

        // return the view
        return view;
    }

    public int getShownIndex() {
        // get the index from the arguments
        int index = getArguments().getInt("index", 0);
        return index;
    }

    public static DetailFragment newInstance(int index) {
        // create a new DetailFragment
        DetailFragment newDetailFragment = new DetailFragment();

        // create an argument with the current index
        Bundle arguments = new Bundle();
        arguments.putInt("index", index);

        // add the argument to the new DetailFragment
        newDetailFragment.setArguments(arguments);

        return newDetailFragment;
    }


}
