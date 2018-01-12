package mx.tlacahuepec.ppd_aa.browseflight;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mx.tlacahuepec.ppd_aa.Injection;
import mx.tlacahuepec.ppd_aa.R;
import mx.tlacahuepec.ppd_aa.ViewModelHolder;
import mx.tlacahuepec.ppd_aa.utils.ActivityUtils;

public class BrowseFlightActivity extends AppCompatActivity implements BrowseFlightNavigator {

    public static final String BROWSE_FLIGHT_VIEWMODEL_TAG = "BROWSE_FLIGHT_VIEWMODEL_TAG";

    private BrowseFlightViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_flight_act);

        // Setup components like toolbar for back navigation

        BrowseFlightFragment browseFlightFragment = findOrCreateViewFragment();

        mViewModel = findOrCreateViewModel();

        // Link View and ViewModel
        browseFlightFragment.setViewModel(mViewModel);

        mViewModel.onActivityCreated(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //@VisibleForTesting
    //public IdlingResource getCountingIdlingResource() {
    //    return EspressoIdlingResource.getIdlingResource();
    //}

    @Override
    public void onOpenFlightDetails() {
        // TODO: implement
        // show fragment
    }

    @NonNull
    private BrowseFlightFragment findOrCreateViewFragment() {
        // View Fragment
        BrowseFlightFragment browseFlightFragment =
                (BrowseFlightFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (browseFlightFragment == null) {
            browseFlightFragment = BrowseFlightFragment.newInstance();

            // Send the relevant information to the fragment like ids
            Bundle bundle = new Bundle();
            browseFlightFragment.setArguments(bundle);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    browseFlightFragment, R.id.contentFrame);
        }

        return browseFlightFragment;
    }

    private BrowseFlightViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<BrowseFlightViewModel> retainedViewModel =
                (ViewModelHolder<BrowseFlightViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(BROWSE_FLIGHT_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            BrowseFlightViewModel viewModel = new BrowseFlightViewModel(
                    getApplicationContext(),
                    Injection.provideFlightsRepository(getApplicationContext()));

            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    BROWSE_FLIGHT_VIEWMODEL_TAG);
            return viewModel;
        }
    }

    @Override
    protected void onDestroy() {
        mViewModel.onActivityDestroyed();
        super.onDestroy();
    }
}
