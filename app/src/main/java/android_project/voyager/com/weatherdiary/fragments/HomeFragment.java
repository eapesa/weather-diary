package android_project.voyager.com.weatherdiary.fragments;

//import android.app.Fragment;
import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android_project.voyager.com.weatherdiary.R;
import android_project.voyager.com.weatherdiary.utils.Constants;
import android_project.voyager.com.weatherdiary.utils.Labels;
import android_project.voyager.com.weatherdiary.utils.Toasts;
import android_project.voyager.com.weatherdiary.utils.Utilities;

/**
 * Created by eapesa on 7/13/15.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, LocationListener {

    public static final String TAG = HomeFragment.class.getSimpleName();

    Button mButtonUpdate;
    TextView mTextViewPlaceName;
    TextView mTextViewCelsius;
    TextView mTextViewFahrenheit;
    TextView mTextViewCloudiness;
    TextView mTextViewWind;

    Context mContext;
    Criteria criteria;
    ProgressDialog mProgressDialogUpdate;
    LocationManager locationManager;
    SharedPreferences mSharedPrefs;
    SharedPreferences.Editor mSharedPrefsEditor;

    String contentTypeLabel = "Content-Type";
    String contentTypeValue = "application/json";
    String httpMethod = "GET";

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weatherdiary_home_fragment, container, false);

        mTextViewPlaceName = (TextView) rootView.findViewById
                (R.id.weatherdiary_home_placename_textview);
        mTextViewCelsius = (TextView) rootView.findViewById
                (R.id.weatherdiary_home_celsius_textview);
        mTextViewFahrenheit = (TextView) rootView.findViewById
                (R.id.weatherdiary_home_fahrenheit_textview);
        mTextViewCloudiness = (TextView) rootView.findViewById
                (R.id.weatherdiary_home_cloudiness_textview);
        mTextViewWind = (TextView) rootView.findViewById
                (R.id.weatherdiary_home_wind_textview);
        mButtonUpdate = (Button) rootView.findViewById
                (R.id.weatherdiary_home_updateforecast_button);
        mButtonUpdate.setOnClickListener(this);

        mProgressDialogUpdate = new ProgressDialog(this.getActivity());
        mProgressDialogUpdate.setMessage(Labels.HOME_UPDATE_FORECAST);

//        mContext = rootView.getContext();
        mSharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        mSharedPrefsEditor = mSharedPrefs.edit();

        criteria = new Criteria();
        locationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);

        if ( locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ){
            locationManager.requestSingleUpdate(criteria, this, Looper.getMainLooper());
        } else {
            Toast.makeText(getActivity(), Toasts.NO_GPS, Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    /***
     * @ Button Click Listener Methods
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weatherdiary_home_updateforecast_button:
                if ( locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ){
                    locationManager.requestSingleUpdate(criteria, HomeFragment.this, Looper.getMainLooper());
                } else {
                    Toast.makeText(getActivity(), Toasts.NO_GPS, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /***
     * @ Location Listener Methods
     */
    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(getActivity(), Toasts.NO_GPS, Toast.LENGTH_SHORT).show();
            return;
        }

        double latitude;
        double longitude;
        String latitudeString = mSharedPrefs.getString(Constants.CURRENT_LAT_LABEL, null);
        String longitudeString = mSharedPrefs.getString(Constants.CURRENT_LONG_LABEL, null);
        if (latitudeString == null && longitudeString == null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            mSharedPrefsEditor.putString(Constants.CURRENT_LAT_LABEL, String.valueOf(latitude));
            mSharedPrefsEditor.putString(Constants.CURRENT_LONG_LABEL, String.valueOf(longitude));
            mSharedPrefsEditor.commit();
        } else {
            latitude = Double.parseDouble(latitudeString);
            longitude = Double.parseDouble(longitudeString);
        }
        Log.d(TAG, "Getting coordinates... LAT: " + latitude + " LON: " + longitude);

        Log.d("HELLO", "### getActivity is "+mContext);
        if (Utilities.checkNetworkConnection(mContext)) {
            getWeatherForecast(latitude, longitude);
        } else {
            Toast.makeText(getActivity(), Toasts.NO_NETWORK, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /*
     * @ Custom functionalities
     */
    private void getWeatherForecast(final double latitude, final double longitude) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialogUpdate.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                InputStream inputStream = null;
                HttpURLConnection urlConnection;
                int statusCode;

                try {
                    URL url = new URL(Constants.WEATHER_API + "?lat=" + latitude
                            + "&lon=" + longitude);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty(contentTypeLabel, contentTypeValue);
                    urlConnection.setRequestMethod(httpMethod);
                    statusCode = urlConnection.getResponseCode();
                    inputStream = urlConnection.getInputStream();

                    if (statusCode == 200) {
                        return readInputStream(inputStream);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String response) {
                mProgressDialogUpdate.hide();
                Log.d(TAG, "RESPONSE: " + response);
                try {
                    updateForecast(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }

    private void updateForecast(String json) throws JSONException {
        JSONObject mainJson = new JSONObject(json);

        if (mainJson.getInt("cod") != 200) {
            Toast.makeText(getActivity(), Toasts.WEATHER_API_FAILED, Toast.LENGTH_SHORT).show();
            return;
        }

        String city = mainJson.getString("name");
        String country = mainJson.getJSONObject("sys").getString("country");
        String namePlace = city + ", " + country;
        mSharedPrefsEditor.putString(Constants.CURRENT_PLACENAME_LABEL, namePlace);
        mSharedPrefsEditor.commit();
        mTextViewPlaceName.setText(namePlace);

        int mainTempKelvin = mainJson.getJSONObject("main").getInt("temp");
        mTextViewCelsius.setText( Utilities.kelvinToCelsius(mainTempKelvin)
                + Constants.CELSIUS_UNIT );
        mTextViewFahrenheit.setText( Utilities.kelvinToFahrenheit(mainTempKelvin)
                + Constants.FAHRENHEIT_UNIT );

        String cloudiness = mainJson.getJSONArray("weather").getJSONObject(0).getString("description");
        mTextViewCloudiness.setText(cloudiness);

        double wind = mainJson.getJSONObject("wind").getDouble("speed");
        mTextViewWind.setText( String.valueOf(wind) + "m/s");
    }
}
