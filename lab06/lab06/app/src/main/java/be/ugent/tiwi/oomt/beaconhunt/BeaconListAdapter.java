package be.ugent.tiwi.oomt.beaconhunt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import be.ugent.tiwi.oomt.beaconhunt.model.Beacon;

/**
 * Created by Sam Leroux on 14/01/15.
 */
public class BeaconListAdapter extends BaseAdapter {

    private ArrayList<String> addresses;
    private HashMap<String, Beacon> beacons;
    private Context context;

    public BeaconListAdapter(Context context) {
        this(new HashMap<String, Beacon>(),context);
    }

    public BeaconListAdapter(HashMap<String, Beacon> beacons, Context context) {
        this.beacons = beacons;
        this.context = context;
        this.addresses = new ArrayList<>();
        for (String address:beacons.keySet()){
            addresses.add(address);
        }
    }

    public void addBeacon(Beacon b){
        beacons.put(b.getAddress(), b);
        addresses.add(b.getAddress());
        notifyDataSetChanged();
    }

    public Beacon getBeacon(String address){
        return beacons.get(address);
    }

    @Override
    public int getCount() {
        return beacons.size();
    }

    @Override
    public Object getItem(int position) {
        return beacons.get(addresses.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.beacon_list_element, null);
        }

        Beacon b = (Beacon)getItem(position);

        ImageView image = (ImageView) v.findViewById(R.id.imageView);
        TextView txtDistance = (TextView)v.findViewById(R.id.textView2);
        TextView txtName = (TextView)v.findViewById(R.id.textView4);
        CheckBox swtFound = (CheckBox)v.findViewById(R.id.checkBox);
        ProgressBar pgbProgress = (ProgressBar)v.findViewById(R.id.progressBar);

        image.setImageResource(b.getIconResource());
        txtDistance.setText(b.getEstimatedDistance() + " m");
        txtName.setText(b.getName());
        swtFound.setChecked(b.isFound());

        Double d = b.getEstimatedDistance();
        pgbProgress.setProgress(100 - (d.intValue()*10));

        return v;
    }
}
