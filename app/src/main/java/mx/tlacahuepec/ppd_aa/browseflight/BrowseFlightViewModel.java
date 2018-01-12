package mx.tlacahuepec.ppd_aa.browseflight;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import java.util.ArrayList;
import java.util.List;

import mx.tlacahuepec.ppd_aa.BR;
import mx.tlacahuepec.ppd_aa.data.Flight;
import mx.tlacahuepec.ppd_aa.data.source.FlightsDataSource;
import mx.tlacahuepec.ppd_aa.data.source.FlightsRepository;

/**
 * Created by santi on 1/9/2018.
 */

public class BrowseFlightViewModel extends BaseObservable {

    // These observable fields will update Views automatically
    public final ObservableList<Flight> items = new ObservableArrayList<>();
    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    private final FlightsRepository mFlightsRepository;

    // To avoid leaks, this must be an Application Context.
    private final Context mContext;

    private BrowseFlightNavigator mBrowseFlightNavigator;

    private boolean mIsDataLoaded = false;

    BrowseFlightViewModel(Context context, FlightsRepository flightsRepository) {
        // Force use of Application Context.
        mContext = context.getApplicationContext();
        mFlightsRepository = flightsRepository;
    }

    void setNavigator(BrowseFlightNavigator navigator) {
        mBrowseFlightNavigator = navigator;
    }

    void onActivityCreated(BrowseFlightNavigator navigator) {
        mBrowseFlightNavigator = navigator;
    }

    void onActivityDestroyed() {
        // Clear references to avoid potential memory leaks.
        mBrowseFlightNavigator = null;
    }

    @Bindable
    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void start() {
        loadFlights(true);
    }

    public void loadFlights(boolean forceUpdate) {
        loadFlights(forceUpdate, true);
    }

    private void loadFlights(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            dataLoading.set(true);
        }
        if (forceUpdate) {

            mFlightsRepository.refreshFlights();
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        //EspressoIdlingResource.increment(); // App is busy until further notice

        mFlightsRepository.getFlights(new FlightsDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Flight> tasks) {
                List<Flight> tasksToShow = new ArrayList<Flight>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                //if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                //    EspressoIdlingResource.decrement(); // Set app as idle.
                //}

                if (showLoadingUI) {
                    dataLoading.set(false);
                }

                items.clear();
                items.addAll(tasksToShow);
                // It's a @Bindable so update manually
                notifyPropertyChanged(BR.empty);
            }

            @Override
            public void onDataNotAvailable() {
                //mIsDataLoadingError.set(true);
            }
        });
    }

}
