package be.ugent.oomt.labo5;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import be.ugent.oomt.labo5.GameLogic.GameState;
import be.ugent.oomt.labo5.GameLogic.GameThread;

/**
 * Created by elias on 21/09/15.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private final GameState gameState;
    private GameThread _thread;

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
        // we have to tell thread to shut down & wait for it to finish
        _thread.stopAndWait();
        _thread = null;
    }

    // TODO: Override onTouchEvent to start the game and control the paddle

    // TODO: implement SensorEventListener and implement its methods
}
