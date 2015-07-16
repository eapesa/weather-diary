package android_project.voyager.com.weatherdiary.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import android_project.voyager.com.weatherdiary.R;
import android_project.voyager.com.weatherdiary.models.CurrentWeather;
import android_project.voyager.com.weatherdiary.utils.Constants;
import android_project.voyager.com.weatherdiary.utils.Utilities;

/**
 * Created by eapesa on 7/14/15.
 */
public class NewForecastActivity extends Activity {
    TextView mPlaceNameTextView;
    TextView mCelsiusTextView;
    TextView mFahrenheitTextView;
    TextView mCloudinessTextView;
    TextView mWindTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weatherdiary_newforecast_activity);

        initializeViews();
        processIntentExtras();
    }

    /*
     * Initializers
     */
    private void initializeViews () {
        mPlaceNameTextView = (TextView) findViewById
                (R.id.weatherdiary_newforecast_placename_textview);
        mCelsiusTextView = (TextView) findViewById
                (R.id.weatherdiary_newforecast_celsius_textview);
        mFahrenheitTextView = (TextView) findViewById
                (R.id.weatherdiary_newforecast_fahrenheit_textview);
        mCloudinessTextView = (TextView) findViewById
                (R.id.weatherdiary_newforecast_cloudiness_textview);
        mWindTextView = (TextView) findViewById
                (R.id.weatherdiary_newforecast_wind_textview);
    }

    /*
     * Custom Methods
     */
    private void processIntentExtras() {
        Bundle bundle = getIntent().getExtras();

        String namePlace = bundle.getString(Constants.ARGS_PLACENAME);
        String celsius = bundle.getString(Constants.ARGS_CELSIUS);
        String fahrenheit = bundle.getString(Constants.ARGS_FAHRENHEIT);
        String cloudiness = bundle.getString(Constants.ARGS_CLOUDINESS);
        String windSpeed = bundle.getString(Constants.ARGS_WINDSPEED);
        String forecastTime = bundle.getString(Constants.ARGS_FORECAST_TIME);

        mPlaceNameTextView.setText(namePlace);
        mCelsiusTextView.setText(celsius);
        mFahrenheitTextView.setText(fahrenheit);
        mCloudinessTextView.setText(cloudiness);
        mWindTextView.setText(windSpeed);
    }

}
