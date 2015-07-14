package android_project.voyager.com.weatherdiary.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android_project.voyager.com.weatherdiary.R;
import android_project.voyager.com.weatherdiary.utils.Constants;

/**
 * Created by eapesa on 7/13/15.
 */
public class GetForecastFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private static SupportMapFragment mGoogleMapFragment;
    SharedPreferences mSharedPrefs;
    GoogleMap mGoogleMap;

    private LatLng clickedLatLng;
    private double latitude;
    private double longitude;

    public static GetForecastFragment newInstance(int sectionNumber) {
        GetForecastFragment fragment = new GetForecastFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weatherdiary_getforecast_fragment, container, false);

        mSharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        latitude = Double.parseDouble(mSharedPrefs.getString(Constants.CURRENT_LAT_LABEL,
                Constants.DEFAULT_LAT_VALUE));
        longitude = Double.parseDouble( mSharedPrefs.getString(Constants.CURRENT_LONG_LABEL,
                Constants.DEFAULT_LONG_VALUE) );

        FragmentManager fragmentManager = getChildFragmentManager();
        mGoogleMapFragment = (SupportMapFragment) fragmentManager
                .findFragmentById(R.id.weatherdiary_getforecast_map_fragment);
        mGoogleMapFragment.getMapAsync(this);

        if (mGoogleMapFragment == null) {
            mGoogleMapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.weatherdiary_getforecast_map_fragment, mGoogleMapFragment)
                    .commit();
        }

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        LatLng currentLatLng = new LatLng(latitude, longitude);
        String placeLabel = mSharedPrefs.getString(Constants.CURRENT_PLACENAME_LABEL,
                Constants.DEFAULT_PLACENAME_VALUE);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                Constants.DEFAULT_ZOOM_VALUE));
        MarkerOptions mapMarker = new MarkerOptions().position(currentLatLng).title(placeLabel);
        Marker currentPlaceMarker = googleMap.addMarker(mapMarker);

        googleMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d("@@@ CLICKED PLACE", "LAT: " + latLng.latitude + " LONG: " + latLng.longitude);

        MarkerOptions mapMarker = new MarkerOptions().position(
                new LatLng(latLng.latitude, latLng.longitude)).title("New Place");
        getForecastDialog(mapMarker);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    /*
     * Miscellaneous
     */
    private void getForecastDialog(final MarkerOptions mapMarker) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = dialogBuilder
                .setTitle(Constants.ALERT_FORECAST_TITLE)
                .setMessage(Constants.ALERT_FORECAST_MESSAGE)
                .setPositiveButton(Constants.ALERT_FORECAST_AFFIRMATIVE_BUTTON_LABEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGoogleMap.addMarker(mapMarker);
                    }
                })
                .setNeutralButton(Constants.ALERT_FORECAST_NEGATIVE_BUTTON_LABEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        dialog.show();
    }
}
