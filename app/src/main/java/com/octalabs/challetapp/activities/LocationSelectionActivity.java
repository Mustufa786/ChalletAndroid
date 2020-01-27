package com.octalabs.challetapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.octalabs.challetapp.R;
import com.octalabs.challetapp.utils.Constants;
import com.octalabs.challetapp.utils.FusedLocationTracker;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LocationSelectionActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraChangeListener, FusedLocationTracker.IOnUpdateListener {


    private GoogleMap mMap;
    private FusedLocationTracker mFusedLocationTracker;

    static boolean isActive;
    double myLat, myLng;
    KProgressHUD hud;
    TextView doneBtn;
    boolean notSelectedLocation;
    boolean isFirst;
    TextView currentLocationText;
    boolean isCompanyAddress;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    private static final int ALLOW_OPEN_LOCATION = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);

        setTextAction(Objects.requireNonNull(getSupportActionBar()), "Location");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));
        if (Build.VERSION.SDK_INT >= 23) {
            checkForPhoneStatePermissionLocationAccess();
        } else {
            try {
                initializeMap();
                createLocationService();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        notSelectedLocation = true;
        isFirst = true;
        isActive = true;
        hud = KProgressHUD.create(LocationSelectionActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);
        currentLocationText = (TextView) findViewById(R.id.current_location_text);
        currentLocationText.setEnabled(false);

        doneBtn.setVisibility(View.VISIBLE);
        initializeMap();
        onClickListeners();
    }

    private void setTextAction(ActionBar actionbar, String title) {
        TextView textview = new TextView(this);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText(title);
        textview.setTextColor(Color.WHITE);
        textview.setTextSize(16);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);

        LinearLayout childLayout = new LinearLayout(this);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        childLayout.setLayoutParams(linearParams);

        TextView mType = new TextView(this);
        doneBtn = new TextView(this);

        mType.setLayoutParams(new TableLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        doneBtn.setLayoutParams(new TableLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        mType.setTextSize(16);
        mType.setPadding(0, 0, 0, 0);
        mType.setTextColor(getResources().getColor(R.color.white));
        mType.setGravity(Gravity.START | Gravity.CENTER);

        doneBtn.setTextSize(16);

        mType.setTextColor(getResources().getColor(R.color.white));
        doneBtn.setTextColor(getResources().getColor(R.color.white));

        doneBtn.setGravity(Gravity.END | Gravity.CENTER);

        mType.setText(title);
        doneBtn.setText("DONE");

        childLayout.addView(doneBtn, 0);
        childLayout.addView(mType, 0);

        actionbar.setCustomView(childLayout);

    }

    private void checkForPhoneStatePermissionLocationAccess() {

        if (ContextCompat.checkSelfPermission(LocationSelectionActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {

                    Log.i("tag", "test");
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    requestPermissions(new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                            },
                            MY_PERMISSIONS_REQUEST_LOCATION);
                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        } else {

            try {

                initializeMap();
                createLocationService();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }


    private void onClickListeners() {
        currentLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(LocationSelectionActivity.this);
                    startActivityForResult(intent, 100);
                    Toast.makeText(LocationSelectionActivity.this, "Search clicked", Toast.LENGTH_SHORT).show();

                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }


            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneBtnClicked();
            }
        });

    }


    private void initializeMap() {
        if (isActive) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(LocationSelectionActivity.this);
            }
        }

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setTrafficEnabled(true);
        currentLocationText.setEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                LocationSelectionActivity.this.mMap.clear();
                LocationSelectionActivity.this.mMap.addMarker(new MarkerOptions().position(latLng));
                myLat = latLng.latitude;
                myLng = latLng.longitude;
                notSelectedLocation = false;

            }
        });
        this.mMap = googleMap;
        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                try {
                    final CameraPosition position = googleMap.getCameraPosition();
                    final Geocoder geocoder;

                    geocoder = new Geocoder(LocationSelectionActivity.this, Locale.getDefault());
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final List<Address> addresses;
                                addresses = geocoder.getFromLocation(position.target.latitude, position.target.longitude, 1);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (addresses.size() > 0) {
                                            try {
                                                String fromLocationStr = addresses.get(0).getAddressLine(0).toString() + "," + position.target.latitude + "," + position.target.longitude;
                                                currentLocationText.setText(addresses.get(0).getAddressLine(0).toString());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                });

                                Log.i("tag", addresses.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //autocompleteFragment.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(LocationSelectionActivity.this, data);
                Log.i("tag", "Place:" + place.toString());
                currentLocationText.setText(place.getName());
                myLat = place.getLatLng().latitude;
                myLng = place.getLatLng().longitude;
                CameraPosition newCamPos = new CameraPosition(new LatLng(myLat, myLng),
                        15.5f,
                        mMap.getCameraPosition().tilt, //use old tilt
                        mMap.getCameraPosition().bearing); //use old bearing
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 4000, null);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(LocationSelectionActivity.this, data);
                Log.i("tag", status.getStatusMessage());
            } else if (requestCode == RESULT_CANCELED) {

            }
        } else if (requestCode == ALLOW_OPEN_LOCATION) {
            if (resultCode == RESULT_OK) {
                createLocationService();

            }
        }
    }

    private boolean validate() {
        if (validateMarker()) {
            if (validateCurrentLocationName()) {
                return true;
            }
            displayAlert("Alert", "Shake the Map A Little so the location name start showing in the search bar");
            return false;
        }
        displayAlert("Alert", "Please select your location by tapping on the map");
        return false;
    }

    private boolean validateCurrentLocationName() {
        if (!currentLocationText.getText().toString().equalsIgnoreCase("")) {
            return true;
        }
        return false;
    }

    private boolean validateMarker() {
        if (notSelectedLocation) {
            return false;
        }
        return true;
    }


    public void doneBtnClicked() {
        if (validate()) {

            AlertDialog mDialog = new AlertDialog.Builder(LocationSelectionActivity.this)
                    .setTitle("Alert")
                    .setMessage("Mark this location ?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra(Constants.SELECTED_LATITUE, String.valueOf(myLat));
                            intent.putExtra(Constants.SELECTED_LOCATION_TITLE, currentLocationText.getText().toString());
                            intent.putExtra(Constants.SELECTED_LONGITUDE, String.valueOf(myLng));
                            setResult(RESULT_OK, intent);
                            LocationSelectionActivity.this.finish();

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();

            mDialog.show();


        }


    }

    private void displayAlert(String alert, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LocationSelectionActivity.this)
                .setTitle(alert)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraIdle() {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    try {

                        initializeMap();
                        createLocationService();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(LocationSelectionActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    checkForPhoneStatePermissionLocationAccess();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


        }
    }

    private void createLocationService() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!Objects.requireNonNull(manager).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return;
        }
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mFusedLocationTracker == null) {
                    mFusedLocationTracker = new FusedLocationTracker(LocationSelectionActivity.this, LocationSelectionActivity.this);
                }
                mFusedLocationTracker.startLocationUpdates();
            }
        }, 500);

    }

    private void buildAlertMessageNoGps() {
        Toast.makeText(this, "Please enable GPS", Toast.LENGTH_SHORT).show();
        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), ALLOW_OPEN_LOCATION);
    }


    @Override
    public void onLocationUpdate(Location location) {
        myLat = location.getLatitude();
        myLng = location.getLongitude();
        if (mMap != null && isFirst) {
            CameraPosition newCamPos = new CameraPosition(new LatLng(myLat, myLng),
                    15.5f,
                    mMap.getCameraPosition().tilt, //use old tilt
                    mMap.getCameraPosition().bearing); //use old bearing
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 1000, null);
            isFirst = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFusedLocationTracker != null)
            mFusedLocationTracker.stopLocationUpdates();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }
}