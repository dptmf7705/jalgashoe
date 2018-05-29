package com.dankook.jalgashoe.navi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.dankook.jalgashoe.BaseActivity;
import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.data.vo.PathInfoVO;
import com.dankook.jalgashoe.data.vo.PathPointVO;
import com.dankook.jalgashoe.databinding.ActivityNavigationBinding;
import com.dankook.jalgashoe.service.LocationService;
import com.dankook.jalgashoe.util.BitmapUtil;
import com.dankook.jalgashoe.util.SnackbarUtils;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

public class NaviActivity extends BaseActivity<ActivityNavigationBinding> implements NaviNavigator {

    public static final String EXTRA_START_LATITUDE = "EXTRA_START_LATITUDE";
    public static final String EXTRA_START_LONGITUDE = "EXTRA_START_LONGITUDE";
    public static final String EXTRA_START_ADDRESS = "EXTRA_START_ADDRESS";
    public static final String EXTRA_END_LATITUDE = "EXTRA_END_LATITUDE";
    public static final String EXTRA_END_LONGITUDE = "EXTRA_END_LONGITUDE";
    public static final String EXTRA_END_ADDRESS = "EXTRA_END_ADDRESS";

    public static final String EXTRA_FROM_SERVICE_LATITUDE = "EXTRA_FROM_SERVICE_LATITUDE";
    public static final String EXTRA_FROM_SERVICE_LONGITUDE = "EXTRA_FROM_SERVICE_LONGITUDE";

    private NaviViewModel viewModel;

    private PathInfoVO pathInfo = new PathInfoVO();

    /** T Map 관련*/
    private TMapView tMapView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_navigation;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        finishNavigation();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupMapView();
        getIntentInfo();

        viewModel = new NaviViewModel();
        viewModel.setNavigator(this);

        binding.setViewModel(viewModel);
        binding.setPath(pathInfo);
        binding.setNavi(viewModel.getNaviInfo());

        viewModel.start(tMapView, pathInfo);
    }

    private void setupMapView() {

        tMapView = new TMapView(this);
        binding.mapView.addView(tMapView);

        tMapView.setSKTMapApiKey("b7bfb971-b45e-40d9-8edb-8b2b46bfb04d"); // key 설정
        tMapView.setZoomLevel(19); // 지도 줌레벨 설정 (7~19)
        tMapView.setIconVisibility(true); // 현재위치 아이콘 나타내기
        tMapView.setMapType(TMapView.MAPTYPE_HYBRID); // 일반 지도 사용

        tMapView.setCompassMode(true); // 나침반 모드 해제
        tMapView.setSightVisible(true); // 시야 표출 해제

//        tMapView.setIcon(BitmapUtil.getScaledBitmap(tMapView.getResources(), R.drawable.ic_navigation, 130)); // 현재위치 아이콘
        tMapView.setTrackingMode(true); // 사용자 위치 따라서 이동하는 모드로

        // 출발, 도착 아이콘 변경
        Bitmap startIcon = BitmapUtil.writeTextOnDrawable(tMapView.getResources(), R.drawable.ic_location_active, 130, "출발");
        Bitmap endIcon = BitmapUtil.writeTextOnDrawable(tMapView.getResources(), R.drawable.ic_marker_destination, 130, "도착");
        tMapView.setTMapPathIcon(startIcon, endIcon);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processLocationCallback(intent);
    }

    private void processLocationCallback(Intent intent) {
        if(intent != null) {
            double latitude = intent.getDoubleExtra(EXTRA_FROM_SERVICE_LATITUDE, 0);
            double longitude = intent.getDoubleExtra(EXTRA_FROM_SERVICE_LONGITUDE, 0);

            viewModel.changeCurrentLocation(new TMapPoint(latitude, longitude));
            viewModel.calculateCurrentDistance(new TMapPoint(latitude, longitude));
        }
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
    public void startNavigation() {
        // gps 서비스 시작
        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        startService(intent);

        // 블루투스로 출발신호 보내기
    }

    @Override
    public void onNextPath(PathPointVO vo) {

    }

    @Override
    public void finishNavigation() {
        // gps 서비스 종료
        Intent intent = new Intent(this, LocationService.class);
        stopService(intent);
    }

}
