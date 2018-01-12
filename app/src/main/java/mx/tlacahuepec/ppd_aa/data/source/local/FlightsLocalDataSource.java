package mx.tlacahuepec.ppd_aa.data.source.local;

import android.support.annotation.NonNull;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.tlacahuepec.ppd_aa.data.Flight;
import mx.tlacahuepec.ppd_aa.data.source.FlightsDataSource;
import mx.tlacahuepec.ppd_aa.utils.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by santi on 1/11/2018.
 */

public class FlightsLocalDataSource implements FlightsDataSource {

    private static volatile FlightsLocalDataSource INSTANCE;
    private final Realm realm;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private FlightsLocalDataSource(@NonNull AppExecutors appExecutors,
                                   @NonNull Realm realmInstance) {
        mAppExecutors = appExecutors;
        realm = realmInstance;
    }

    public static FlightsLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                     @NonNull Realm realmInstance) {
        if (INSTANCE == null) {
            synchronized (FlightsLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FlightsLocalDataSource(appExecutors, realmInstance);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getFlights(@NonNull final LoadTasksCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //final List<Flight> flights = mTasksDao.getTasks();
                //
                // Find the first person (no query conditions) and read a field
                final RealmResults<Flight> flights = realm.where(Flight.class).findAll();

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (flights.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onTasksLoaded(flights);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshFlights() {
        // NOP
    }

    @Override
    public void saveFlight(@NonNull final Flight flight) {
        checkNotNull(flight);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                // All writes must be wrapped in a transaction to facilitate safe multi threading
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insert(flight);
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void deleteFlight(@NonNull final String flightId) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<Flight> result = realm.where(Flight.class).equalTo("id", flightId).findAll();
                        result.deleteAllFromRealm();
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void deleteAllFlights() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<Flight> result = realm.where(Flight.class).findAll();
                        result.deleteAllFromRealm();
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void updateFlight(@NonNull final Flight flight) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insertOrUpdate(flight);
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }
}
