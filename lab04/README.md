# Lab 3: MQTT-based Twitter App Part 2

## Question 1

We need to access the MQTT client application wide. How can we accomplish this?
What is the best approach?

```
By the use of a singleton class.
```

## Question 2

Can we subscribe to all topics and if this is possible why is this a bad idea?

```
Too many messages?
```

## Question 3

Why is this application not ready for production? Can we fix this with MQTT?

```

```

## Set permissions

Add ACCESS_NETWORK_STATE, INTERNET and WAKE_LOCK permissions to the manifest file.

```xml
<permission android:name="ACCESS_NETWORK_STATE"/>
<permission android:name="INTERNET" />
<permission android:name="WAKE_LOCK" />
```

## MqttHandler

Set unique client id and server url

```java
public static final String serverURI = "tcp://iot.eclipse.org:1883";
public static final String clientId = "mytest@ugent.be";
```

Create MqttAndroidClient

```java
private void createClient() {
	client = new MqttAndroidClient(context, serverURI, clientId);
}
```

Set callbacks and connection options (see code for now)






