package com.dankook.jalgashoe.map;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageView;

import com.dankook.jalgashoe.BaseActivity;
import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.databinding.ActivityMapBinding;
import com.dankook.jalgashoe.map.path.MapPathActivity;
import com.dankook.jalgashoe.searchPoi.SearchActivity;
import com.dankook.jalgashoe.util.SnackbarUtils;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import static com.dankook.jalgashoe.util.Constant.TAB_COUNT;
import static com.dankook.jalgashoe.util.Constant.TAB_ICON;

public class MapActivity extends BaseActivity<ActivityMapBinding> implements MapNavigator, TMapGpsManager.onLocationChangedCallback {

    private MapViewModel viewModel;
    private TMapGpsManager gpsManager;
    private TMapView mapView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new MapViewModel();// 뷰모델 생성
        viewModel.setNavigator(this);

        binding.setViewModel(viewModel);// xml 레이아웃에 뷰모델 등록

        mapView = new TMapView(this);// tMap view 생성
        binding.mapviewLayout.addView(mapView);// 화면에 적용

        gpsManager = new TMapGpsManager(this);// gps 위치 탐색 객체 생성
        gpsManager.setLocationCallback();// 위치 변경 콜백 등록

        viewModel.start(mapView, gpsManager);

        setupTabLayout();
    }

    private void setupTabLayout() {

        for (int i = 0; i < TAB_COUNT; i++) {
            binding.tabLayout.addTab(binding.tabLayout.newTab());
            View view = getLayoutInflater().inflate(R.layout.layout_main_tab, null);
            ImageView icon = view.findViewById(R.id.icon);
            icon.setImageResource(TAB_ICON[i]);
            binding.tabLayout.getTabAt(i).setCustomView(view);
        }

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                changeTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onLocationChange(Location location) {
        TMapPoint point = new TMapPoint(location.getLatitude(), location.getLongitude());
        viewModel.changeCurrentLocation(point);
    }

    @Override
    public void startSearchActivity() {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivityForResult(intent, SearchActivity.REQUEST_MAP_SEARCH);
        overridePendingTransition(R.anim.enter_no_anim, R.anim.exit_no_anim);
    }

    @Override
    public void startPathActivity(String s) {
        Intent intent = new Intent(getApplicationContext(), MapPathActivity.class);
        intent.putExtra("document", s);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showSnackBar(String message) {
        SnackbarUtils.showSnackbar(binding.getRoot(), message);
    }
}
