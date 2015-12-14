package be.ugent.oomt.labo5.GameLogic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameState {

    private final Paint mPaint = new Paint();
    private final Rect bounds = new Rect();

    private Ball mBall;
    private Paddle mPlayer;
    private Paddle mOpponent;

    private boolean mInitialized = false;
    private boolean updatePaddlesAndBall = false;
    private int height=0;
    private int width=0;
    private static final int PADDING = 20;

    public GameState() {
        //set the game colour
        mPaint.setARGB(200, 0, 200, 0);
        //change font size
        mPaint.setTextSize(120f);
    }

    //The update method
    protected void update() {
        if (height == 0 || width == 0) {
            return;
        }

        if (!mInitialized) {
            mBall = new Ball();
            serveBall();
            mOpponent = new Paddle(width>>1, PADDING);
            mOpponent.setHandicap(20); // decreases max speed
            mPlayer = new Paddle(width>>1, height - PADDING - Paddle.PADDLE_THICKNESS);
            mInitialized = true;
            updatePaddlesAndBall = false;
        }

        if (updatePaddlesAndBall) {
            mOpponent.updatePaddle(width>>1, PADDING);
            mPlayer.updatePaddle(width>>1, height - PADDING - Paddle.PADDLE_THICKNESS);
            mBall.setX(width>>1); //devide by 2
            mBall.setY(height>>1); //devide by 2
            updatePaddlesAndBall = false;
        }

        mBall.move();

        // Shake it up if it appears to not be moving vertically
        if(!mBall.goingUp() && !mBall.goingDown() && !mBall.isPaused()) {
            mBall.randomAngle();
        }

        // Do some basic paddle AI
        mOpponent.destination = (int) bound(mBall.getX(), mOpponent.getWidth()>>1, width - (mOpponent.getWidth()>>1));
        mOpponent.move(true);

        mPlayer.move(false);

        handleBounces();

        // See if all is lost
        if(mBall.getY() >= height) {
            mPlayer.loseLife();
            serveBall();
        } else if (mBall.getY() <= 0) {
            mOpponent.loseLife();
            serveBall();
        }
    }

    private void handleBounces() {
        // Handle bouncing off of a wall
        if(mBall.getX() <= Ball.RADIUS || mBall.getX() >= width - Ball.RADIUS) {
            mBall.bounceVertical();
            // correct ball position
            if (mBall.getX() < Ball.RADIUS) {
                mBall.setX(Ball.RADIUS - mBall.getX());
            } else if (mBall.getX() > width - Ball.RADIUS) {
                mBall.setX(mBall.getX() - Ball.RADIUS + mBall.getXp() - width);
            }
        }

        // Handle opponent bounce
        collisionWithPaddle(mOpponent);

        // Handle player bounce
        collisionWithPaddle(mPlayer);
    }

    private void collisionWithPaddle(Paddle paddle) {
        float dx = Math.max(Math.abs(mBall.getX() - paddle.centerX()) - (paddle.getWidth()>>1) - mBall.RADIUS, 0);
        float dy = Math.max(Math.abs(mBall.getY() - paddle.centerY()) - (paddle.getHeight()>>1)  - mBall.RADIUS, 0);
        float distance = dx * dx + dy * dy;

        if (distance <= 0) { //collision
            if (paddle.getTop() - mBall.RADIUS > mBall.getYp() &&
                    paddle.getTop() - mBall.RADIUS <= mBall.getY() &&
                    paddle.getLeft() - mBall.RADIUS <= mBall.getX() &&
                    mBall.getX() <= paddle.getRight() + mBall.RADIUS) { // bounce on top side
                mBall.setY(paddle.getTop() - mBall.RADIUS - (mBall.getY() - paddle.getTop()));
                mBall.bounceHorizontal(paddle);
            } else if (paddle.getBottom() + mBall.RADIUS < mBall.getYp() &&
                    paddle.getBottom() + mBall.RADIUS >= mBall.getY() &&
                    paddle.getLeft() - mBall.RADIUS <= mBall.getX() &&
                    mBall.getX() <= paddle.getRight() + mBall.RADIUS) { // bounce on bottom side
                mBall.setY(paddle.getBottom() + mBall.RADIUS + (paddle.getBottom() - mBall.getY()));
                mBall.bounceHorizontal(paddle);
            }else if (paddle.getRight() + mBall.RADIUS < mBall.getXp() &&
                    paddle.getRight() + mBall.RADIUS >= mBall.getX() &&
                    paddle.getTop() - mBall.RADIUS < mBall.getY() &&
                    mBall.getY() < paddle.getBottom() + mBall.RADIUS) { // bounce on right side
                mBall.bounceVertical();
                mBall.setX(paddle.getRight() + mBall.RADIUS + (paddle.getRight() - mBall.getX()));
            } else if (paddle.getLeft() - mBall.RADIUS > mBall.getXp() &&
                    paddle.getLeft() - mBall.RADIUS <= mBall.getX() &&
                    paddle.getTop() - mBall.RADIUS < mBall.getY() &&
                    mBall.getY() < paddle.getBottom() + mBall.RADIUS) { // bounce on left side
                mBall.bounceVertical();
                mBall.setX(paddle.getLeft() - mBall.RADIUS - (mBall.getX() - paddle.getLeft()));
            }
            increaseDifficulty();
        }
    }

        /**
         * Reset ball to an initial state
         */
    private void serveBall() {
        mBall.setX(width>>1); //devide by 2
        mBall.setY(height>>1); //devide by 2
        mBall.setPaused(true);
        mBall.speed = Ball.SPEED;
        mBall.randomAngle();
    }

    public void play() {
        mBall.setPaused(false);
        if (!mPlayer.living() || !mOpponent.living()) {
            mPlayer.setLives(Paddle.STARTING_LIVES);
            mOpponent.setLives(Paddle.STARTING_LIVES);
        }
    }

    /**
     * Knocks up the framerate a bit to keep it difficult.
     */
    private void increaseDifficulty() {
        mBall.speed++;
    }

    public void movePaddle(int dx) {
        mPlayer.destination = (int) bound(dx + mPlayer.destination, mPlayer.getWidth()>>1, width - (mPlayer.getWidth()>>1));
    }

    public void movePaddleTo(int x) {
        mPlayer.destination = (int) bound(x, mPlayer.getWidth()>>1, width - (mPlayer.getWidth()>>1));
    }

    private float bound(float x, float low, float hi) {
        return Math.max(low, Math.min(x, hi));
    }

    String win = "WON";
    String lose = "LOST";
    //the draw method
    protected void draw(Canvas canvas, float interpolation) {
        if (this.height != canvas.getHeight() || this.width != canvas.getWidth()) {
            this.height = canvas.getHeight();
            this.width = canvas.getWidth();
            updatePaddlesAndBall = true;
        }

        if (!mInitialized) {
            return;
        }

        //Clear the screen
        canvas.drawColor(Color.BLACK);

        //draw the ball
        mBall.draw(canvas, mPaint, interpolation);

        //draw the bats
        mPlayer.draw(canvas, mPaint, interpolation);
        mOpponent.draw(canvas, mPaint, interpolation);

        //draw line
        // remember x >> 1 is equivalent to x / 2, but works much much faster
        canvas.drawLine(0, (height>>1), width, (height>>1), mPaint);

        //draw score
        // remember x >> 1 is equivalent to x / 2, but works much much faster
        String a = String.valueOf(mPlayer.getLives()), b = String.valueOf(mOpponent.getLives());
        //measure height of font
        mPaint.getTextBounds(a, 0, a.length(), bounds);
        canvas.drawText(a, PADDING, (this.height>>1) + PADDING + bounds.height(), mPaint);
        mPaint.getTextBounds(b, 0, b.length(), bounds);
        canvas.drawText(b, this.width - PADDING - bounds.width(), (this.height>>1) - PADDING, mPaint);

        //draw win/lose
        if (!mOpponent.living()) {
            mPaint.getTextBounds(win, 0, win.length(), bounds);
            canvas.drawText(lose, (this.width - bounds.width()) >> 1, ((this.height - bounds.height()) >> 2), mPaint);
            canvas.drawText(win, (this.width - bounds.width())>>1, (this.height>>1) + ((this.height - bounds.height())>>2), mPaint);
        } else if (!mPlayer.living()) {
            mPaint.getTextBounds(lose, 0, lose.length(), bounds);
            canvas.drawText(win, (this.width - bounds.width()) >> 1, ((this.height - bounds.height()) >> 2), mPaint);
            canvas.drawText(lose, (this.width - bounds.width())>>1, (this.height>>1) + ((this.height - bounds.height())>>2), mPaint);
        }
    }
}
