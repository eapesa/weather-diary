package android_project.voyager.com.weatherdiary;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import android_project.voyager.com.weatherdiary.R;
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
        try {
            processIntentExtras();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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

    private void processIntentExtras () throws JSONException {
        String intentExtra = getIntent().getStringExtra("open_api_response");
        JSONObject response = new JSONObject(intentExtra);

        String city = response.getString("name");
        String country = response.getJSONObject("sys").getString("country");
        String namePlace = city + ", " + country;
        Log.d("@@@NAMEPLACE", namePlace);
        mPlaceNameTextView.setText(namePlace);

        int mainTempKelvin = response.getJSONObject("main").getInt("temp");
        mCelsiusTextView.setText( Utilities.kelvinToCelsius(mainTempKelvin)
                + Constants.CELSIUS_UNIT );
        mFahrenheitTextView.setText( Utilities.kelvinToFahrenheit(mainTempKelvin)
                + Constants.FAHRENHEIT_UNIT );
        Log.d("@@@TEMPC", Utilities.kelvinToCelsius(mainTempKelvin) + "C");
        Log.d("@@@TEMPF", Utilities.kelvinToFahrenheit(mainTempKelvin) + "F");

        String cloudiness = response.getJSONArray("weather").getJSONObject(0).getString("description");
        mCloudinessTextView.setText(cloudiness);
        Log.d("@@@CLOUDINESS", cloudiness);

        double wind = response.getJSONObject("wind").getDouble("speed");
        mWindTextView.setText( String.valueOf(wind) + "m/s");
        Log.d("@@@WIND", String.valueOf(wind) + "m/s");
    }
}
