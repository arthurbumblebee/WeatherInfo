package hu.ait.android.weatherinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        setUpToolBar();

        TextView tvCityName = findViewById(R.id.tvCityName);
        TextView tvCityTemperature = findViewById(R.id.tvCityTemperature);
        TextView tvCityHumidity = findViewById(R.id.tvCityHumidity);
        ImageView ivIcon = findViewById(R.id.ivIcon);
        TextView tvClouds = findViewById(R.id.tvClouds);
        TextView tvMaxTemp = findViewById(R.id.tvMaxTemp);
        TextView tvMinTemp = findViewById(R.id.tvMinTemp);
        TextView tvSunrise = findViewById(R.id.tvSunrise);
        TextView tvSunset = findViewById(R.id.tvSunset);
        TextView tvPressure = findViewById(R.id.tvPressure);
        TextView tvVisibility = findViewById(R.id.tvVisibility);
        TextView tvWeatherDesc = findViewById(R.id.tvWeatherDesc);
        TextView tvWind = findViewById(R.id.tvWind);
        ImageView kenBurnsView = findViewById(R.id.ken_burns_image);


        if (getIntent().hasExtra(CityListActivity.CITY_NAME)) {

            String cityName = getIntent().getStringExtra(CityListActivity.CITY_NAME);
            tvCityName.setText(cityName);
            tvCityTemperature.setText(getIntent().getStringExtra(CityListActivity.CITY_TEMPERATURE));

            String maxTemp = getIntent().getStringExtra(CityListActivity.TEMP_MAX);
            tvMaxTemp.setText(getString(R.string.max_temp, maxTemp));

            String minTemp = getIntent().getStringExtra(CityListActivity.TEMP_MIN);
            tvMinTemp.setText(getString(R.string.min_temp, minTemp));

            String sunrise = getIntent().getStringExtra(CityListActivity.CITY_SUNRISE);
            tvSunrise.setText(getString(R.string.sun_rise, sunrise));

            String sunset = getIntent().getStringExtra(CityListActivity.CITY_SUNSET);
            tvSunset.setText(getString(R.string.sunset, sunset));

            tvWeatherDesc.setText(getIntent().getStringExtra(CityListActivity.WEATHER_SUMMARY));

            String wind = getIntent().getStringExtra(CityListActivity.WIND);
            tvWind.setText(getString(R.string.wind_speed, wind));

            String clouds = getIntent().getStringExtra(CityListActivity.CITY_CLOUDS);
            tvClouds.setText(getString(R.string.cloud_cover, clouds));

            String visibility = getIntent().getStringExtra(CityListActivity.VISIBILITY);
            tvVisibility.setText(getString(R.string.visibility_units, visibility));

            String pressure = getIntent().getStringExtra(CityListActivity.CITY_PRESSURE);
            tvPressure.setText(getString(R.string.pressure_units, pressure));

            String humidity = getIntent().getStringExtra(CityListActivity.CITY_HUMIDITY);
            tvCityHumidity.setText(getString(R.string.humidity_value, humidity));

            String weather_icon = getIntent().getStringExtra(CityListActivity.CITY_ICON);
            Glide.with(this).load("http://openweathermap.org/img/w/" + weather_icon + ".png").into(ivIcon);
            Glide.with(this).load("http://openweathermap.org/img/w/" + weather_icon + ".png").into(kenBurnsView);

        } else {
            tvCityTemperature.setText(R.string.no_data);
        }
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(R.drawable.action_bar_icon);
        }
    }
}
