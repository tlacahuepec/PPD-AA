package mx.tlacahuepec.ppd_aa.adapter;

import java.util.List;

import mx.tlacahuepec.ppd_aa.browseflight.BrowseFlightViewModel;
import mx.tlacahuepec.ppd_aa.data.source.FlightsRepository;

import mx.tlacahuepec.ppd_aa.data.Flight;

/**
 * Created by santi on 1/11/2018.
 */
public class FlightAdapter<Flight> extends DataBindingBaseAdapter {

    private final int layoutId;

    private final BrowseFlightViewModel mViewModel;

    private List<Flight> mFlights;

    private FlightsRepository mFlightsRepository;


    public FlightAdapter(OnItemClickListener itemClickListener, int layoutId,
                         List<Flight> flights, //TasksActivity taskItemNavigator,
                         FlightsRepository flightsRepository,
                         BrowseFlightViewModel flightsViewModel) {
        super(itemClickListener);
        this.layoutId = layoutId;

        //mTaskItemNavigator = taskItemNavigator;
        mFlightsRepository = flightsRepository;
        mViewModel = flightsViewModel;
        setList(flights);
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @Override
    protected Flight getItemForPosition(int position) {
        return mFlights.get(position);
    }

    @Override
    public int getItemCount() {
        return mFlights != null ? mFlights.size() : 0;
    }

    private void setList(List<Flight> flights) {
        mFlights = flights;
        notifyDataSetChanged();
    }
}