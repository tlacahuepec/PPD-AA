package mx.tlacahuepec.ppd_aa;

/**
 * Created by santi on 1/11/2018.
 */

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import io.realm.Realm;
import mx.tlacahuepec.ppd_aa.data.source.FlightsRepository;
import mx.tlacahuepec.ppd_aa.data.source.local.FlightsLocalDataSource;
import mx.tlacahuepec.ppd_aa.data.source.remote.FlightsRemoteDataSource;
import mx.tlacahuepec.ppd_aa.utils.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of data sources.
 */
public class Injection {

    public static FlightsRepository provideFlightsRepository(@NonNull Context applicationContext) {
        checkNotNull(applicationContext);
        Application application = (PPD_AA_Application) applicationContext.getApplicationContext();
        Realm realmInstance = ((PPD_AA_Application) application).getRealmInstance();
        FlightsRepository flightsRepository = FlightsRepository.getInstance(FlightsLocalDataSource.getInstance(new AppExecutors(), realmInstance), FlightsRemoteDataSource.getInstance());
        return flightsRepository;
    }
}
