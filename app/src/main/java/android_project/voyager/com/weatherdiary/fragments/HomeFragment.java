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
public class HomeFragment extends Fragment {

    public static HomeFragment newInstance(int sectionNumber) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.weatherdiary_home_activity, container, false);
        return rootView;
    }
}
