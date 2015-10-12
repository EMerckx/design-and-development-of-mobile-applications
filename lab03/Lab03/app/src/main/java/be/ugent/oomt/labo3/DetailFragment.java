package be.ugent.oomt.labo3;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by elias on 12/01/15.
 */
public class DetailFragment extends Fragment {

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    public static DetailFragment newInstance(int index) {
        DetailFragment f = new DetailFragment();

        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // TODO: change DetailFragment to show selected user feed and initialize loader

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView title = (TextView) view.findViewById(R.id.detail_title);
        TextView summary = (TextView) view.findViewById(R.id.detail_summary);
        title.setText(getResources().getStringArray(R.array.superheroes_names)[getShownIndex()]);
        summary.setText(getResources().getStringArray(R.array.superheroes_history)[getShownIndex()]);
        return view;
    }

    // TODO: implement LoaderManager.LoaderCallbacks<Cursor> interface

    // TODO: on cursor load finish append all messages to text view
}
