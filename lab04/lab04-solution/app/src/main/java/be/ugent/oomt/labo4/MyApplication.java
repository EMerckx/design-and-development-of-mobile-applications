package be.ugent.oomt.labo4;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import be.ugent.oomt.labo4.contentprovider.MessageProvider;

/**
 * Created by elias on 20/01/15.
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        //MessageProvider.addTestData(context);
        MqttHandler.getInstance();
    }
    public static Context getContext(){
        return context;
    }
    // TODO: add static getter method for context;
}
