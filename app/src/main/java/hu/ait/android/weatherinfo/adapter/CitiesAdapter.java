package hu.ait.android.weatherinfo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import hu.ait.android.weatherinfo.CityListActivity;
import hu.ait.android.weatherinfo.R;
import hu.ait.android.weatherinfo.data.City;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {

    private int lastPosition = -1;
    private final List<City> cityList;
    private final Context context;

    public CitiesAdapter(List<City> cityList, Context context) {
        this.cityList = cityList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cityRow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.city_row, parent, false);
        return new ViewHolder(cityRow);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        City city = cityList.get(position);

        holder.tvCityTemperature.setText(city.getCityTemperature());
        holder.tvCityName.setText(city.getCityName());
        holder.tvCityWeatherSummary.setText(city.getCityWeatherSummary());

        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCityDetails(holder.getAdapterPosition());
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCity(holder.getAdapterPosition());
            }
        });

        setAnimation(holder.itemView, position);

    }

    private void showCityDetails(int cityToViewPosition) {
        ((CityListActivity) context).viewCityDetails(cityToViewPosition);
    }

    public void deleteCity(int cityToDeletePosition) {
        ((CityListActivity) context).deleteCity(cityToDeletePosition);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void onCityMove(int fromAdapterPosition, int toAdapterPosition) {
        if (fromAdapterPosition < toAdapterPosition) {
            for (int i = fromAdapterPosition; i < toAdapterPosition; i++) {
                Collections.swap(cityList, i, i + 1);
            }
        } else {
            for (int i = fromAdapterPosition; i > toAdapterPosition; i--) {
                Collections.swap(cityList, i, i - 1);
            }
        }
        notifyItemMoved(fromAdapterPosition, toAdapterPosition);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvCityName;
        final TextView tvCityTemperature;
        final TextView tvCityWeatherSummary;
        final Button btnDelete;
        final Button btnDetails;

        ViewHolder(View itemView) {
            super(itemView);
            tvCityWeatherSummary = itemView.findViewById(R.id.tvCityWeatherSummary);
            tvCityName = itemView.findViewById(R.id.tvCityName);
            tvCityTemperature = itemView.findViewById(R.id.tvCurrentTemp);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }
    }

}
