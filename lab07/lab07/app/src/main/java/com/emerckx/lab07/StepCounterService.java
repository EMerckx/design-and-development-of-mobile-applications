package com.emerckx.lab07;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

public class StepCounterService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;

    private int amountOfSteps;
    boolean hasPeaked;
    private float[] previousValueAccelerometer;
    private float[] currentValueAccelerometer;

    static final String ACTION_STEPCOUNT = "com.emerckx.lab07.stepcount";

    public StepCounterService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // LIFECYCLE METHODS (unbounded service)

    @Override
    public void onCreate() {
        Log.i("StepCounterService", "onCreate()");
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorEventListener = (SensorEventListener) this;

        amountOfSteps = 0;
        hasPeaked = false;
        currentValueAccelerometer = new float[3];
        previousValueAccelerometer = new float[3];
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("StepCounterService", "onStartCommand(Intent intent, int flags, int startId)");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("StepCounterService", "onDestroy()");
        sensorManager.unregisterListener(sensorEventListener);
        super.onDestroy();
    }


    // SENSOREVENTLISTENER METHODS

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Log.i("StepCounterService", "onSensorChanged(SensorEvent event)");
        currentValueAccelerometer = event.values.clone();
        calculateStep();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i("StepCounterService", "onAccuracyChanged(Sensor sensor, int accuracy)");
    }

    // FUNCTIONS

    private double getMagnitude(float x, float y, float z) {
        return Math.sqrt(x * x + y * y + z * z);
    }

    private double getLowpass(double previous, double current, double alpha) {
        // NOTE the  -9.81 because of gravity !!!
        return previous + alpha * (current - previous) - 9.81;
    }

    private void calculateStep() {
        double currentMagnitude = getMagnitude(currentValueAccelerometer[0],
                currentValueAccelerometer[1], currentValueAccelerometer[2]);
        double previousMagnitude = getMagnitude(currentValueAccelerometer[0],
                currentValueAccelerometer[1], currentValueAccelerometer[2]);

        double result = getLowpass(previousMagnitude, currentMagnitude, 0.1);

        double top_threshold = 1.5;
        double bottom_threshold = -1.5;

        if(result > top_threshold){
            hasPeaked = true;
        }
        else if(result < bottom_threshold && hasPeaked){
            Log.i("StepCounterService", "added 1 step");
            amountOfSteps++;
            hasPeaked=false;

            if(amountOfSteps % 10 == 0){
                broadcastIntent();
            }
        }

        previousValueAccelerometer = currentValueAccelerometer.clone();
    }

    // BROADCAST INTENTS

    private void broadcastIntent(){
        Log.i("StepCounterService", "broadcastIntent()");
        Intent intent = new Intent();
        intent.putExtra("steps", amountOfSteps);
        intent.setAction(ACTION_STEPCOUNT);
        sendBroadcast(intent);
    }
}
