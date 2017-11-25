package hu.ait.android.weatherinfo;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

class WeatherApplication extends Application {

    private Realm realCities;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }

    public void openRealm() {
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realCities = Realm.getInstance(config);
    }

    public void closeRealm() {
        realCities.close();
    }

    public Realm getRealCities() {
        return realCities;
    }
}
