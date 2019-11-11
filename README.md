# wifi_rssi
An android application that retrieves the RSSI for each wireless network in the environment and reports this reading to an heroku API

The api enpoint can be found at:
https://mysterious-brushlands-97954.herokuapp.com
The application only uses the /wifiap path

### Third party libs used
| Name             | Description   |
| :-------------:|--------------|
| [Retrofit](https://square.github.io/retrofit/) |  A type-safe HTTP client for Android and Java. |
| [Moshi](https://github.com/square/moshi) | Moshi is a JSON library for Android to parse JSON into Java objects. |
