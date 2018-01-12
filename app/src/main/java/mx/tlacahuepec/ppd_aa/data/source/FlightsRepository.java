package mx.tlacahuepec.ppd_aa.data.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mx.tlacahuepec.ppd_aa.data.Flight;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by santi on 1/11/2018.
 */

public class FlightsRepository implements FlightsDataSource {

    private static FlightsRepository INSTANCE = null;

    private final FlightsDataSource mTasksRemoteDataSource;

    private final FlightsDataSource mTasksLocalDataSource;

    Map<String, Flight> mCachedFlights;

    boolean mCacheIsDirty = false;

    private FlightsRepository(@NonNull FlightsDataSource localDataSource, @NonNull FlightsDataSource remoteDataSource) {
        mTasksLocalDataSource = checkNotNull(localDataSource);
        mTasksRemoteDataSource = checkNotNull(remoteDataSource);
    }

    public static FlightsRepository getInstance(FlightsDataSource localDataSource,
                                                FlightsDataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new FlightsRepository(localDataSource, remoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getFlights(@NonNull final LoadTasksCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedFlights != null && !mCacheIsDirty) {
            callback.onTasksLoaded(new ArrayList<>(mCachedFlights.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getTasksFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mTasksLocalDataSource.getFlights(new LoadTasksCallback() {
                @Override
                public void onTasksLoaded(List<Flight> tasks) {
                    refreshCache(tasks);
                    callback.onTasksLoaded(new ArrayList<>(mCachedFlights.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getTasksFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void refreshFlights() {
        mCacheIsDirty = true;
    }


    @Override
    public void saveFlight(@NonNull Flight flight) {
        checkNotNull(flight);
        mTasksRemoteDataSource.saveFlight(flight);
        mTasksLocalDataSource.saveFlight(flight);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedFlights == null) {
            mCachedFlights = new LinkedHashMap<>();
        }
        mCachedFlights.put(flight.getId(), flight);
    }

    @Override
    public void deleteFlight(@NonNull String flightId) {
        mTasksRemoteDataSource.deleteFlight(checkNotNull(flightId));
        mTasksLocalDataSource.deleteFlight(checkNotNull(flightId));

        mCachedFlights.remove(flightId);
    }

    private void getTasksFromRemoteDataSource(@NonNull final LoadTasksCallback callback) {
        mTasksRemoteDataSource.getFlights(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Flight> tasks) {
                refreshCache(tasks);
                refreshLocalDataSource(tasks);
                callback.onTasksLoaded(new ArrayList<>(mCachedFlights.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void deleteAllFlights() {
        mTasksRemoteDataSource.deleteAllFlights();
        mTasksLocalDataSource.deleteAllFlights();

        if (mCachedFlights == null) {
            mCachedFlights = new LinkedHashMap<>();
        }
        mCachedFlights.clear();
    }

    private void refreshCache(List<Flight> flights) {
        if (mCachedFlights == null) {
            mCachedFlights = new LinkedHashMap<>();
        }
        mCachedFlights.clear();
        for (Flight flight : flights) {
            mCachedFlights.put(flight.getId(), flight);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Flight> flights) {
        mTasksLocalDataSource.deleteAllFlights();
        for (Flight flight : flights) {
            mTasksLocalDataSource.saveFlight(flight);
        }
    }

    @Override
    public void updateFlight(@NonNull Flight flight) {
        checkNotNull(flight);
        mTasksRemoteDataSource.updateFlight(flight);
        mTasksLocalDataSource.updateFlight(flight);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedFlights == null) {
            mCachedFlights = new LinkedHashMap<>();
        }
        mCachedFlights.remove(flight.getId());
        mCachedFlights.put(flight.getId(), flight);
    }
}
