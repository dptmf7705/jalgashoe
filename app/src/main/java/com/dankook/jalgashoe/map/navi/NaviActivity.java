package com.dankook.jalgashoe.map.navi;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.dankook.jalgashoe.BaseActivity;
import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.data.vo.PathInfoVO;
import com.dankook.jalgashoe.databinding.ActivityNavigationBinding;
import com.dankook.jalgashoe.util.SnackbarUtils;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

public class NaviActivity extends BaseActivity<ActivityNavigationBinding> implements NaviNavigator, TMapGpsManager.onLocationChangedCallback {

    public static final String EXTRA_START_LATITUDE = "EXTRA_START_LATITUDE";
    public static final String EXTRA_START_LONGITUDE = "EXTRA_START_LONGITUDE";
    public static final String EXTRA_START_ADDRESS = "EXTRA_START_ADDRESS";
    public static final String EXTRA_END_LATITUDE = "EXTRA_END_LATITUDE";
    public static final String EXTRA_END_LONGITUDE = "EXTRA_END_LONGITUDE";
    public static final String EXTRA_END_ADDRESS = "EXTRA_END_ADDRESS";

    private NaviViewModel viewModel;

    private PathInfoVO pathInfo = new PathInfoVO();

    private TMapView tMapView;
    private TMapGpsManager gpsManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_navigation;
    }

    @Override
    protected void onResume() {
        super.onResume();
        gpsManager.OpenGps();
    }

    @Override
    protected void onPause() {
        gpsManager.CloseGps();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tMapView = new TMapView(this);
        binding.mapView.addView(tMapView);

        gpsManager = new TMapGpsManager(this);

        getIntentInfo();

        viewModel = new NaviViewModel();
        binding.setViewModel(viewModel);
        binding.setPath(pathInfo);

        viewModel.setNavigator(this);
        viewModel.start(tMapView, gpsManager, pathInfo);
    }

    private void getIntentInfo(){
        Intent intent = getIntent();

        pathInfo.setStartAddress(intent.getStringExtra(EXTRA_START_ADDRESS));
        pathInfo.setEndAddress(intent.getStringExtra(EXTRA_END_ADDRESS));

        TMapPoint startPoint = new TMapPoint(
                    intent.getDoubleExtra(EXTRA_START_LATITUDE, 0),
                    intent.getDoubleExtra(EXTRA_START_LONGITUDE, 0)
            );
        pathInfo.setStartPoint(startPoint);

        TMapPoint endPoint = new TMapPoint(
                    intent.getDoubleExtra(EXTRA_END_LATITUDE, 0),
                    intent.getDoubleExtra(EXTRA_END_LONGITUDE, 0)
            );
        pathInfo.setEndPoint(endPoint);
    }

    @Override
    public void showSnackBar(String message) {
        SnackbarUtils.showSnackbar(binding.getRoot(), message);
    }

    @Override
    public void onLocationChange(Location location) {
        viewModel.changeCurrentLocation(new TMapPoint(location.getLatitude(), location.getLongitude()));
    }
}
