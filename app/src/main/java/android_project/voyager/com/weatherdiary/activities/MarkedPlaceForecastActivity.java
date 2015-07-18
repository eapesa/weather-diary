package android_project.voyager.com.weatherdiary.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import android_project.voyager.com.weatherdiary.R;
import android_project.voyager.com.weatherdiary.adapters.DailyForecastAdapter;
import android_project.voyager.com.weatherdiary.dao.MarkedPlacesWeatherDAO;
import android_project.voyager.com.weatherdiary.models.Weather;
import android_project.voyager.com.weatherdiary.utils.Constants;

/**
 * Created by eapesa on 7/14/15.
 */
public class MarkedPlaceForecastActivity extends Activity {
//    TextView mPlaceNameTextView;
//    TextView mCelsiusTextView;
//    TextView mFahrenheitTextView;
//    TextView mCloudinessTextView;
//    TextView mWindTextView;
    private TextView mPlaceNameTextView;
    private TextView mForecastTimeTextView;
    private ListView mForecastListView;
    private MarkedPlacesWeatherDAO weatherDAO;
    private String mCurrentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weatherdiary_markedplace_forecast_activity);

        initializeViews();
        initializeHelpers();
        processIntentExtras();
        populateListView();
    }

    /*
     * Initializers
     */
    private void initializeViews () {
        mPlaceNameTextView = (TextView) findViewById
                (R.id.weatherdiary_markedplace_placename_textview);
        mForecastTimeTextView = (TextView) findViewById
                (R.id.weatherdiary_markedplace_forecastTime_textview);
        mForecastListView = (ListView) findViewById
                (R.id.weatherdiary_markedplace_listview);
    }

    private void initializeHelpers() {
        weatherDAO = new MarkedPlacesWeatherDAO(this);
    }

    /*
     * Custom Methods
     */
    private void processIntentExtras() {
        mCurrentMarker = getIntent().getStringExtra(Constants.ARGS_MARKER);
        String nameOfPlace = getIntent().getStringExtra(Constants.ARGS_PLACENAME);
        String forecastTime = getIntent().getStringExtra(Constants.ARGS_FORECAST_TIME);

        mPlaceNameTextView.setText(nameOfPlace);
        mForecastTimeTextView.setText(forecastTime);
    }

    private void populateListView() {
        ArrayList<Weather> weathers = weatherDAO.getDailyForecast(mCurrentMarker);
        mForecastListView.setAdapter(new DailyForecastAdapter(this, weathers));
    }
}
