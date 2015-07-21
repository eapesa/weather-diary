package android_project.voyager.com.weatherdiary.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android_project.voyager.com.weatherdiary.utils.Constants;

/**
 * Created by eapesa on 7/13/15.
 */
public class Utilities {
    public static String kelvinToCelsius(double kelvinTemp) {
        double converted = kelvinTemp - 273.15;
        return String.format("%.1f", converted);
    }

    public static String kelvinToFahrenheit(double kelvinTemp) {
        double converted = ((kelvinTemp - 273.15) * 9 / 5) + 32;
        return String.format("%.1f", converted);
    }

    public static String celsiusToFahrenheit(String temp) {
        double celsiusTemp = Double.parseDouble(temp.split("°")[0]);
        double fahrenTemp = (celsiusTemp * 1.8) + 32;
        return String.valueOf(fahrenTemp) + Constants.FAHRENHEIT_UNIT;
    }

    public static String fahrenheitToCelsius(String temp) {
        double fahrenTemp = Double.parseDouble(temp.split("°")[0]);
        double celsiusTemp = (fahrenTemp - 32) / 1.8;
        return String.valueOf(celsiusTemp) + Constants.CELSIUS_UNIT;
    }

    public static boolean checkNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
