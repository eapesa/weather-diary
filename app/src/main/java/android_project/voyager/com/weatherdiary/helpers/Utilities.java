package android_project.voyager.com.weatherdiary.helpers;

import android.content.Context;

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

    public static int getLargeImageResourceFromCode(Context context, String code) {
        int resourceId = 0;
        if ( code.equalsIgnoreCase("01d") || code.equalsIgnoreCase("01n") ) {
            resourceId = context.getResources().getIdentifier("clear_l",
                    "drawable", context.getPackageName());
        } else if ( code.equalsIgnoreCase("02d") || code.equalsIgnoreCase("02n") ) {
            resourceId = context.getResources().getIdentifier("few_clouds_l",
                    "drawable", context.getPackageName());
        } else if ( code.equalsIgnoreCase("03d") || code.equalsIgnoreCase("03n") ) {
            resourceId = context.getResources().getIdentifier("scattered_clouds_l",
                    "drawable", context.getPackageName());
        } else if ( code.equalsIgnoreCase("04d") || code.equalsIgnoreCase("04n") ) {
            resourceId = context.getResources().getIdentifier("broken_clouds_l",
                    "drawable", context.getPackageName());
        } else if ( code.equalsIgnoreCase("09d") || code.equalsIgnoreCase("09n") ) {
            resourceId = context.getResources().getIdentifier("shower_rain_l",
                    "drawable", context.getPackageName());
        } else if ( code.equalsIgnoreCase("10d") || code.equalsIgnoreCase("10n") ) {
            resourceId = context.getResources().getIdentifier("rain_l",
                    "drawable", context.getPackageName());
        } else if ( code.equalsIgnoreCase("11d") || code.equalsIgnoreCase("11n") ) {
            resourceId = context.getResources().getIdentifier("thunderstorm_l",
                    "drawable", context.getPackageName());
        } else if ( code.equalsIgnoreCase("13d") || code.equalsIgnoreCase("13n") ) {
            resourceId = context.getResources().getIdentifier("snow_l",
                    "drawable", context.getPackageName());
        } else {
            resourceId = context.getResources().getIdentifier("weather_icon_l",
                    "drawable", context.getPackageName());
        }

        return resourceId;
    }

    public static int getImageResourceFromCode(Context context, String code) {
        int resourceId = 0;
        if ( code.equalsIgnoreCase("01d") || code.equalsIgnoreCase("01n") ) {
            resourceId = context.getResources().getIdentifier("clear_s",
                    "drawable", context.getPackageName());
        } else if ( code.equalsIgnoreCase("02d") || code.equalsIgnoreCase("02n") ) {
            resourceId = context.getResources().getIdentifier("few_clouds_s",
                    "drawable", context.getPackageName());
        } else if ( code.equalsIgnoreCase("03d") || code.equalsIgnoreCase("03n") ) {
            resourceId = context.getResources().getIdentifier("scattered_clouds_s",
                    "drawable", context.getPackageName());
        } else if ( code.equalsIgnoreCase("04d") || code.equalsIgnoreCase("04n") ) {
            resourceId = context.getResources().getIdentifier("broken_clouds_s",
                    "drawable", context.getPackageName());
        } else if ( code.equalsIgnoreCase("09d") || code.equalsIgnoreCase("09n") ) {
            resourceId = context.getResources().getIdentifier("shower_rain_s",
                    "drawable", context.getPackageName());
        } else if ( code.equalsIgnoreCase("10d") || code.equalsIgnoreCase("10n") ) {
            resourceId = context.getResources().getIdentifier("rain_s",
                    "drawable", context.getPackageName());
        } else if ( code.equalsIgnoreCase("11d") || code.equalsIgnoreCase("11n") ) {
            resourceId = context.getResources().getIdentifier("thunderstorm_s",
                    "drawable", context.getPackageName());
        } else if ( code.equalsIgnoreCase("13d") || code.equalsIgnoreCase("13n") ) {
            resourceId = context.getResources().getIdentifier("snow_s",
                    "drawable", context.getPackageName());
        } else {
            resourceId = context.getResources().getIdentifier("weather_icon_s",
                    "drawable", context.getPackageName());
        }

        return resourceId;
    }
}
