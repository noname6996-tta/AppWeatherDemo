package com.example.dubaothoitiet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinhthang.dubaothoitiet.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WeatherAdapter extends android.widget.BaseAdapter {
    Context context;
    int layout;
    List<ListAllWeather> list;

    public WeatherAdapter(Context context, int layout, List<ListAllWeather> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);
        TextView txtDate = view.findViewById(R.id.txtDate);
        TextView txtMinTemp = view.findViewById(R.id.txtMinTemp);
        TextView txtMaxTemp = view.findViewById(R.id.txtMaxTemp);
        ImageView imgWeather = view.findViewById(R.id.imgWeather);
        ListAllWeather weather = list.get(i);
        txtDate.setText(weather.getDay() + "");
        txtMaxTemp.setText(weather.getMaxTemp() + " °");
        txtMinTemp.setText(weather.getMinTemp() + " °");
        Picasso.get().load(weather.getWeather()).into(imgWeather);
        return view;
    }
}
