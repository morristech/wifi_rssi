# wifi_rssi
An android application that retrieves the RSSI for each wireless network in the environment and reports this reading to an heroku API

The api enpoint can be found at:
https://mysterious-brushlands-97954.herokuapp.com
The application only uses the /wifiap path

### Application Design
The Android WiFi API is accessed via a System Service called WifiManager. Once we obtain a WifiManager service instance, we evaluate if the WiFi is enabled on the client’s device. If false, we enable it by calling the setWifiEnabled method by passing in true as a parameter 
We can start to scan the WiFi networks around us. For that, we need to use a dedicated BroadcastReceiver registered with the following intent :
WifiManager.SCAN_RESULTS_AVAILABLE_ACTION
Once the BroadcastReceiver is registered, we can start to scan the WiFi networks by calling the startScan method of the WifiManager service.

When the scanning is ended, the onReceive method of our BroadcastReceiver implementation if called. We need to call the getScanResults method of the WifiManager service to get the results.
Lastly we iterate  through the list of results returned by the WifiManager’s getScanResults method, creating a new instance of WifiAccessPoint model for each result then saving each model to the local db and then posting it to the remote api mentioned above.

### Third party libs used
| Name             | Description   |
| :-------------:|--------------|
| [Retrofit](https://square.github.io/retrofit/) |  A type-safe HTTP client for Android and Java. |
| [Moshi](https://github.com/square/moshi) | Moshi is a JSON library for Android to parse JSON into Java objects. |
| [Room](https://developer.android.com/topic/libraries/architecture/room) | A persistence library provides an abstraction layer over SQLite. |
