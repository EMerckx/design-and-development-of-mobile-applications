# Lab 5: Touch events and sensor basics

## Question 1

While using the gyroscope we changed the orientation from portrait to landscape. Why does the functionality change when the orientation is changed? How can we fix this?

```
Portrait:
	rotation to the left gives us movement in the left direction
	rotation to the right gives us movement in the right direction

Landscape:
	rotation to the left gives us movement in the left direction
	rotation to the right gives us movement in the left direction

This is because we still rotate to the left, seen from the
portrait position. If we rotate the device to the left, as seen 
from the portrait layout, we see that there is movement in the
right direction.
So the portrait axis remains!

Fix: choose a different axis?
```

## Add the GameView

In the file activity_pong.xml we add the be.ugent.oomt.labo5.GameView surfaceview to the viewgroup.

```xml
<be.ugent.oomt.labo5.GameView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:keepScreenOn="true"
    android:id="@+id/gameview" />
```

## Game control with touch events

Override OnTouchEvent in the GameView to start the game and control the paddle.

```java
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
```

## List all sensors available for this device 

In the onCreate() method of PongActivity

```java
SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
for (Sensor sensor : sensorList) {
    Log.i("PongActivity", sensor.getName());
}
```

( check the code for more info )