# Lab 9: Hybrid applications

## Install Cordova

Install node.js via https://nodejs.org/en/ and restart the computer

In cmd (it is best to run it as Administrator)

```
npm install -g cordova
set PATH=%PATH%;C:\Users\<username>\AppData\Roaming\npm
set PATH=%PATH%;C:\Users\<username>\AppData\Local\Android\sdk\tools
set JAVA_HOME="C:\Program Files\Java\jdk1.8.0_60"
```

And in the SDK Manager we install the SDK Tools

```
Android Studio > Tools > Android > SDK Manager > Appearance & Behavior 
	> System Settings > Android SDK > SDK Tools > Android SDK Tools 24.4.1
```

NOTE: after we close our cmd, the changes applied to the variables PATH and JAVA_HOME will be lost. To make it persistent, use the command ```setx```

## A new hybrid application

Create a new app

```
cordova create lab09 com.emerckx.lab09 Lab09
```

Add the android version

```
cd lab09
cordova platform add android
```

Build the app

```
cordova build
```

Emulate the app. This needs to be run as an administrator, otherwise it will just prompt "Waiting for emulator"

```
cordova emulate android
```

If an emulator has already started, we can use the run command

```
cordova run android
```

## A hybrid version of a web app 

We copy paste the code of lab08 (html, css and javascript) in this project's www directory. After we run the code, we see that everything works.

To run the app on a physical device, use the following command

```
cordova run android --device
```

## Scheduling native notifications

Install the Cordova Local-Notification Plugin

```
cordova plugin add https://github.com/katzer/cordova-plugin-local-notifications
````

Add the plugin script to the index.html file

```html
<script type="text/javascript" src="cordova.js"></script>
```

Add the notification code to the javascript file

```javascript
function showNotificationAfterSeconds(message, seconds, ledColor) {
    var now = new Date().getTime();
    var xSecondsFromNow = new Date(now + seconds * 1000);
    if (ledColor === undefined) {
        ledColor = "FFFFFF";
    }

    cordova.plugins.notification.local.schedule({
        text: message,
        at: xSecondsFromNow,
        led: ledColor,
        sound: null
    });
}
```


