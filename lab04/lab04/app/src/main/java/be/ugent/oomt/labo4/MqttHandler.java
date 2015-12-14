package be.ugent.oomt.labo4;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;

/**
 * Created by elias on 19/01/15.
 */
public class MqttHandler {

    private static final String TAG = MqttHandler.class.getCanonicalName();
    private static MqttHandler instance;
    private MqttAndroidClient client;

    // TODO: fill in IP or public Mqtt server
    // Mqtt server address
    public static final String serverURI = "tcp://<IP>:1883";
    //public static final String serverURI = "tcp://iot.eclipse.org:1883"; //public mqtt server

    // TODO: fill in client id (unique user email)
    // unique client id for identifying to Mqtt server
    public static final String clientId = "<e-mail>";

    // TODO: fill in start topics
    // topics to subscribe to on application start
    public static final String[] start_topics = new String[]{};

    // Quality Of Service levels
    // 0: msg are only delivered when online, 1: msg are send once, 2: msg are send and checked
    public static final int qos = 2;

    // when connection is lost do not clean up the subscriptions of the user
    private final boolean cleanSession = false;
    private final Context context;

    private MqttHandler(Context context) {
        this.context = context;
        createClient();
    }

    // TODO: Create singleton class with the application context (to start the MqttHandler when your application starts, create instance of this class in MyApplication)

    private void createClient() {
        // TODO: Create MqttAndroidClient, set callback, set tracecallback, set options and connect the client
        client = null;
    }

    /*
        Use this method to get the Mqtt client from anywhere in your application. The client can be used to send messages and subscribe to new topics.
     */
    public MqttAndroidClient getClient() {
        return client;
    }

    // TODO: implement IMqttActionListener, MqttCallback interfaces and MqttTraceHandler
}
