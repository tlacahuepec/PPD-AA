package mx.tlacahuepec.ppd_aa;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by santi on 1/11/2018.
 */

public class PPD_AA_Application extends Application {

    private Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    public Realm getRealmInstance() {
        return realm;
    }
}
