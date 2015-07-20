package android_project.voyager.com.weatherdiary.fragments;

//import android.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import android_project.voyager.com.weatherdiary.R;
import android_project.voyager.com.weatherdiary.activities.MarkedPlaceForecastActivity;
import android_project.voyager.com.weatherdiary.adapters.MarkedPlacesAdapter;
import android_project.voyager.com.weatherdiary.dao.MarkedPlacesWeatherDAO;
import android_project.voyager.com.weatherdiary.models.MarkedPlace;
import android_project.voyager.com.weatherdiary.utils.Constants;

/**
 * Created by eapesa on 7/13/15.
 */
public class ForecastDiaryFragment extends Fragment implements ListView.OnItemClickListener{

    private ListView mListView;
    private MarkedPlacesAdapter mMarkedAdapter;
    private Context mContext;
    private MarkedPlacesWeatherDAO mWeatherDAO;
    private ArrayList<MarkedPlace> mMarkedPlaces;

    public static ForecastDiaryFragment newInstance() {
        ForecastDiaryFragment fragment = new ForecastDiaryFragment();
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weatherdiary_forecastdiary_fragment, container, false);

        initializeHelpers();
        initializeViews(rootView);
        loadData();

        return rootView;
    }

    /*
     * Custom functionalities
     */
    private void initializeHelpers() {
        mWeatherDAO = new MarkedPlacesWeatherDAO(mContext);
    }

    private void initializeViews(View view) {
        mListView = (ListView) view.findViewById(R.id.weatherdiary_forecastdiary_listview);
        mListView.setOnItemClickListener(this);
    }

    private void loadData() {
        mMarkedPlaces = mWeatherDAO.getAllMarkedPlaces();
        mMarkedAdapter = new MarkedPlacesAdapter(mContext, mMarkedPlaces);
        mListView.setAdapter(mMarkedAdapter);
    }

    /*
     * ListView Item Click Listener
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MarkedPlace markedPlace = mMarkedPlaces.get(position);

        Intent intent = new Intent(getActivity().getApplicationContext(),
                MarkedPlaceForecastActivity.class);
        intent.putExtra(Constants.ARGS_MARKER, markedPlace.markerId);
        intent.putExtra(Constants.ARGS_PLACENAME, markedPlace.nameOfPlace);
        intent.putExtra(Constants.ARGS_FORECAST_TIME, markedPlace.forecastTime);
        startActivity(intent);
    }
}
