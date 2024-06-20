package com.twd.chitboyapp.spsskl;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.SphericalUtil;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.LocationCalculator;
import com.twd.chitboyapp.spsskl.constant.LocationUpdatetor;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.Screenshot;
import com.twd.chitboyapp.spsskl.enums.Confirmation;
import com.twd.chitboyapp.spsskl.enums.MapEnum;
import com.twd.chitboyapp.spsskl.interfaces.LocationUpdateListener;
import com.twd.svalidation.ServerValidation;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PlantAreaCalcuationActivity extends AppCompatActivity implements OnMapReadyCallback, LocationUpdateListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private GoogleMap mMap;
    LocationUpdatetor locationUpdatetor;
    private ArrayList<LatLng> arrayPoints = null;
    private ArrayList<Double> accuracy = null;
    private ArrayList<Marker> markers = null;
    Polygon polygon = null;
    private int calculationType = 0;
    private boolean checkClick = false;
    private String areaMin, areaMax, mapAllowIn, calculationAcc, clickCalulate, vlat, vlong, vradius;
    double visitLat;
    double visitlong;
    MapEnum mapEnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_area_calcuation);


        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Intent intent = getIntent();
        vlat = intent.getStringExtra("vlat");
        vlong = intent.getStringExtra("vlong");
        vradius = intent.getStringExtra("vradius");


        Activity activity = PlantAreaCalcuationActivity.this;
        startLocation(activity);
        doMapInilization();


        ConstantFunction cf = new ConstantFunction();
        //String[] key = new String[]{getResources().getString(R.string.prefareamin), getResources().getString(R.string.prefareamax), getResources().getString(R.string.prefmapallowedin), getResources().getString(R.string.prefcalculationacc), getResources().getString(R.string.prefcalculateonline)};
        String[] key = new String[]{"t1", "t2", "t3", "t4", "t5"};
        String[] value = new String[]{"0.01", "9.00", "100", "10", "Y"};
        String[] data = cf.getSharedPrefValue(activity, key, value);
        areaMin = data[0];
        areaMax = data[1];
        mapAllowIn = data[2];
        calculationAcc = data[3];


        AppCompatButton btnsubmit, btntakepoint, btnundo, btnclear, btnskip;
        AppCompatTextView txtgpsarealbl, txtcollon17, txtgpsareatxt, txthectorlbl;
        ConstraintLayout clother;

        btnsubmit = findViewById(R.id.btnsubmit);
        btntakepoint = findViewById(R.id.btntakepoint);
        btnundo = findViewById(R.id.btnundo);
        btnclear = findViewById(R.id.btnclear);
        btnskip = findViewById(R.id.btnskip);
        txtgpsarealbl = findViewById(R.id.txtgpsarealbl);
        txtcollon17 = findViewById(R.id.txtcollon17);
        txtgpsareatxt = findViewById(R.id.txtgpsareatxt);
        txthectorlbl = findViewById(R.id.txthectorlbl);
        clother = findViewById(R.id.clother);
        AppCompatButton btnprev = findViewById(R.id.btnprev);

        btnsubmit.setOnClickListener(this);
        btnskip.setOnClickListener(this);

        showMyLocation();

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mapEnum = (MapEnum) intent.getSerializableExtra("map");
        if (intent.hasExtra("poltlocation")) {
            ArrayList<LatLng> poltlocation = (ArrayList<LatLng>) intent.getSerializableExtra("poltlocation");
            arrayPoints = new ArrayList<>(poltlocation);
        } else if (intent.hasExtra("visitLat") || intent.hasExtra("visitlong")) {
            visitLat = intent.getDoubleExtra("visitLat", 0);
            visitlong = intent.getDoubleExtra("visitlong", 0);
        }
        if (mapEnum == MapEnum.CALCULATION) {
            clickCalulate = data[4];
            btntakepoint.setOnClickListener(this);
            btnundo.setOnClickListener(this);
            btnclear.setOnClickListener(this);
            clother.setVisibility(View.VISIBLE);
        } else {
            clickCalulate = "N";
            btntakepoint.setVisibility(View.GONE);
            btnundo.setVisibility(View.GONE);
            btnclear.setVisibility(View.GONE);
            btnsubmit.setVisibility(View.GONE);
            clother.setVisibility(View.VISIBLE);
            btnprev.setText(getResources().getString(R.string.ok));
            if (mapEnum == MapEnum.VIEWLOCATION) {
                txtgpsarealbl.setVisibility(View.GONE);
                txtcollon17.setVisibility(View.GONE);
                txtgpsareatxt.setVisibility(View.GONE);
                txthectorlbl.setVisibility(View.GONE);
            }
        }
    }

    public void onClick(View view) {
        Activity activity = PlantAreaCalcuationActivity.this;
        int viewId = view.getId();
        if (viewId == R.id.btnsubmit) {
            if (arrayPoints.size() >= 3) {
                AppCompatTextView txtgpsareatxt = findViewById(R.id.txtgpsareatxt);
                String gpsarea = txtgpsareatxt.getText().toString();
                ServerValidation sv = new ServerValidation();
                if (sv.checkFloat(gpsarea)) {
                    double calarea = Double.parseDouble(gpsarea);
                    if (calarea <= Double.parseDouble(areaMax) && calarea >= Double.parseDouble(areaMin)) {
                        AlertDialog alertDialog = ConstantFunction.createProgress(activity);
                        ConstantFunction.showDialog(alertDialog);
                        Screenshot screenshot = new Screenshot(activity);
                        String mapss = screenshot.generateFileName("mapss");
                        screenshot.setFileName(mapss);
                        Intent intent = new Intent();
                        intent.putExtra("arrayPoints", arrayPoints);
                        intent.putExtra("gpsarea", gpsarea);
                        intent.putExtra("mapss", mapss);
                        intent.putExtra("calculationType", String.valueOf(calculationType));
                        setResult(Activity.RESULT_OK, intent);
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (Marker marker : markers) {
                            builder.include(marker.getPosition());
                        }
                        LatLngBounds bounds = builder.build();
                        int padding = 200; // offset from edges of the map in pixels
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        mMap.moveCamera(cu);
                        screenshot.setNextIntent(intent);
                        screenshot.setAlertDialog(alertDialog);
                        mMap.snapshot(screenshot);

                    } else
                        Constant.showToast(String.format(getResources().getString(R.string.errorareabetweenatob), areaMin, areaMax), activity, R.drawable.ic_wrong);
                } else
                    Constant.showToast(getResources().getString(R.string.errorareanotcount), activity, R.drawable.ic_wrong);
            } else
                Constant.showToast(getResources().getString(R.string.errortakeminimum3point), activity, R.drawable.ic_wrong);


            //saveOrUpdate();
        } else if (viewId == R.id.btntakepoint) {
            if (calculationType != 2) {
                takePointOnMap(activity);
            } else {
                Constant.showToast(getResources().getString(R.string.errorhybridarea), PlantAreaCalcuationActivity.this, R.drawable.ic_wrong);
            }
        } else if (viewId == R.id.btnundo) {
            askConfirm(Confirmation.UNDO, activity);
        } else if (viewId == R.id.btnclear) {
            askConfirm(Confirmation.CLEAR, activity);
        } else if (viewId == R.id.btnskip) {
            skip();
        }
    }

    double prevAcc = 9999;

    private void showMyLocation() {
        AppCompatTextView txtlat = findViewById(R.id.txtlat);
        AppCompatTextView txtlong = findViewById(R.id.txtlong);
        AppCompatTextView txtacc = findViewById(R.id.txtacc);
        String lat = txtlat.getText().toString();
        String longs = txtlong.getText().toString();
        String acc = txtacc.getText().toString();
        if (!lat.equals("")) {
            double dlat = Double.parseDouble(lat);
            double dlong = Double.parseDouble(longs);
            double dacc = Double.parseDouble(acc);
            prevAcc = dacc;
            if (dacc <= 100) {
                LatLng latLng = new LatLng(dlat, dlong);
                animateCamera(latLng);
            }
        }

    }

    private void animateCamera(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        if (mMap != null) {
            mMap.animateCamera(cameraUpdate);
        }
    }

    private void doMapInilization() {
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fm.getMapAsync(this);
        arrayPoints = new ArrayList<>();
        accuracy = new ArrayList<>();
        markers = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MenuHandler cf = new MenuHandler();
        return cf.openCommon(this, item, null);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuHandler cf = new MenuHandler();
        return cf.performRefreshOption(menu, this);
    }

    private void skip() {
        AppCompatTextView txtacc = findViewById(R.id.txtacc);
        AppCompatTextView txtlat = findViewById(R.id.txtlat);
        AppCompatTextView txtlong = findViewById(R.id.txtlong);
        AppCompatTextView txtgpsareatxt = findViewById(R.id.txtgpsareatxt);
        String acc = txtacc.getText().toString();
        String latitude = txtlat.getText().toString();
        String longitude = txtlong.getText().toString();
        ServerValidation sv = new ServerValidation();
        if (sv.checkFloat(acc)) {
            double dacc = Double.parseDouble(acc);

            LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            arrayPoints.add(point);
            accuracy.add(dacc);
            txtgpsareatxt.setText("0.40");

        } else {
            Constant.showToast("waiting for location", PlantAreaCalcuationActivity.this, R.drawable.ic_wrong);
        }
    }

    private void askConfirm(Confirmation confirmation, Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.popup_delete);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        AppCompatTextView message = dialog.findViewById(R.id.message);
        AppCompatTextView txt_deletehead = dialog.findViewById(R.id.txt_deletehead);
        if (confirmation == Confirmation.CLEAR) {
            message.setText(getResources().getString(R.string.messageclearpoint));
            txt_deletehead.setText(getResources().getString(R.string.clear));
        } else if (confirmation == Confirmation.UNDO) {
            message.setText(getResources().getString(R.string.messageclearlast));
            txt_deletehead.setText(getResources().getString(R.string.undo));
        }

        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        AppCompatButton btncanceldelete = dialog.findViewById(R.id.btncanceldelete);
        AppCompatButton btnconfirmdelete = dialog.findViewById(R.id.btnconfirmdelete);

        btncanceldelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        btnconfirmdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmation == Confirmation.CLEAR) {
                    mapClear();
                } else if (confirmation == Confirmation.UNDO) {
                    removeLastPoint(activity);
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void mapClear() {
        mMap.clear();
        arrayPoints.clear();
        markers.clear();
        accuracy.clear();
        checkClick = false;
        calculationType = 0;
        polygon = null;
        AppCompatTextView txtgpsareatxt = findViewById(R.id.txtgpsareatxt);
        txtgpsareatxt.setText("");
    }

    private void removeLastPoint(Activity activity) {
        int size = arrayPoints.size();
        if (size == 0) {
            Constant.showToast(getResources().getString(R.string.errornopointtaken), activity, R.drawable.ic_wrong);
        } else if (size == 1) {
            mapClear();
        } else {
            clearAndRedrawMap(true, size);
        }
    }

    private void clearAndRedrawMap(boolean isRemove, int size) {
        mMap.clear();
        markers = new ArrayList<>();
        if (isRemove) size = size - 1;
        for (int i = 0; i < size; i++) {
            LatLng point = arrayPoints.get(i);
            if (i == 0 && mapEnum == MapEnum.VIEWPOLYGON) animateCamera(point);
            Marker marker = mMap.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_zoom)).draggable(true).rotation(180).snippet(String.valueOf(i)));
            markers.add(marker);
        }

        if (isRemove) {
            arrayPoints.remove(size);
            accuracy.remove(size);
        }
        polygon = null;
        polygon = mMap.addPolygon(new PolygonOptions().clickable(true).strokeColor(Color.BLUE).strokeWidth(5).geodesic(true).addAll(arrayPoints).fillColor(getResources().getColor(R.color.mapcolor)));
        polygon.setGeodesic(true);
        polygon.setPoints(arrayPoints);
        if (size >= 3) {
            calculateArea(false);
        } else {
            AppCompatTextView txtgpsareatxt = findViewById(R.id.txtgpsareatxt);
            txtgpsareatxt.setText("");
        }
    }

    private void takePointOnMap(Activity activity) {
        /*LatLng point = mMap.getCameraPosition().target;*/
        AppCompatTextView txtacc = findViewById(R.id.txtacc);
        AppCompatTextView txtlat = findViewById(R.id.txtlat);
        AppCompatTextView txtlong = findViewById(R.id.txtlong);
        String acc = txtacc.getText().toString();
        String latitude = txtlat.getText().toString();
        String longitude = txtlong.getText().toString();
        ServerValidation sv = new ServerValidation();
        if (sv.checkFloat(acc)) {

            double dacc = Double.parseDouble(acc);
            if (dacc <= Double.parseDouble(calculationAcc)) {
                LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                LocationCalculator locationCalculator = new LocationCalculator();
                boolean takePoint = true;
                if (sv.checkFloat(vradius) && sv.checkFloat(vlat) && sv.checkFloat(vlong)) {
                    double distance = locationCalculator.distanceBetween2LatLngs(new LatLng(Double.parseDouble(vlat), Double.parseDouble(vlong)), point);
                    takePoint = distance <= Double.parseDouble(vradius);
                }
                if (takePoint) {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_zoom)).draggable(true).rotation(180).snippet(String.valueOf(arrayPoints.size())));
                    arrayPoints.add(point);
                    markers.add(marker);
                    accuracy.add(dacc);
                    if (polygon == null) {
                        polygon = mMap.addPolygon(new PolygonOptions().clickable(true).strokeColor(Color.BLUE).strokeWidth(5).geodesic(true).addAll(arrayPoints).fillColor(getResources().getColor(R.color.mapcolor)));
                        polygon.setGeodesic(true);
                    }
                    polygon.setPoints(arrayPoints);
                    if (arrayPoints.size() >= 3) {
                        calculateArea(false);
                    }
                    calculationType = 1;
                } else {
                    Constant.showToast(getResources().getString(R.string.erroryouarenotinvillage), PlantAreaCalcuationActivity.this, R.drawable.ic_wrong);
                }
            } else {
                Constant.showToast(String.format(getResources().getString(R.string.reqaccnotavailableparam), calculationAcc), activity, R.drawable.ic_wrong);
            }
        }
    }

    private void calculateArea(boolean showMsg) {
        Activity activity = PlantAreaCalcuationActivity.this;
        ArrayList<LatLng> clone = (ArrayList<LatLng>) arrayPoints.clone();
        clone.add(arrayPoints.get(0));
        double area = SphericalUtil.computeArea(clone);
        double calarea = area / 10000;
        if (calarea > Double.parseDouble(areaMax) || calarea < Double.parseDouble(areaMin)) {
            if (showMsg)
                Constant.showToast(String.format(getResources().getString(R.string.errorareabetweenatob), areaMin, areaMax), activity, R.drawable.ic_wrong);
        } else {
            DecimalFormat df = new DecimalFormat("#0.00");
            AppCompatTextView txtgpsareatxt = findViewById(R.id.txtgpsareatxt);
            txtgpsareatxt.setText(MarathiHelper.convertMarathitoEnglish(df.format(calarea)));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.mMap.setMyLocationEnabled(true);
        this.mMap.setTrafficEnabled(true);
        this.mMap.setIndoorEnabled(true);
        this.mMap.setBuildingsEnabled(true);
        this.mMap.getUiSettings().setZoomControlsEnabled(true);
        if (mapEnum == MapEnum.VIEWPOLYGON) {
            clearAndRedrawMap(false, arrayPoints.size());
        } else if (mapEnum == MapEnum.VIEWLOCATION) {
            LatLng point = new LatLng(visitLat, visitlong);
            Marker marker = mMap.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_zoom)).draggable(true).rotation(180));
            markers.add(marker);
            animateCamera(point);
        }
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
                if (clickCalulate.equalsIgnoreCase("Y")) {
                    if (calculationType == 2) {
                        checkClick = true;
                        int pos = Integer.parseInt(marker.getSnippet());

                        if (pos != -1) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                            arrayPoints.set(pos, marker.getPosition());
                            if (polygon == null) {
                                polygon = mMap.addPolygon(new PolygonOptions().clickable(true).strokeColor(Color.BLUE).strokeWidth(5).geodesic(true).addAll(arrayPoints).fillColor(getResources().getColor(R.color.mapcolor)));
                                polygon.setGeodesic(true);
                            }
                            polygon.setPoints(arrayPoints);
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_double_zoom));
                            marker.setRotation(180);
                            if (arrayPoints.size() >= 3) {
                                calculateArea(false);
                            }
                        }
                    } else {
                        Constant.showToast(getResources().getString(R.string.errorwakingpointmovenotallowed), PlantAreaCalcuationActivity.this, R.drawable.ic_wrong);
                    }
                } else {
                    Constant.showToast(getResources().getString(R.string.errorareaban), PlantAreaCalcuationActivity.this, R.drawable.ic_wrong);
                }
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                if (clickCalulate.equalsIgnoreCase("Y")) {
                    if (calculationType == 2) {
                        checkClick = false;
                        int pos = Integer.parseInt(marker.getSnippet());
                        if (pos != -1) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                            arrayPoints.set(pos, marker.getPosition());
                            if (polygon == null) {
                                polygon = mMap.addPolygon(new PolygonOptions().clickable(true).strokeColor(Color.BLUE).strokeWidth(5).geodesic(true).addAll(arrayPoints).fillColor(getResources().getColor(R.color.mapcolor)));
                                polygon.setGeodesic(true);
                            }
                            polygon.setPoints(arrayPoints);
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_zoom));
                            marker.setRotation(180);
                            if (arrayPoints.size() >= 3) {
                                calculateArea(false);
                            }
                        }
                    } else {
                        Constant.showToast(getResources().getString(R.string.errorwakingpointmovenotallowed), PlantAreaCalcuationActivity.this, R.drawable.ic_wrong);
                    }
                } else {
                    Constant.showToast(getResources().getString(R.string.errorareaban), PlantAreaCalcuationActivity.this, R.drawable.ic_wrong);
                }
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                if (clickCalulate.equalsIgnoreCase("Y")) {
                    if (calculationType == 2) {
                        int pos = Integer.parseInt(marker.getSnippet());

                        if (pos != -1) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                            arrayPoints.set(pos, marker.getPosition());
                            if (polygon == null) {
                                polygon = mMap.addPolygon(new PolygonOptions().clickable(true).strokeColor(Color.BLUE).strokeWidth(5).geodesic(true).addAll(arrayPoints).fillColor(getResources().getColor(R.color.mapcolor)));
                                polygon.setGeodesic(true);
                            }
                            polygon.setPoints(arrayPoints);
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_double_zoom));
                            marker.setRotation(180);
                            if (arrayPoints.size() >= 3) {
                                calculateArea(false);
                            }
                        }
                    } else {
                        Constant.showToast(getResources().getString(R.string.errorwakingpointmovenotallowed), PlantAreaCalcuationActivity.this, R.drawable.ic_wrong);
                    }
                } else {
                    Constant.showToast(getResources().getString(R.string.errorareaban), PlantAreaCalcuationActivity.this, R.drawable.ic_wrong);
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == LocationUpdatetor.REQUEST_CHECK_SETTINGS) {
                locationUpdatetor.startLocationButtonClick();
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == LocationUpdatetor.REQUEST_CHECK_SETTINGS) {
                locationUpdatetor.stopLocationUpdates();
            }
        }
    }

    @Override
    public void onLocationUpdate(double lats, double longs, double accuracy) {
        locationUpdatetor.updateUIData(PlantAreaCalcuationActivity.this, lats, longs, accuracy);
        if (prevAcc > 100 && mapEnum == MapEnum.CALCULATION) showMyLocation();
        AppCompatButton btntakepoint = findViewById(R.id.btntakepoint);
        btntakepoint.setEnabled(accuracy <= Double.parseDouble(calculationAcc));
    }

    private void startLocation(Activity activity) {
        locationUpdatetor = new LocationUpdatetor(activity);
        locationUpdatetor.locationCaller = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        locationUpdatetor.startLocationButtonClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationUpdatetor.stopLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationUpdatetor.stopLocationUpdates();
    }

    private String calAirToAirDist() {
        Location loc1 = new Location("");
        loc1.setLatitude(17.85160);
        loc1.setLongitude(75.10289);

        if (!arrayPoints.isEmpty()) {
            LatLng latLng = arrayPoints.get(0);

            Location loc2 = new Location("");
            loc2.setLatitude(latLng.latitude);
            loc2.setLongitude(latLng.longitude);

            DecimalFormat df = new DecimalFormat("#0.000");
            float distanceInMeters = loc1.distanceTo(loc2) / 1000;
            return MarathiHelper.convertMarathitoEnglish(df.format(distanceInMeters));
        }
        return null;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if (clickCalulate.equalsIgnoreCase("Y")) {
            if (calculationType != 1) {
                if (!checkClick) {
                    AppCompatTextView txtacc = findViewById(R.id.txtacc);
                    AppCompatTextView txtlat = findViewById(R.id.txtlat);
                    AppCompatTextView txtlong = findViewById(R.id.txtlong);
                    String acc = txtacc.getText().toString();
                    String latitude = txtlat.getText().toString();
                    String longitude = txtlong.getText().toString();
                    ServerValidation sv = new ServerValidation();
                    if (sv.checkFloat(acc)) {
                        LocationCalculator locationCalculator = new LocationCalculator();
                        boolean takePoint = true;
                        if (sv.checkFloat(vradius) && sv.checkFloat(vlat) && sv.checkFloat(vlong)) {
                            double distance = locationCalculator.distanceBetween2LatLngs(new LatLng(Double.parseDouble(vlat), Double.parseDouble(vlong)), latLng);
                            takePoint = distance <= Double.parseDouble(vradius);
                        }
                        if (takePoint) {
                            if (!arrayPoints.isEmpty() || locationCalculator.distanceBetween2LatLngs(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)), latLng) <= Integer.parseInt(mapAllowIn)) {
                                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_zoom)).draggable(true).rotation(180).snippet(String.valueOf(arrayPoints.size())));
                                markers.add(marker);
                                arrayPoints.add(latLng);
                                accuracy.add(0.0);
                                if (polygon == null) {
                                    polygon = mMap.addPolygon(new PolygonOptions().clickable(true).strokeColor(Color.BLUE).strokeWidth(5).geodesic(true).addAll(arrayPoints).fillColor(getResources().getColor(R.color.mapcolor)));
                                    polygon.setGeodesic(true);
                                }
                                polygon.setPoints(arrayPoints);
                                if (arrayPoints.size() >= 3) {
                                    calculateArea(false);
                                }
                            } else {
                                Constant.showToast(String.format(getResources().getString(R.string.errorallowedarea), mapAllowIn), PlantAreaCalcuationActivity.this, R.drawable.ic_wrong);
                            }
                        } else {
                            Constant.showToast(getResources().getString(R.string.erroryouarenotinvillage), PlantAreaCalcuationActivity.this, R.drawable.ic_wrong);
                        }
                    } else {
                        Constant.showToast(getResources().getString(R.string.wearegettingyourlocation), PlantAreaCalcuationActivity.this, R.drawable.ic_wrong);
                    }
                }
                calculationType = 2;
            } else {
                Constant.showToast(getResources().getString(R.string.errorhybridarea), PlantAreaCalcuationActivity.this, R.drawable.ic_wrong);
            }
        } else {
            Constant.showToast(getResources().getString(R.string.errorareaban), PlantAreaCalcuationActivity.this, R.drawable.ic_wrong);
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        if (calculationType == 2) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_double_zoom));
            marker.setRotation(180);
        } else {
            Constant.showToast(getResources().getString(R.string.errorwakingpointmovenotallowed), PlantAreaCalcuationActivity.this, R.drawable.ic_wrong);
        }
        return false;
    }

}