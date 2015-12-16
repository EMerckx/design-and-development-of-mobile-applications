package be.ugent.tiwi.oomt.beaconhunt;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

import be.ugent.tiwi.oomt.beaconhunt.model.Beacon;

import static java.lang.Math.pow;


public class MainActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter;
    private boolean scanning = false;
    private BeaconListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashMap<String, Beacon> beacons = new HashMap<>();
        beacons.put("D8:18:AF:89:AE:8D", new Beacon("Estimote I", "D8:18:AF:89:AE:8D", R.drawable.beacon1, false));
        beacons.put("DA:C7:AC:D8:86:31", new Beacon("Estimote II", "DA:C7:AC:D8:86:31", R.drawable.beacon3, false));
        beacons.put("CC:9E:65:5C:6A:09", new Beacon("Estimote III", "CC:9E:65:5C:6A:09", R.drawable.beacon2, false));

        adapter = new BeaconListAdapter(beacons, this);
        ListView beaconList = (ListView) findViewById(R.id.listView);
        beaconList.setAdapter(adapter);

        // get the bluetoothadapter
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth NOT supported", Toast.LENGTH_SHORT).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // on click handler for the button
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (scanning) {
                    scanning = false;
                    button.setText("Start scan");
                    bluetoothAdapter.stopLeScan(mLeScanCallback);
                } else {
                    scanning = true;
                    button.setText("Stop scan");
                    bluetoothAdapter.startLeScan(mLeScanCallback);
                }
            }
        });
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("FOUND", device.getAddress());
                            Beacon b = adapter.getBeacon(device.getAddress());
                            if (b != null) {
                                b.setRssi(rssi);
                                Log.d("DISTANCE", Double.toString(b.getEstimatedDistance()));
                                double d = Double.parseDouble("0.5");
                                if (b.getEstimatedDistance() > d) {
                                    b.setFound(true);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            Toast.makeText(this, "Bluetooth ingeschakeld!", Toast.LENGTH_SHORT).show();
        }
    }
}
