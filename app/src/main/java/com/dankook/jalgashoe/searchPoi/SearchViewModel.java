package com.dankook.jalgashoe.searchPoi;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.text.Editable;
import android.util.Log;

import com.dankook.jalgashoe.searchPoi.poiList.PoiListNavigator;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

/**
 * Created by yeseul on 2018-05-08.
 */

public class SearchViewModel {

    private final String TAG = "SearchViewModel";

    public final ObservableBoolean showHistory = new ObservableBoolean();

    public final ObservableField<String> searchText = new ObservableField<>();

    public final ObservableList<TMapPOIItem> poiItems = new ObservableArrayList<>();
    public final ObservableList<String> autoCompItems = new ObservableArrayList<>();

    public final ObservableField<TMapPOIItem> selectedPoi = new ObservableField<>();

    private SearchActivityNavigator navigator;
    private PoiListNavigator poiNavigator;

    private TMapView tMapView;
    private TMapData tMapData;

    public SearchViewModel(){
        tMapData = new TMapData();
    }

    public void setTMapView(TMapView tMapView) {
        this.tMapView = tMapView;
        setupMap();
    }

    public TMapView getTMapView() {
        return tMapView;
    }

    public void setNavigator(SearchActivityNavigator navigator) {
        this.navigator = navigator;
    }

    public void setPoiNavigator(PoiListNavigator poiNavigator) {
        this.poiNavigator = poiNavigator;
    }

    public void start() {
        showHistory.set(true);
    }

    public void onSearchTextChanged(Editable editable){
        String inputText = "";

        if(editable != null) {
            inputText = editable.toString();
        }

        // 입력 텍스트가 빈 경우 검색어 히스토리 목록
        // 텍스트가 입력된 경우 검색어 자동완성 목록을 보여줌
        if(inputText.equals("")){
            showHistory.set(true);
        } else{
            showHistory.set(false);
        }

        searchText.set(inputText);

        // get auto search list
        searchFromData(inputText);
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

        tMapView.setIconVisibility(true);
    }

    public void changeCurrentPoiLocation(TMapPOIItem item){
        if(item != null) {
            tMapView.setCenterPoint(item.getPOIPoint().getLongitude(), item.getPOIPoint().getLatitude(), true);
            tMapView.setLocationPoint(item.getPOIPoint().getLongitude(), item.getPOIPoint().getLatitude());
        }
    }

    public void onSearchItemClick(String keyword){
        navigator.changeToPoiListFragment();

        tMapData.findAllPOI(keyword, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                poiItems.clear();

                if(arrayList == null || arrayList.size() == 0){
                    return;
                }

                poiItems.addAll(arrayList);

                setSelectedPoi(0);

                for(final TMapPOIItem item : poiItems){
                    tMapData.convertGpsToAddress(item.getPOIPoint().getLatitude(), item.getPOIPoint().getLongitude(),
                            new TMapData.ConvertGPSToAddressListenerCallback() {
                        @Override
                        public void onConvertToGPSToAddress(String s) {
                            item.address = s;
                            poiNavigator.addAddress();
                        }
                    });
                }

                poiNavigator.notifyAdapter();
            }
        });
    }

    public void setSelectedPoi(int position) {
        TMapMarkerItem marker = tMapView.getMarkerItemFromID(poiItems.get(position).getPOIID());
        tMapView.bringMarkerToFront(marker);

        selectedPoi.set(poiItems.get(position));
        changeCurrentPoiLocation(poiItems.get(position));
    }

    public void onDestinationClick(){
        navigator.finishActivityWithResult(SearchActivity.RESULT_DESTINATION, selectedPoi.get().getPOIPoint());
    }

    public void onDepartureClick(){
        navigator.finishActivityWithResult(SearchActivity.RESULT_DEPARTURE, selectedPoi.get().getPOIPoint());
    }

    public void onSelectCurrentLocation(){
        navigator.showSnackBar("현위치로 설정");
    }

    public void onSelectFromMap(){
        navigator.showSnackBar("지도에서 설정");
    }
}
