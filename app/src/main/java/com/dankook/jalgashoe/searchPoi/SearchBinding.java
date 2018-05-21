package com.dankook.jalgashoe.searchPoi;

import android.databinding.BindingAdapter;
import android.widget.ListView;

import com.dankook.jalgashoe.searchPoi.search.AutoSearchFragment;
import com.dankook.jalgashoe.searchPoi.poiList.PoiListFragment;
import com.skt.Tmap.TMapPOIItem;

import java.util.List;

/**
 * Created by yeseul on 2018-05-08.
 */

public class SearchBinding {

    @BindingAdapter(value = {"searchItems"})
    public static void searchListBinding(ListView listView, List<String> items){
        AutoSearchFragment.AutoCompListAdapter adapter = (AutoSearchFragment.AutoCompListAdapter) listView.getAdapter();
        if(adapter != null){
            adapter.updateList(items);
        }
    }
}
