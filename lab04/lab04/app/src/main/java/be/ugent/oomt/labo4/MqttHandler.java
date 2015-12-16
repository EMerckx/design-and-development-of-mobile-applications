package be.ugent.oomt.labo4;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.android.service.MqttTraceHandler;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by elias on 19/01/15.
 */
public class MqttHandler {

    private static final String TAG = MqttHandler.class.getCanonicalName();
    private static MqttHandler instance;
    private MqttAndroidClient client;

    // DONE: fill in IP or public Mqtt server
    // Mqtt server address
    public static final String serverURI = "tcp://iot.eclipse.org:1883"; //"tcp://<IP>:1883";
    //public static final String serverURI = "tcp://iot.eclipse.org:1883"; //public mqtt server

    // DONE: fill in client id (unique user email)
    // unique client id for identifying to Mqtt server
    public static final String clientId = "mytest@ugent.be"; //"<e-mail>";

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

    // DONE: Create singleton class with the application context
    // (to start the MqttHandler when your application starts,
    // create instance of this class in MyApplication)
    public static MqttHandler getInstance(Context context){
        return new MqttHandler(context);
    }

    private void createClient() {
        // DONE: Create MqttAndroidClient
        client = new MqttAndroidClient(context, serverURI, clientId);

        // TODO: Set callback
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                Log.i("MqttHandler", "msg");
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                Log.i("MqttHandler", "msg");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                Log.i("MqttHandler", "msg");
            }
        });

        // TODO: Set tracecallback
        client.setTraceCallback(new MqttTraceHandler() {
            @Override
            public void traceDebug(String source, String message) {
                Log.i("MqttHandler", "traceDebug(String source, String message)");
            }

            @Override
            public void traceError(String source, String message) {
                Log.i("MqttHandler", "msg");
            }

            @Override
            public void traceException(String source, String message, Exception e) {
                Log.i("MqttHandler", "msg");
            }
        });

        // TODO: Set options
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        // if client disconnects, server remembers client (and does not clean up)
        mqttConnectOptions.setCleanSession(false);
        // set last will and testament message
        // on disconnect, let the other users know that you stop
        // by sending your last will message to your state topic
        for(int i=0; i<start_topics.length; i++){
            mqttConnectOptions.setWill(start_topics[i], null, qos, true);
        }

        // TODO: connect the client
        try {
            client.connect();
        } catch (MqttException e) {
            Log.e("MqttHandler", "client.connect() failed");
            e.printStackTrace();
        }



        /*
        code from paho:
        String topic        = "MQTT Examples";
        String content      = "Message from MqttPublishSample";
        int qos             = 2;
        String broker       = "tcp://iot.eclipse.org:1883";
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            //MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            //MqttConnectOptions connOpts = new MqttConnectOptions();
            //connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");
            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }*/
    }

    /*
        Use this method to get the Mqtt client from anywhere in your application. The client can be used to send messages and subscribe to new topics.
     */
    public MqttAndroidClient getClient() {
        return client;
    }

    // TODO: implement IMqttActionListener, MqttCallback interfaces and MqttTraceHandler
}
