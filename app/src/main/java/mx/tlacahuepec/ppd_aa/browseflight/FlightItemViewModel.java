package mx.tlacahuepec.ppd_aa.browseflight;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;


import mx.tlacahuepec.ppd_aa.data.Flight;
import mx.tlacahuepec.ppd_aa.data.source.FlightsRepository;

/**
 * Created by santi on 1/12/2018.
 */

public class FlightItemViewModel extends BaseObservable {

    public final ObservableField<String> flightId = new ObservableField<>();
    public final ObservableField<String> flightName = new ObservableField<>();
    public final ObservableField<Boolean> flightSaved = new ObservableField<>();

    private final ObservableField<Flight> mFlightObservable = new ObservableField<>();


    private final FlightsRepository mFlightsRepository;

    private final Context mContext;

    public FlightItemViewModel(Context context, FlightsRepository flightsRepository) {
        mContext = context.getApplicationContext(); // Force use of Application Context.
        mFlightsRepository = flightsRepository;

        // Exposed observables depend on the mTaskObservable observable:
        mFlightObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Flight flight = mFlightObservable.get();
                if (flight != null) {
                    flightId.set(flight.getId());
                    flightName.set(flight.getName());
                    flightSaved.set(flight.isSaved());
                }
            }
        });
    }
    //flightClicked
    //saveToWishlist
    //flightName

    @Bindable
    public boolean getSaveToWishlist() {
        Flight flight = mFlightObservable.get();
        return flight != null && flight.isSaved();
    }

    public void setSaveToWishlist(boolean saved) {
        Flight flight = mFlightObservable.get();

        // Notify repository and user
        if (saved) {
            mFlightsRepository.updateFlight(flight);
        }
    }

    // This could be an observable, but we save a call to Task.getTitleForList() if not needed.
    @Bindable
    public String getFlightName() {
        if (mFlightObservable.get() == null) {
            return "No data";
        }
        return mFlightObservable.get().getName();
    }

    public void flightClicked() {
        //TODO: finish implementation
    }

}
