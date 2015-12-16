# Lab 7: Android services, Broadcastintents, custom UI components and the accelerometer

## Service

### Create new service

```
Right click on project > New > Service > Service
```

### Lifecycle

![Service lifecycle][img1]

### Unbounded or bounded service

> <b>Unbounded Service or Started</b>
>
>A service is started when an application component, such as an activity, starts it by calling startService(). Once started, a service can run in the background indefinitely, even if the component that started it is destroyed.
>
> -- <cite>Spurdow</cite>

Good explanation on StackOverflow: [here][1] and [here][2]

So we will choose for an unbounded service in our app. 

### Get the accelerometer

Global variables

```java
private SensorManager sensorManager;
private Sensor sensor;
```

in onCreate()

```java
sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
```

### Step counter implementation

For this implementation we will follow [this guide in Python][3]

First we set our 4 global variables: the amount of steps, a boolean to see if the value has peaked, the current value and the previous value of the accelerometer

```java
private int amountOfSteps;
boolean hasPeaked;
private float[] previousValueAccelerometer;
private float[] currentValueAccelerometer;
```

Init them in the onCreate() method

```java
amountOfSteps = 0;
hasPeaked = false;
currentValueAccelerometer = new float[3];
previousValueAccelerometer = new float[3];
```

#### Step 1: Calculate the maginitude of the signal

```java
private double getMagnitude(double x, double y, double z){
    return Math.sqrt(x * x + y * y + z * z);
}
```

#### Step 2: Low pass filter the magnitude signal

```java
private double getLowpass(double previous, double current, double alpha) {
    // NOTE the  -9.81 because of gravity !!!
    return previous + alpha * (current - previous) - 9.81;
}
```

#### Step 3: Thresholding

```java
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
    }

    previousValueAccelerometer = currentValueAccelerometer.clone();
}
```

#### Finally call method

```java
@Override
public void onSensorChanged(SensorEvent event) {
    currentValueAccelerometer = event.values.clone();
    calculateStep();
}
```


https://developer.android.com/tools/support-library/setup.html#libs-with-res

https://developer.android.com/tools/support-library/features.html#v7

[1]: https://stackoverflow.com/questions/25240299/can-anybody-explain-what-is-difference-between-unbound-and-bound-service-in-andr
[2]: https://stackoverflow.com/questions/9272217/service-or-bound-service
[3]: http://nbviewer.ipython.org/url/users.ugent.be/~sleroux/asd3.ipynb
[img1]: https://raw.githubusercontent.com/EMerckx/design-and-development-of-mobile-applications/master/lab07/res/service_lifecycle.png