package hu.ait.android.weatherinfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import hu.ait.android.weatherinfo.adapter.CitiesAdapter;
import hu.ait.android.weatherinfo.data.City;
import hu.ait.android.weatherinfo.data.WeatherResult;
import hu.ait.android.weatherinfo.network.WeatherAPI;
import hu.ait.android.weatherinfo.touch.CityListTouchHelperCallback;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class CityListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String CITY_NAME = "CITY_NAME";
    public static final String CITY_TEMPERATURE = "CITY_TEMPERATURE";
    public static final String CITY_HUMIDITY = "CITY_HUMIDITY";
    public static final String CITY_CLOUDS = "CITY_CLOUDS";
    public static final String CITY_ICON = "CITY_ICON";
    public static final String CITY_PRESSURE = "CITY_PRESSURE";
    public static final String CITY_SUNRISE = "CITY_SUNRISE";
    public static final String CITY_SUNSET = "CITY_SUNSET";
    public static final String TEMP_MAX = "TEMP_MAX";
    public static final String TEMP_MIN = "TEMP_MIN";
    public static final String VISIBILITY = "VISIBILITY";
    public static final String WEATHER_SUMMARY = "WEATHER_SUMMARY";
    public static final String WIND = "WIND";
    private CitiesAdapter citiesAdapter;
    private CoordinatorLayout layoutContent;
    private static final String KEY_FIRST = "KEY_FIRST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setUpToolBar();

        ((WeatherApplication) getApplication()).openRealm();

        RealmResults<City> allCities = getRealm().where(City.class).findAll();

        City citiesArray[] = new City[allCities.size()];
        List<City> citiesResult = new ArrayList<>(Arrays.asList(allCities.toArray(citiesArray)));

        citiesAdapter = new CitiesAdapter(citiesResult, this);
        RecyclerView recyclerViewCities = findViewById(R.id.recyclerViewCities);
        recyclerViewCities.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCities.setAdapter(citiesAdapter);

        CityListTouchHelperCallback cityListTouchHelperCallback = new CityListTouchHelperCallback(
                citiesAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(
                cityListTouchHelperCallback);
        touchHelper.attachToRecyclerView(recyclerViewCities);

        FloatingActionButton fab = findViewById(R.id.btnAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCityDialog();
            }
        });

        layoutContent = findViewById(R.id.layoutContent);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (isFirstStart()) {
            new MaterialTapTargetPrompt.Builder(CityListActivity.this)
                    .setTarget(findViewById(R.id.btnAdd))
                    .setPrimaryText(R.string.a_new_city)
                    .setSecondaryText(R.string.create_new_city)
                    .show();
        }


    }


    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(R.drawable.action_bar_icon);
        }
    }

    private boolean isFirstStart() {
        SharedPreferences sp =
                PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getBoolean(KEY_FIRST, true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        saveFirstStartFlag();
    }

    private void saveFirstStartFlag() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(KEY_FIRST, false);
        edit.apply();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.city_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all:
                deleteAll();
                return true;
            case R.id.action_add_city:
                showAddCityDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            showSnackBarMessage(getString(R.string.about_message));
        } else if (id == R.id.nav_add_city) {
            showAddCityDialog();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Realm getRealm() {
        return ((WeatherApplication) getApplication()).getRealCities();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((WeatherApplication) getApplication()).closeRealm();
    }

    private void showSnackBarMessage(String message) {
        Snackbar.make(layoutContent,
                message,
                Snackbar.LENGTH_LONG
        ).setAction(R.string.hide, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...
            }
        }).show();
    }

    public void deleteCity(final int cityToDeletePosition) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CityListActivity.this);
        builder.setTitle(R.string.attention);
        builder.setTitle(R.string.confirm_delete_item);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<City> cityList = citiesAdapter.getCityList();
                final City city = cityList.get(cityToDeletePosition);

                getRealm().beginTransaction();

                city.deleteFromRealm();
                cityList.remove(cityToDeletePosition);
                citiesAdapter.notifyItemRemoved(cityToDeletePosition);

                getRealm().commitTransaction();

                showSnackBarMessage(getString(R.string.deleted_an_item));
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                citiesAdapter.notifyItemChanged(cityToDeletePosition);
                showSnackBarMessage(getString(R.string.deletion_cancelled));
            }
        });
        builder.show();
    }

    private void deleteAll() {

        AlertDialog.Builder builder = new AlertDialog.Builder(CityListActivity.this);
        builder.setTitle(R.string.attention);
        builder.setTitle(R.string.confirm_delete_all);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                getRealm().beginTransaction();

                int sizeOfCityList = citiesAdapter.getItemCount();
                List<City> cityList = citiesAdapter.getCityList();
                if (sizeOfCityList > 0) {
                    getRealm().deleteAll();
                    cityList.clear();
                    citiesAdapter.notifyDataSetChanged();
                }
                getRealm().commitTransaction();
                showSnackBarMessage(getString(R.string.deleted_all_items));
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                showSnackBarMessage(getString(R.string.deletion_cancelled));
            }
        });
        builder.show();
    }

    private void addCity(final String cityName) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);

        Call<WeatherResult> call
                = weatherAPI.getWeather(
                cityName,
                getString(R.string.units_metric),
                getString(R.string.app_id));

        call.enqueue(new Callback<WeatherResult>() {
            @Override
            public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
                if (response.isSuccessful()) {
                    getRealm().beginTransaction();

                    List<City> cityList = citiesAdapter.getCityList();

                    City newCity = getRealm().createObject(City.class, UUID.randomUUID().toString());

                    newCity.setCityName(cityName);
                    newCity.setCityTemperature(response.body().getMain().getTemp().toString());
                    newCity.setCityHumidity(response.body().getMain().getHumidity().toString());
                    newCity.setCityWeatherSummary(response.body().getWeather().get(0).getDescription());
                    newCity.setCityClouds(response.body().getClouds().getAll().toString());
                    newCity.setCityIcon(response.body().getWeather().get(0).getIcon());
                    newCity.setCityPressure(response.body().getMain().getPressure().toString());
                    newCity.setCityTempMax(response.body().getMain().getTempMax().toString());
                    newCity.setCityTempMin(response.body().getMain().getTempMin().toString());
                    newCity.setCityVisibility(response.body().getVisibility().toString());
                    newCity.setCityWind(response.body().getWind().getSpeed().toString());

                    String sunrise = new SimpleDateFormat(getString(R.string.date_format), Locale.GERMANY)
                            .format(new Date(response.body().getSys().getSunrise() * 1000L).getTime());
                    String sunset = new SimpleDateFormat(getString(R.string.date_format), Locale.GERMANY)
                            .format(new Date(response.body().getSys().getSunset() * 1000L).getTime());

                    newCity.setCitySunrise(sunrise);
                    newCity.setCitySunset(sunset);

                    getRealm().commitTransaction();
                    cityList.add(0, newCity);
                    citiesAdapter.notifyItemInserted(0);

                    showSnackBarMessage(getString(R.string.you_added_an_item));

                } else {
                    showSnackBarMessage(getString(R.string.city_not_exist));
                }
            }

            @Override
            public void onFailure(Call<WeatherResult> call, Throwable t) {
                showSnackBarMessage(t.getMessage());
            }
        });

    }

    private void showAddCityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CityListActivity.this);
        builder.setTitle(R.string.new_city_name);

        final EditText input = new EditText(CityListActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(input.getText())) {
                    addCity(input.getText().toString());
                } else {
                    input.setError(getString(R.string.input_error));
                }
            }

        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                showSnackBarMessage(getString(R.string.city_add_cancelled));
            }
        });

        builder.show();
    }

    public void viewCityDetails(int cityToViewPosition) {
        Intent intentDetails = new Intent(CityListActivity.this,
                DetailsActivity.class);

        List<City> cityList = citiesAdapter.getCityList();
        final City city = cityList.get(cityToViewPosition);

        String cityName = city.getCityName();
        String cityTemperature = city.getCityTemperature();
        String cityHumidity = city.getCityHumidity();
        String cityClouds = city.getCityClouds();
        String cityIcon = city.getCityIcon();
        String cityPressure = city.getCityPressure();
        String citySunrise = city.getCitySunrise();
        String citySunset = city.getCitySunset();
        String cityTempMax = city.getCityTempMax();
        String cityTempMin = city.getCityTempMin();
        String cityVisibility = city.getCityVisibility();
        String cityWeatherSummary = city.getCityWeatherSummary();
        String cityWind = city.getCityWind();

        intentDetails.putExtra(CITY_NAME, cityName);
        intentDetails.putExtra(CITY_TEMPERATURE, cityTemperature);
        intentDetails.putExtra(CITY_HUMIDITY, cityHumidity);
        intentDetails.putExtra(CITY_CLOUDS, cityClouds);
        intentDetails.putExtra(CITY_ICON, cityIcon);
        intentDetails.putExtra(CITY_PRESSURE, cityPressure);
        intentDetails.putExtra(CITY_SUNRISE, citySunrise);
        intentDetails.putExtra(CITY_SUNSET, citySunset);
        intentDetails.putExtra(TEMP_MAX, cityTempMax);
        intentDetails.putExtra(TEMP_MIN, cityTempMin);
        intentDetails.putExtra(VISIBILITY, cityVisibility);
        intentDetails.putExtra(WEATHER_SUMMARY, cityWeatherSummary);
        intentDetails.putExtra(WIND, cityWind);

        startActivity(intentDetails);
    }
}
