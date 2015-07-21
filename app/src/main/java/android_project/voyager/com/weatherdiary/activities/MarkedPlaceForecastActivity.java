package android_project.voyager.com.weatherdiary.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
public class MarkedPlaceForecastActivity extends Activity implements
        ListView.OnItemClickListener {
    private TextView mPlaceNameTextView;
    private TextView mForecastTimeTextView;
    private ListView mForecastListView;
    private DailyForecastAdapter dailyForecastAdapter;
    private MarkedPlacesWeatherDAO weatherDAO;
    private String mCurrentMarker;
    private int mTempType;

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
        mTempType = 1;
        mPlaceNameTextView = (TextView) findViewById
                (R.id.weatherdiary_markedplace_placename_textview);
        mForecastTimeTextView = (TextView) findViewById
                (R.id.weatherdiary_markedplace_forecastTime_textview);
        mForecastListView = (ListView) findViewById
                (R.id.weatherdiary_markedplace_listview);
        mForecastListView.setOnItemClickListener(this);
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
        dailyForecastAdapter = new DailyForecastAdapter(this, weathers, mTempType);
        mForecastListView.setAdapter(dailyForecastAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mTempType = mTempType * -1;
        dailyForecastAdapter.changeTempUnit();
        dailyForecastAdapter.notifyDataSetChanged();
    }
}
