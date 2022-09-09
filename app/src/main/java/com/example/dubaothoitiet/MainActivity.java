package com.example.dubaothoitiet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dinhthang.dubaothoitiet.R;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listViewForecast;
    WeatherAdapter adapter;
    List<ListAllWeather> list;
    ImageView imgWeatherCurrent;
    TextView txtAddress, txtTemp, txtHumid, txtCloud, txtWind, txtTime, txtDesc;
    SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd-MM HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        addData();
        addWeatherCurrent("hanoi");
    }

    private void addForecastWeather(String lat, String lon) {
        String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=ecb7a5561e836a4abe7958ef5779ac07&units=metric";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                list.clear();
                try {
                    JSONArray listWeather = response.getJSONArray("list");
                    for (int i = 0; i < listWeather.length(); i++) {
                        putDataForecastWeather(listWeather,response,i);
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.d("pdt", "onResponse: " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("pdt", "" + error.toString());
            }
        });
        requestQueue.add(jsonObject);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        MenuItem search = menu.findItem(R.id.itSearch);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(MainActivity.this, "Chuyên đổi địa điểm", Toast.LENGTH_SHORT).show();
                addWeatherCurrent(s);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void addWeatherCurrent(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=ecb7a5561e836a4abe7958ef5779ac07&units=metric";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    putDataWeatherCurrent(response);
                } catch (Exception e) {
                    Log.d("pdt", "onResponse: " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("pdt", "" + error.toString());
            }
        });
        requestQueue.add(jsonObject);
    }

    private void addData() {
        adapter = new WeatherAdapter(this, R.layout.line_weather, list);
        listViewForecast.setAdapter(adapter);
    }

    private void setData(String temp, String dateCurrent, String name, String humid, String desc, String wind, String cloudPercent, String urlImg) {
        txtTemp.setText(temp + "°");
        txtTime.setText(dateCurrent);
        txtAddress.setText(name);
        txtHumid.setText(humid + "%");
        txtDesc.setText(desc);
        txtWind.setText(wind + "m/s");
        txtCloud.setText(cloudPercent + "%");
        Picasso.get().load(urlImg).into(imgWeatherCurrent);
    }

    private void mapping() {
        listViewForecast = findViewById(R.id.lsvForecast);
        txtAddress = findViewById(R.id.txtAddress);
        txtTemp = findViewById(R.id.txtTemp);
        txtHumid = findViewById(R.id.txtHumid);
        txtCloud = findViewById(R.id.txtCloud);
        txtWind = findViewById(R.id.txtWind);
        txtTime = findViewById(R.id.txtTime);
        txtDesc = findViewById(R.id.txtDesc);
        imgWeatherCurrent = findViewById(R.id.imgWeatherCurrent);
        list = new ArrayList<>();
    }

    private void putDataWeatherCurrent (JSONObject response) throws JSONException {
        JSONArray weatherArray = response.getJSONArray("weather");
        JSONObject weatherObj = weatherArray.getJSONObject(0);
        JSONObject windy = response.getJSONObject("wind");
        JSONObject cloud = response.getJSONObject("clouds");
        JSONObject main = response.getJSONObject("main");
        JSONObject coord = response.getJSONObject("coord");
        String name = response.getString("name");
        long lngay = Long.parseLong(response.getString("dt"));
        Date date = new Date(lngay * 1000);
        String dateCurrent = dateFormat.format(date);
        String humid = main.getString("humidity");
        String temp = main.getString("temp");
        String lat = coord.getString("lat");
        String lon = coord.getString("lon");
        String cloudPercent = cloud.getString("all");
        String wind = windy.getString("speed");
        String icon = weatherObj.getString("icon");
        String urlImg = "https://openweathermap.org/img/wn/" + icon + "@2x.png?fbclid=IwAR1ys9cXhXXShFB6GyXqXn_DSHunzvQpu0sGoLdhVQJ1RDuLdvmHYIlp9Ns";
        String desc = weatherObj.getString("description");
        setData(temp, dateCurrent, name, humid, desc, wind, cloudPercent, urlImg);
        addForecastWeather(lat, lon);
    }

    private void putDataForecastWeather (JSONArray listWeather,JSONObject respone,int i) throws JSONException{
        JSONObject weather = listWeather.getJSONObject(i);
        JSONObject main = weather.getJSONObject("main");
        JSONArray weatherForecast = weather.getJSONArray("weather");
        String dt = weather.getString("dt");
        long lngay = Long.parseLong(dt);
        Date date = new Date(lngay * 1000);
        String dateCurrent = dateFormat.format(date);
        Float minTemp = Float.parseFloat(main.getString("temp_min"));
        Float maxTemp = Float.parseFloat(main.getString("temp_max"));
        Float humidity = Float.parseFloat(main.getString("humidity"));
        JSONObject detailWeather = weatherForecast.getJSONObject(0);
        String icon = detailWeather.getString("icon");
        String urlImg = "http://openweathermap.org/img/wn/" + icon + "@2x.png?fbclid=IwAR28YhluTsIrJKXCeIiwB7c7ZCM5z0MBABx8LMmrTWrUhuKnZREfZ9VOHvY";
        list.add(new ListAllWeather(dateCurrent, urlImg, humidity, maxTemp, minTemp));
    }
}