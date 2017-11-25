package hu.ait.android.weatherinfo.touch;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import hu.ait.android.weatherinfo.adapter.CitiesAdapter;

public class CityListTouchHelperCallback extends ItemTouchHelper.Callback {
    private final CitiesAdapter citiesAdapter;

    public CityListTouchHelperCallback(CitiesAdapter citiesAdapter) {
        this.citiesAdapter = citiesAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        citiesAdapter.onCityMove(
                viewHolder.getAdapterPosition(),
                target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        citiesAdapter.deleteCity(viewHolder.getAdapterPosition());
    }
}
