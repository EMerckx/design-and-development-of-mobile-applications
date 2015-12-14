package be.ugent.oomt.labo5.GameLogic;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    /** Handle to the surface manager object we interact with */
    private final SurfaceHolder _surfaceHolder;
    private final GameState _state;

    volatile private boolean _run;

    private static int TICKS_PER_SECOND = 25;
    private static int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
    private static int MAX_FRAMESKIP = 5;

    private long next_game_tick = SystemClock.uptimeMillis();
    private int loops;
    private float interpolation;

    public GameThread(SurfaceHolder holder, GameState state) {
        _surfaceHolder = holder;
        _state = state;
    }

    @Override
    public void start() {
        _run = true;
        super.start();
    }

    public void stopAndWait() {
        boolean retry = true;
        _run = false;
        while (retry) {
            try {
                this.join();
                retry = false;
            } catch (InterruptedException e) {
                // we will try it again and again...
            }
        }
    }

    @Override
    public void run() {
        Canvas c;
        while(_run)
        {
            loops = 0;
            while( SystemClock.uptimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) {
                _state.update();

                next_game_tick += SKIP_TICKS;
                loops++;
            }

            interpolation = (float)(SystemClock.uptimeMillis() + SKIP_TICKS - next_game_tick) / (float)SKIP_TICKS;
            c = null;
            try {
                c = _surfaceHolder.lockCanvas(null);
                if (c != null) { // lunar lander bug
                    synchronized (_surfaceHolder) {
                        _state.draw(c, interpolation);
                    }
                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    _surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
        Log.i(GameThread.class.toString(), "Ending game thread");
    }
}
