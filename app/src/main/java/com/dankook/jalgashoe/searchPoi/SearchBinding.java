package com.dankook.jalgashoe.searchPoi;

import android.databinding.BindingAdapter;
import android.widget.ListView;

import com.dankook.jalgashoe.searchPoi.search.SearchFragment;
import com.dankook.jalgashoe.searchPoi.poiList.PoiListFragment;
import com.skt.Tmap.TMapPOIItem;

import java.util.List;

/**
 * Created by yeseul on 2018-05-08.
 */

public class SearchBinding {

    @BindingAdapter(value = {"searchItems"})
    public static void searchListBinding(ListView listView, List<String> items){
        SearchFragment.AutoCompListAdapter adapter = (SearchFragment.AutoCompListAdapter) listView.getAdapter();
        if(adapter != null){
            adapter.updateList(items);
        }
    }

    @BindingAdapter(value = {"poiItems"})
    public static void poiListBinding(ListView listView, List<TMapPOIItem> items){
        PoiListFragment.PoiListAdapter adapter = (PoiListFragment.PoiListAdapter) listView.getAdapter();
        if(adapter != null){
            adapter.updateList(items);
        }
    }

}
