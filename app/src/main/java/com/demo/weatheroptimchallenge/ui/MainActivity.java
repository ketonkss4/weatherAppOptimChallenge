package com.demo.weatheroptimchallenge.ui;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.weatheroptimchallenge.R;
import com.demo.weatheroptimchallenge.databinding.ActivityMainBinding;
import com.demo.weatheroptimchallenge.models.Condition;
import com.demo.weatheroptimchallenge.models.Observation;
import com.demo.weatheroptimchallenge.models.Location;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String ZIP_CODE = "zipCode";
    public static final String DEF_VALUE = "0";
    private MapFragment mMapFragment;
    private ActivityMainBinding binding;
    private GoogleMap googleMap;
    private BottomSheetBehavior behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.fab.setOnClickListener(v -> promptNewLocationInput());

        setupBottomSheet();

        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_layout, mMapFragment);
        fragmentTransaction.commit();

        mMapFragment.getMapAsync(this);
    }

    private void setupBottomSheet() {
        NestedScrollView bottomSheet = binding.bottomSheet;
        behavior = BottomSheetBehavior.from(bottomSheet);

    }

    private void scrollBottomSheetUp() {
        binding.bottomSheet.post(() -> behavior.setState(BottomSheetBehavior.STATE_EXPANDED));
    }

    private void hideBottomSheet() {
        binding.bottomSheet.post(() -> behavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
    }

    private String retrieveLastLocation() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(ZIP_CODE, DEF_VALUE);
    }

    private void saveLastLocation(String zipCode) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ZIP_CODE, zipCode);
        editor.apply();
    }

    private void setConditions(String savedZip) {
        Condition.getNetworkRequest(savedZip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(condition -> {
                    if(condition.getResponse().getError()==null) {
                        Observation observation = condition.getCurrent_observation();
                        setupBottomSheetInfo(observation);
                        setNewLocation(observation);
                    }else {
                        String error_description = condition.getResponse().getError().getDescription();
                        onError(new Throwable(error_description));
                    }
                }, this::onError);
    }

    private void setNewLocation(Observation observation) {
        Location location = observation.getLocation();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(12)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.animateCamera(cameraUpdate, 2000, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                scrollBottomSheetUp();
            }

            @Override
            public void onCancel() {

            }
        });


    }

    private void setupBottomSheetInfo(Observation observation) {

        TextView cityName = (TextView) binding.weatherSheet.findViewById(R.id.city_name);
        cityName.setText(observation.getLocation().getCity());
        ImageView conditionIcon = (ImageView) binding.weatherSheet.findViewById(R.id.condition_icon);
        Picasso.with(this).load(observation.getIconUrl()).into(conditionIcon);
        TextView condition = (TextView) binding.weatherSheet.findViewById(R.id.condition);
        condition.setText(observation.getCondition());
        TextView temp = (TextView) binding.weatherSheet.findViewById(R.id.temp);
        temp.setText(observation.getTemp());
        TextView humidity = (TextView) binding.weatherSheet.findViewById(R.id.humidity);
        humidity.setText(observation.getHumidity());
        TextView time = (TextView) binding.weatherSheet.findViewById(R.id.time);
        time.setText(observation.getObservationTime());
        binding.weatherSheet.invalidate();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Retrieve the current condition from the network and set databinding
        //based on last saved location
        this.googleMap = googleMap;


        String lastLocation = retrieveLastLocation();
        if (!TextUtils.isEmpty(lastLocation) && !lastLocation.equalsIgnoreCase(DEF_VALUE)) {
            setConditions(lastLocation);
        }else {
            setMapToDefaultPosition(googleMap);
            promptNewLocationInput();
        }
    }

    private void setMapToDefaultPosition(GoogleMap googleMap) {
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(46.7296, 94.6859));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);
    }

    private void promptNewLocationInput() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View promptsView = inflater.inflate(R.layout.location_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        final EditText zipCodeInput = (EditText) promptsView.findViewById(R.id.zipCodeInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        (dialog, id) -> {
                            String newZipCode = zipCodeInput.getText().toString();
                            if (newZipCode.length() == 5) {
                                hideBottomSheet();
                                setConditions(newZipCode);
                                saveLastLocation(newZipCode);
                            } else {
                                onError(new Throwable("Enter valid ZipCode"));
                            }
                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> {
                            dialog.cancel();
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void onError(Throwable throwable) {
        Log.v(MainActivity.class.getSimpleName(), throwable.getMessage());
        Snackbar.make(binding.getRoot(), throwable.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

}
