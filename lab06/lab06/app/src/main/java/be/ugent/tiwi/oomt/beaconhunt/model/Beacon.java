package be.ugent.tiwi.oomt.beaconhunt.model;

import android.media.Image;

import static java.lang.Math.pow;

/**
 * Created by Sam Leroux on 14/01/15.
 */
public class Beacon {
    private String name;
    private String address;
    private int iconResource;
    private boolean found;
    private int rssi = 0;

    public Beacon(String name, String address, int iconResource, boolean found) {
        this.name = name;
        this.address = address;
        this.iconResource = iconResource;
        this.found = found;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public double getEstimatedDistance(){
        if(rssi == 0){
            return 10;
        }
        double A = -45.0; // Reference RSSI value at 1 meter
        double n = 3; // Path-loss exponent
        String d = String.format("%.2f", pow(10.0,((A + rssi)/(10.0*n))));
        return pow(10.0, ((A + rssi) / (10.0 * n)));
    }

    // return getDistance(rssi, 100);
    private double getDistance(int rssi, int txPower) {
    /*
     * RSSI = TxPower - 10 * n * lg(d)
     * n = 2 (in free space)
     *
     * d = 10 ^ ((TxPower - RSSI) / (10 * n))
     */

        return Math.pow(10d, ((double) txPower - rssi) / (10 * 2));
    }
}
