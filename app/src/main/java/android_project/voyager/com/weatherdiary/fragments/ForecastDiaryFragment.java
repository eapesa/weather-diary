package android_project.voyager.com.weatherdiary.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android_project.voyager.com.weatherdiary.R;

/**
 * Created by eapesa on 7/13/15.
 */
public class ForecastDiaryFragment extends Fragment {
    public static ForecastDiaryFragment newInstance(int sectionNumber) {
        ForecastDiaryFragment fragment = new ForecastDiaryFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weatherdiary_forecastdiary_fragment, container, false);
        return rootView;
    }
}
