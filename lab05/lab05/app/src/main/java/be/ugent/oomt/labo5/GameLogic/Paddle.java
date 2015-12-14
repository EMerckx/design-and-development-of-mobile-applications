package be.ugent.oomt.labo5.GameLogic;

import android.animation.RectEvaluator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;

public class Paddle {
    /** Thickness of the paddle */
    protected static final int PADDLE_THICKNESS = 20;
    /** Width of the paddle */
    protected static final int PADDLE_WIDTH = 120;

    protected static final int STARTING_LIVES = 3;
    protected static final int PLAYER_PADDLE_SPEED = 40;

    // for interpolation
    private RectEvaluator rectEvaluator;
    private Rect mRectToDraw;
    private final Rect mRect;
    private final Rect mRectPrevious;


    private int mHandicap = 0;
    private int mSpeed = PLAYER_PADDLE_SPEED;
    private int mLives = STARTING_LIVES;

    protected int destination;

    public Paddle(int x, int y) {
        mRect = new Rect();
        mRectPrevious = new Rect();
        mRectToDraw = new Rect();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rectEvaluator = new RectEvaluator(mRectToDraw);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            rectEvaluator = new RectEvaluator();
        }
        updatePaddle(x,y);
    }

    protected void updatePaddle(int x, int y) {
        mRect.set(0,y,PADDLE_WIDTH, y + PADDLE_THICKNESS);
        mRectPrevious.set(mRect);
        mRectToDraw.set(mRect);
        setPosition(x);
        destination = x;
    }

    protected void move() {
        move(mSpeed);
    }

    protected void move(boolean handicapped) {
        move((handicapped) ? mSpeed - mHandicap : mSpeed);
    }

    protected void move(int s) {
        mRectPrevious.set(mRect);

        int dx = (int) Math.abs(mRect.centerX() - destination);

        if(destination < mRect.centerX()) {
            mRect.offset((dx > s) ? -s : -dx,0);
        }
        else if(destination > mRect.centerX()) {
            mRect.offset((dx > s) ? s : dx,0);
        }
    }

    public void setLives(int lives) {
        mLives = Math.max(0, lives);
    }

    public void setPosition(int x) {
        mRect.offset(x - mRect.centerX(), 0);
    }

    public void setSpeed(int s) {
        mSpeed = (s > 0) ? s : mSpeed;
    }

    public void setHandicap(int h) {
        mHandicap = (h >= 0 && h < mSpeed) ? h : mHandicap;
    }

    public void loseLife() {
        mLives = Math.max(0, mLives - 1);
    }

    public boolean living() {
        return mLives > 0;
    }

    public int getWidth() {
        return Paddle.PADDLE_WIDTH;
    }

    public int getHeight() { return Paddle.PADDLE_THICKNESS; }

    public int getTop() {
        return mRect.top;
    }

    public int getBottom() {
        return mRect.bottom;
    }

    public int centerX() {
        return mRect.centerX();
    }

    public int centerY() {
        return mRect.centerY();
    }

    public int getLeft() {
        return mRect.left;
    }

    public int getRight() {
        return mRect.right;
    }

    public int getLives() {
        return mLives;
    }

    protected void draw(Canvas canvas, Paint mPaint, float interpolation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mRectToDraw = rectEvaluator.evaluate(interpolation, mRectPrevious, mRect);
        } else {
            mRectToDraw.set(mRect);
        }
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(mRectToDraw, mPaint);
    }
}
