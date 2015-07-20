package android_project.voyager.com.weatherdiary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import android_project.voyager.com.weatherdiary.R;
import android_project.voyager.com.weatherdiary.models.MarkedPlace;

/**
 * Created by eapesa on 7/20/15.
 */
public class MarkedPlacesAdapter extends ArrayAdapter {

    private Context mContext;
    private ArrayList<MarkedPlace> mMarkedPlaces;

    public MarkedPlacesAdapter(Context context, ArrayList<MarkedPlace> markedPlaces) {
        super(context, R.layout.weatherdiary_forecastdiary_listview_row, markedPlaces);
        this.mContext = context;
        this.mMarkedPlaces = markedPlaces;
    }

    private static class ViewHolder {
        public TextView nameOfPlace;
        public TextView forecastTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.weatherdiary_forecastdiary_listview_row, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.nameOfPlace = (TextView) view.findViewById
                    (R.id.weatherdiary_forecastdiary_listview_place_textview);
            viewHolder.forecastTime = (TextView) view.findViewById
                    (R.id.weatherdiary_forecastdiary_listview_forecasttime_textview);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder vHolder = (ViewHolder) view.getTag();
        MarkedPlace markedPlace = mMarkedPlaces.get(position);

        vHolder.nameOfPlace.setText(markedPlace.nameOfPlace);
        vHolder.forecastTime.setText(markedPlace.forecastTime);

        return view;
    }
}
