package android_project.voyager.com.weatherdiary.adapters;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import android_project.voyager.com.weatherdiary.R;
import android_project.voyager.com.weatherdiary.models.Weather;

/**
 * Created by eapesa on 7/18/15.
 */
public class DailyForecastAdapter extends ArrayAdapter<Weather> {

    private Context mContext;
    private ArrayList<Weather> mWeathers;

    public DailyForecastAdapter(Context context, ArrayList<Weather> weathers) {
        super(context, R.layout.weatherdiary_markedplace_forecast_listview_row, weathers);
        this.mContext = context;
        this.mWeathers = weathers;
    }

    static class ViewHolder {
        public TextView forecastDay;
        public TextView forecastMonth;
        public TextView minTemp;
        public TextView maxTemp;
        public TextView description;
        public TextView windSpeed;
        public TextView cloudiness;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.weatherdiary_markedplace_forecast_listview_row, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.forecastDay = (TextView) view.findViewById
                    (R.id.weatherdiary_markedplace_forecast_listview_day_textview);
            viewHolder.forecastMonth = (TextView) view.findViewById
                    (R.id.weatherdiary_markedplace_forecast_listview_month_textview);
            viewHolder.minTemp = (TextView) view.findViewById
                    (R.id.weatherdiary_markedplace_forecast_listview_minTemp_textview);
            viewHolder.maxTemp = (TextView) view.findViewById
                    (R.id.weatherdiary_markedplace_forecast_listview_maxTemp_textview);
            viewHolder.description = (TextView) view.findViewById
                    (R.id.weatherdiary_markedplace_forecast_listview_description_textview);
            viewHolder.windSpeed = (TextView) view.findViewById
                    (R.id.weatherdiary_markedplace_forecast_listview_windSpeed_textview);
            viewHolder.cloudiness = (TextView) view.findViewById
                    (R.id.weatherdiary_markedplace_forecast_listview_cloudiness_textview);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder vHolder = (ViewHolder) view.getTag();
        Weather weather = mWeathers.get(position);

        vHolder.forecastDay.setText(weather.day);
        vHolder.forecastMonth.setText(weather.month);
        vHolder.minTemp.setText(weather.minTemp);
        vHolder.maxTemp.setText(weather.maxTemp);
        vHolder.description.setText(weather.forecastDescription);
        vHolder.windSpeed.setText(weather.windSpeed);
        vHolder.cloudiness.setText(weather.cloudiness);

        Log.d("@@@ AD", "DESC: " + weather.forecastDescription);
        Log.d("@@@ AD", "WIND SPEED: " + weather.windSpeed);

        return view;
    }
}
