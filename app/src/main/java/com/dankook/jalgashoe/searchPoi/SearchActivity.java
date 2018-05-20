package com.dankook.jalgashoe.searchPoi;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dankook.jalgashoe.BaseActivity;
import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.databinding.ActivitySearchBinding;
import com.dankook.jalgashoe.util.SnackbarUtils;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

public class SearchActivity extends BaseActivity<ActivitySearchBinding> implements SearchActivityNavigator{

    public static final int REQUEST_MAP_SEARCH = 101;
    public static final int RESULT_DESTINATION = 1;
    public static final int RESULT_DEPARTURE = 2;
    public static final String BUNDLE_EXTRA_LATITUDE = "BUNDLE_EXTRA_LATITUDE";
    public static final String BUNDLE_EXTRA_LONGITUDE = "BUNDLE_EXTRA_LONGITUDE";
    public static final String BUNDLE_EXTRA_POINT = "BUNDLE_EXTRA_POINT";
    public static final String STRING_EXTRA_NAME = "STRING_EXTRA_NAME";

    public final ObservableBoolean showHistory = new ObservableBoolean(true);

    private TMapView tMapView;
    private SearchViewModel viewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tMapView = new TMapView(this);

        viewModel = new SearchViewModel();
        viewModel.setNavigator(this);

        binding.setActivity(this);
        binding.setViewModel(viewModel);

        viewModel.start(tMapView);
    }

    @Override
    public void onActivityFinish() {
        finish();
        overridePendingTransition(R.anim.enter_no_anim, R.anim.exit_no_anim);
    }

    @Override
    public void showSnackBar(String message) {
        SnackbarUtils.showSnackbar(binding.getRoot(), message);
    }

    @Override
    public void finishActivityWithResult(int resultCode, TMapPoint poiPoint) {
        Intent intent = getIntent();

        Bundle bundle = new Bundle();
        bundle.putDouble(BUNDLE_EXTRA_LONGITUDE, poiPoint.getLongitude());
        bundle.putDouble(BUNDLE_EXTRA_LATITUDE, poiPoint.getLatitude());

        intent.putExtra(BUNDLE_EXTRA_POINT, bundle);
        intent.putExtra(STRING_EXTRA_NAME, viewModel.selectedPoi.get().name);
        setResult(resultCode, intent);
        finish();
        overridePendingTransition(R.anim.enter_no_anim, R.anim.exit_no_anim);
    }

}
