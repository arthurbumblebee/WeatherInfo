package hu.ait.android.weatherinfo.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class City extends RealmObject {

    @PrimaryKey
    private String cityId;
    private String cityIcon;
    private String cityName;
    private String cityTemperature;
    private String cityHumidity;
    private String cityWeatherSummary;
    private String cityPressure;
    private String citySunrise;
    private String citySunset;
    private String cityVisibility;
    private String cityTempMin;
    private String cityTempMax;
    private String cityWind;
    private String cityClouds;


    public City() {
    }


    public String getCityPressure() {
        return cityPressure;
    }

    public void setCityPressure(String cityPressure) {
        this.cityPressure = cityPressure;
    }

    public String getCitySunrise() {
        return citySunrise;
    }

    public void setCitySunrise(String citySunrise) {
        this.citySunrise = citySunrise;
    }

    public String getCitySunset() {
        return citySunset;
    }

    public void setCitySunset(String citySunset) {
        this.citySunset = citySunset;
    }

    public String getCityVisibility() {
        return cityVisibility;
    }

    public void setCityVisibility(String cityVisibility) {
        this.cityVisibility = cityVisibility;
    }

    public String getCityTempMin() {
        return cityTempMin;
    }

    public void setCityTempMin(String cityTempMin) {
        this.cityTempMin = cityTempMin;
    }

    public String getCityTempMax() {
        return cityTempMax;
    }

    public void setCityTempMax(String cityTempMax) {
        this.cityTempMax = cityTempMax;
    }

    public String getCityWind() {
        return cityWind;
    }

    public void setCityWind(String cityWind) {
        this.cityWind = cityWind;
    }

    public String getCityClouds() {
        return cityClouds;
    }

    public void setCityClouds(String cityClouds) {
        this.cityClouds = cityClouds;
    }

    public String getCityWeatherSummary() {
        return cityWeatherSummary;
    }

    public void setCityWeatherSummary(String cityWeatherSummary) {
        this.cityWeatherSummary = cityWeatherSummary;
    }

    public String getCityHumidity() {
        return cityHumidity;
    }

    public void setCityHumidity(String cityHumidity) {
        this.cityHumidity = cityHumidity;
    }


    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityTemperature() {
        return cityTemperature;
    }

    public void setCityTemperature(String cityTemperature) {
        this.cityTemperature = cityTemperature;
    }

    public String getCityIcon() {
        return cityIcon;
    }

    public void setCityIcon(String cityIcon) {
        this.cityIcon = cityIcon;
    }
}
