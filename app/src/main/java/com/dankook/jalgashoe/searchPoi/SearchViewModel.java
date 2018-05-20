package com.dankook.jalgashoe.searchPoi;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.graphics.Bitmap;
import android.text.Editable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.util.BitmapTextUtil;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

/**
 * Created by yeseul on 2018-05-08.
 */

public class SearchViewModel {

    public final ObservableField<String> searchText = new ObservableField<>();
    public final ObservableBoolean isSearching = new ObservableBoolean();
    public final ObservableBoolean showPoiList = new ObservableBoolean();

    public final ObservableList<TMapPOIItem> poiItems = new ObservableArrayList<>();
    public final ObservableList<String> autoCompItems = new ObservableArrayList<>();

    public final ObservableField<TMapPOIItem> selectedPoi = new ObservableField<>();

    private SearchActivityNavigator navigator;
    private TMapView tMapView;
    private TMapData tMapData;

    public SearchViewModel(){
        tMapData = new TMapData();
    }

    public void start(TMapView tMapView) {
        this.tMapView = tMapView;

        setupMap();

        isSearching.set(true);
        showPoiList.set(false);
    }

    public void setNavigator(SearchActivityNavigator navigator) {
        this.navigator = navigator;
    }

    public void onClickBackButton(){
        navigator.onActivityFinish();
    }

    public void onSearchTextChanged(Editable editable){

        if(!isSearching.get()){
            isSearching.set(true);
        }

        searchText.set(editable.toString());

        if (editable == null || editable.toString().equals("")) {
            isSearching.set(false);
            return;
        }

        searchFromData(editable.toString());
    }

    public void searchFromData(String searchKey){

        tMapData.autoComplete(searchKey, new TMapData.AutoCompleteListenerCallback() {
            @Override
            public void onAutoComplete(ArrayList<String> arrayList) {
                autoCompItems.clear();

                if(arrayList == null || arrayList.size() == 0){
                    return;
                }

                autoCompItems.addAll(arrayList);
            }
        });

    }

    // 지도 설정
    private void setupMap() {
        // key 설정
        tMapView.setSKTMapApiKey("b7bfb971-b45e-40d9-8edb-8b2b46bfb04d");

        // 지도 줌레벨 설정
        tMapView.setZoomLevel(17);

        changeCurrentPoiLocation(selectedPoi.get());

        // 마커 아이콘 설정
        setLocationIcon(1);

        tMapView.setIconVisibility(true);
    }

    public void setLocationIcon(final int index){

        Glide.with(tMapView.getContext())
                .load(R.drawable.ic_marker)
                .asBitmap()
                .override(50, 50)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Bitmap icon = BitmapTextUtil.writeTextOnBitmap(resource, String.valueOf(index));
                        tMapView.setIcon(icon);
                    }
                });
    }

    // 현재위치 지정
    public void changeCurrentPoiLocation(TMapPOIItem poi){
        if(poi != null) {
            tMapView.setCenterPoint(Double.parseDouble(poi.noorLon), Double.parseDouble(poi.noorLat), true);
            tMapView.setLocationPoint(Double.parseDouble(poi.noorLon), Double.parseDouble(poi.noorLat));
        }
    }

    public void onSearchItemClick(int position){

        isSearching.set(false);
        showPoiList.set(true);

        String keyword = autoCompItems.get(position);

        tMapData.findAllPOI(keyword, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                poiItems.clear();

                if(arrayList == null || arrayList.size() == 0){
                    return;
                }

                poiItems.addAll(arrayList);

                changeCurrentPoiLocation(poiItems.get(0));
                selectedPoi.set(poiItems.get(0));

                for(final TMapPOIItem item : poiItems){
                    tMapData.convertGpsToAddress(Double.parseDouble(item.noorLon), Double.parseDouble(item.noorLat),
                            new TMapData.ConvertGPSToAddressListenerCallback() {
                        @Override
                        public void onConvertToGPSToAddress(String s) {
                            item.address = s;
//                            navigator.notifyAdapter();
                        }
                    });
                }
            }
        });
    }

    public void showPoiDetail(int position) {
        selectedPoi.set(poiItems.get(position));
        changeCurrentPoiLocation(poiItems.get(position));
    }

    public void onDestinationClick(){
        navigator.finishActivityWithResult(SearchActivity.RESULT_DESTINATION, selectedPoi.get().getPOIPoint());
    }

    public void onDepartureClick(){
        navigator.finishActivityWithResult(SearchActivity.RESULT_DEPARTURE, selectedPoi.get().getPOIPoint());
    }
}
