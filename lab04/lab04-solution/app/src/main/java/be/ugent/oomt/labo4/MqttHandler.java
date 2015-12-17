package be.ugent.oomt.labo4;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.android.service.MqttTraceHandler;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import be.ugent.oomt.labo4.contentprovider.MessageProvider;
import be.ugent.oomt.labo4.contentprovider.database.DatabaseContract;

/**
 * Created by elias on 19/01/15.
 */
public class MqttHandler implements IMqttActionListener, MqttCallback, MqttTraceHandler
{

    private static final String TAG = MqttHandler.class.getCanonicalName();
    private static MqttHandler instance;
    private MqttAndroidClient client;

    // TODO: fill in IP or public Mqtt server
    // Mqtt server address
    //public static final String serverURI = "tcp://<IP>:1883";
    public static final String serverURI = "tcp://iot.eclipse.org:1883"; //public mqtt server

    // TODO: fill in client id (unique user email)
    // unique client id for identifying to Mqtt server
    public static final String clientId = "bart.vanmiegem@ugent.be";

    // TODO: fill in start topics
    // topics to subscribe to on application start
    public static final String[] start_topics = new String[]{"/users/+/state"};

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
    public static MqttHandler getInstance(){
        if(instance==null){
            instance = new MqttHandler(MyApplication.getContext());
        }
        return instance;
    }

    private void createClient() {
        // TODO: Create MqttAndroidClient, set callback, set tracecallback, set options and connect the client
        client = new MqttAndroidClient(context,serverURI, clientId);
        client.setCallback(this);
        client.setTraceCallback(this);
        client.setTraceEnabled(true);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setWill("/users/"+clientId+"/state", "Ik ben offline".getBytes(), qos, true);
        options.setCleanSession(false);

        try {
            client.connect(options, this);
            Log.i(this.getClass().toString(), "Ik ben online");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /*
        Use this method to get the Mqtt client from anywhere in your application. The client can be used to send messages and subscribe to new topics.
     */
    public MqttAndroidClient getClient() {
        return client;
    }

    @Override
    public void connectionLost(Throwable cause) {

        Log.d(getClass().getCanonicalName(), "MQTT Server connection lost" + cause.getMessage());
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        Log.d(getClass().getCanonicalName(), "Message arrived:" + s + ":" + mqttMessage.toString());
        AsyncQueryHandler handler = new AsyncQueryHandler(context.getContentResolver()) {
            @Override
            public void startQuery(int token, Object cookie, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
                super.startQuery(token, cookie, uri, projection, selection, selectionArgs, orderBy);
            }
        };
        if (s.contains("/state")) {
            String message = mqttMessage.getPayload().toString();
            String mes=new String(mqttMessage.getPayload());

            String topic = s;
            int eersteslash = topic.indexOf("/", 1);
            int tweedeslash = topic.indexOf("/", eersteslash + 1);
            topic = topic.substring(eersteslash + 1, tweedeslash);
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.Contact.COLUMN_NAME_CONTACT, topic);
            values.put(DatabaseContract.Contact.COLUMN_NAME_STATE, mes);
            handler.startInsert(1, null, MessageProvider.CONTACTS_CONTENT_URL, values);
        } else {
            String message = new String(mqttMessage.getPayload());
            String topic = s;
            int eersteslash = topic.indexOf("/", 1);
            topic = topic.substring(eersteslash + 1);
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.Message.COLUMN_NAME_CONTACT, topic);
            values.put(DatabaseContract.Message.COLUMN_NAME_MESSAGE, message);
            handler.startInsert(1, null, MessageProvider.MESSAGES_CONTENT_URL, values);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(getClass().getCanonicalName(), "Delivery complete");
    }

    @Override
    public void traceDebug(String source, String message) {

        Log.i(source,message);
    }

    @Override
    public void traceError(String source, String message) {

        Log.i(source,message);
    }

    @Override
    public void traceException(String source, String message, Exception e) {
        Log.i(source,message);
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {

        Log.i("gelukt","gelukt");
        if (client.isConnected()) {
            try {
                String message = "Connected!";
                client.publish("/users/" + clientId + "/state", message.getBytes(), qos, true);
                int[] TOS = new int[start_topics.length];
                for (int i = 0; i < TOS.length; i++) {
                    TOS[i] = qos;
                }
                client.subscribe(start_topics, TOS);

                //String[] topics = new String[]{"users/+"};
                String[] topics = new String[]{"users/+"};
                int[] qos = new int[]{2};
                try {
                    client.subscribe(topics, qos);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        Log.i("FAAL","FAAL");
    }

    // TODO: implement IMqttActionListener, MqttCallback interfaces and MqttTraceHandler
}
