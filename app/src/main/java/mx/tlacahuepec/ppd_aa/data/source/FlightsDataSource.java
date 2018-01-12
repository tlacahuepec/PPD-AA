package mx.tlacahuepec.ppd_aa.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import mx.tlacahuepec.ppd_aa.data.Flight;

/**
 * Created by santi on 1/11/2018.
 */

public interface FlightsDataSource {

    interface LoadTasksCallback {
        void onTasksLoaded(List<Flight> tasks);

        void onDataNotAvailable();
    }

    void getFlights(@NonNull LoadTasksCallback callback);

    void refreshFlights();

    void saveFlight(@NonNull Flight flight);

    void deleteFlight(@NonNull String flightId);

    void deleteAllFlights();

    void updateFlight(@NonNull Flight flight);
}
