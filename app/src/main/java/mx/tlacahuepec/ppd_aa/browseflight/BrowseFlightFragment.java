package mx.tlacahuepec.ppd_aa.browseflight;


import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mx.tlacahuepec.ppd_aa.Injection;
import mx.tlacahuepec.ppd_aa.adapter.DataBindingBaseAdapter;
import mx.tlacahuepec.ppd_aa.adapter.FlightAdapter;
import mx.tlacahuepec.ppd_aa.R;
import mx.tlacahuepec.ppd_aa.data.Flight;
import mx.tlacahuepec.ppd_aa.databinding.BrowseFlightFragBinding;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrowseFlightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseFlightFragment extends Fragment implements DataBindingBaseAdapter.OnItemClickListener {

    private BrowseFlightViewModel mViewModel;

    private BrowseFlightFragBinding mViewDataBinding;

    private Observable.OnPropertyChangedCallback mSnackbarCallback;

    private FlightAdapter mFlightAdapter;


    public BrowseFlightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BrowseFlightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrowseFlightFragment newInstance() {
        return new BrowseFlightFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.browse_flight_frag, container, false);
        if (mViewDataBinding == null) {
            mViewDataBinding = BrowseFlightFragBinding.bind(root);
        }

        mViewDataBinding.setViewmodel(mViewModel);

        setHasOptionsMenu(false);
        setRetainInstance(false);

        return mViewDataBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        if (mSnackbarCallback != null) {
            // TODO: Remove Listeners
            //mViewModel.snackbarText.removeOnPropertyChangedCallback(mSnackbarCallback);
        }
        super.onDestroy();
    }

    public void setViewModel(@NonNull BrowseFlightViewModel viewModel) {
        mViewModel = checkNotNull(viewModel);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // setupDifferentComponents like Fab, ActionBar SnakcBar
        setupRecyclerViewAdapter();
    }

    private void setupRecyclerViewAdapter() {
        RecyclerView mFlightList = mViewDataBinding.rvFlights;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mFlightList.setLayoutManager(layoutManager);

        mFlightList.setHasFixedSize(true);

        mFlightAdapter = new FlightAdapter<>(this, R.layout.flight_list_item,
                new ArrayList<Flight>(), Injection.provideFlightsRepository(getContext().getApplicationContext()),
                mViewModel);
        mFlightList.setAdapter(mFlightAdapter);

    }

    @Override
    public void onItemClick(Object item) {
        // Some Action
    }
}
