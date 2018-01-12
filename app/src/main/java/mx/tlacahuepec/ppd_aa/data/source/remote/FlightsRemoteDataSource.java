package mx.tlacahuepec.ppd_aa.data.source.remote;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.Map;

import mx.tlacahuepec.ppd_aa.data.Flight;
import mx.tlacahuepec.ppd_aa.data.source.FlightsDataSource;

/**
 * Created by santi on 1/11/2018.
 */

public class FlightsRemoteDataSource implements FlightsDataSource {

    private static FlightsRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 2000;

    private final static Map<String, Flight> FLIGHTS_SERVICE_DATA;

    static {
        FLIGHTS_SERVICE_DATA = new LinkedHashMap<>(2);
        addFlight("0", "PBC - DFW", false);
        addFlight("1", "MEX - SFO", false);
        addFlight("2", "PBC - LAX", false);
        addFlight("3", "DTW - OAK", false);
        addFlight("4", "MTY - IAH", false);
        addFlight("5", "GDL - ORL", false);
        addFlight("6", "DTW - MTY", false);
        addFlight("7", "DTW - LAX", false);
        addFlight("8", "DTW - GDL", false);
        addFlight("12", "LAX - MEX", false);
        addFlight("13", "LAX - PBC", false);
        addFlight("14", "LAX - GDL", false);
        addFlight("15", "ATL - MTY", false);
        addFlight("16", "LAX - SFO", false);
        addFlight("17", "ATL - DTW", false);
        addFlight("18", "DTW - ORR", false);
    }

    // Prevent direct instantiation.
    private FlightsRemoteDataSource() {
    }

    public static FlightsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FlightsRemoteDataSource();
        }
        return INSTANCE;
    }

    private static void addFlight(String id, String name, boolean saved) {
        Flight newFlight = new Flight(id, name, saved);
        FLIGHTS_SERVICE_DATA.put(newFlight.getId(), newFlight);
    }

    @Override
    public void getFlights(@NonNull final LoadTasksCallback callback) {
        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onTasksLoaded(Lists.newArrayList(FLIGHTS_SERVICE_DATA.values()));
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void refreshFlights() {
        // NOP
    }

    @Override
    public void saveFlight(@NonNull Flight flight) {
        FLIGHTS_SERVICE_DATA.put(flight.getId(), flight);
    }

    @Override
    public void deleteFlight(@NonNull String flightId) {
        FLIGHTS_SERVICE_DATA.remove(flightId);
    }

    @Override
    public void deleteAllFlights() {
        FLIGHTS_SERVICE_DATA.clear();
    }

    @Override
    public void updateFlight(@NonNull Flight flight) {
        Flight flightOld = FLIGHTS_SERVICE_DATA.get(flight);
        if (flightOld != null) {
            deleteFlight(flight.getId());
        }
        saveFlight(flight);
    }
}
