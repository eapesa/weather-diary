package android_project.voyager.com.weatherdiary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by eapesa on 7/13/15.
 */
public class Utilities {
    private static String mTitle;
    public static String onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = Labels.SECTION1;
                break;
            case 2:
                mTitle = Labels.SECTION2;
                break;
            case 3:
                mTitle = Labels.SECTION3;
                break;
        }
        return mTitle;
    }

    public static String kelvinToCelsius(int kelvinTemp) {
        double converted = kelvinTemp - 273.15;
        return String.format("%.1f", converted);
    }

    public static String kelvinToFahrenheit(int kelvinTemp) {
        double converted = ((kelvinTemp - 273.15) * 9 / 5) + 32;
        return String.format("%.1f", converted);
    }

    public static boolean checkNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
