package be.ugent.oomt.labo5;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import be.ugent.oomt.labo5.GameLogic.GameState;
import be.ugent.oomt.labo5.GameLogic.GameThread;

/**
 * Created by elias on 21/09/15.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener{

    private final GameState gameState;
    private GameThread _thread;

    private boolean init = false;
    private double ix = 0;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        gameState = new GameState();
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        _thread = new GameThread(getHolder(), gameState);
        _thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // reset the variables
        ix = 0;
        init = false;

        // we have to tell thread to shut down & wait for it to finish
        _thread.stopAndWait();
        _thread = null;
    }

    // DONE: Override onTouchEvent to start the game and control the paddle
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // get the x position of the touch event
        int x = (int) event.getX();

        // if the action was a swipe
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            gameState.movePaddleTo(x);
        }
        // if the action was a tap
        else if(event.getAction() == MotionEvent.ACTION_DOWN){
            gameState.play();
            gameState.movePaddleTo(x);
        }

        return true;
    }

    /*// Create a constant to convert nanoseconds to seconds.
    private static final float NS2S = 1.0f / 1000000000.0f;
    private static final float EPSILON = 0.1f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;
    private float[] rotationMatrix = new float[16];*/


    // DONE: implement SensorEventListener and implement its methods
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i("GameView", "onSensorChanged(SensorEvent event)");

        switch(event.sensor.getType()) {
            case Sensor.TYPE_GYROSCOPE:
                /*// has drift !!!
                // This timestep's delta rotation to be multiplied by the current rotation
                // after computing it from the gyro sample data.
                if (timestamp != 0) {
                    final float dT = (event.timestamp - timestamp) * NS2S;
                    // Axis of the rotation sample, not normalized yet.
                    float axisX = event.values[0];
                    float axisY = event.values[1];
                    float axisZ = event.values[2];

                    // Calculate the angular speed of the sample
                    float omegaMagnitude = (float) Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

                    // Normalize the rotation vector if it's big enough to get the axis
                    // (that is, EPSILON should represent your maximum allowable margin of error)
                    if (omegaMagnitude > EPSILON) {
                        axisX /= omegaMagnitude;
                        axisY /= omegaMagnitude;
                        axisZ /= omegaMagnitude;
                    }

                    // Integrate around this axis with the angular speed by the timestep
                    // in order to get a delta rotation from this sample over the timestep
                    // We will convert this axis-angle representation of the delta rotation
                    // into a quaternion before turning it into the rotation matrix.
                    float thetaOverTwo = omegaMagnitude * dT / 2.0f;
                    float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
                    float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
                    deltaRotationVector[0] = sinThetaOverTwo * axisX;
                    deltaRotationVector[1] = sinThetaOverTwo * axisY;
                    deltaRotationVector[2] = sinThetaOverTwo * axisZ;
                    deltaRotationVector[3] = cosThetaOverTwo;
                }
                timestamp = event.timestamp;
                float[] deltaRotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
                // User code should concatenate the delta rotation we computed with the current rotation
                // in order to get the updated rotation.
                float[] copy = rotationMatrix.clone();
                Matrix.multiplyMM(rotationMatrix, 0, copy, 0, deltaRotationMatrix, 0);
                //float theta_x = ((float) Math.atan2(rotationMatrix[9], rotationMatrix[10]));
                float theta_y = ((float) Math.atan2(rotationMatrix[8] * -1, Math.sqrt(Math.pow(rotationMatrix[9], 2) + Math.pow(rotationMatrix[10], 2))));
                //float theta_z = ((float) Math.atan2(rotationMatrix[4], rotationMatrix[0]));
                //Log.d("TEST", "theta_y " + "      " + theta_y);
                float dx = theta_y * 40;
                gameState.movePaddle((int) dx);*/
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                if (!init) {
                    ix = Math.toDegrees(event.values[1]);
                    init = true;
                }
                double sx = (Math.toDegrees(event.values[1]) - ix);
                gameState.movePaddle((int)sx);
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                gameState.movePaddle((int) Math.toDegrees(event.values[1]));
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i("GameView", "onAccuracyChanged(Sensor sensor, int accuracy)");

    }


}
