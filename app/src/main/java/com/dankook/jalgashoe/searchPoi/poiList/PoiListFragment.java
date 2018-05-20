package com.dankook.jalgashoe.searchPoi.poiList;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dankook.jalgashoe.BaseFragment;
import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.databinding.FragmentPoiListBinding;
import com.dankook.jalgashoe.databinding.PoiListItemBinding;
import com.dankook.jalgashoe.searchPoi.search.SearchNavigator;
import com.dankook.jalgashoe.searchPoi.SearchViewModel;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.List;

public class PoiListFragment extends BaseFragment<FragmentPoiListBinding> implements SearchNavigator{

    private PoiListAdapter poiAdapter;
    private SearchViewModel viewModel;
    private TMapView poiMapView;

    private Runnable loadMarkerIcon = new Runnable() {
        @Override
        public void run() {
            viewModel.setLocationIcon(viewModel.poiItems.indexOf(viewModel.selectedPoi.get()) + 1);
        }
    };

    private Runnable updateLocationAddress = new Runnable() {
        @Override
        public void run() {
            poiAdapter.notifyDataSetChanged();
        }
    };

    public void setViewModel(SearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_poi_list;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        poiMapView = new TMapView(context);
        binding.poiMapView.addView(poiMapView);
    }

    private void setupPoiList() {
        poiAdapter = new PoiListAdapter(context, viewModel, new ArrayList<TMapPOIItem>(0));
        poiAdapter.setItemClickListener(new PoiListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                viewModel.showPoiDetail(position);
                getActivity().runOnUiThread(updateLocationAddress);
                getActivity().runOnUiThread(loadMarkerIcon);
//                poiAdapter.notifyDataSetChanged();
            }
        });
        binding.poiListView.setAdapter(poiAdapter);
    }

    @Override
    public void notifyAdapter() {
//        poiAdapter.notifyDataSetChanged();
        getActivity().runOnUiThread(updateLocationAddress);
    }

    /** 검색 결과 나타낼 리스트뷰 어댑터 */
    public static class PoiListAdapter extends BaseAdapter {

        private Context context;
        private SearchViewModel viewModel;
        private List<TMapPOIItem> itemList;

        private OnItemClickListener itemClickListener;
        private OnItemLongClickListener itemLongClickListener;

        public PoiListAdapter(Context context, SearchViewModel viewModel, List<TMapPOIItem> items){
            this.context = context;
            this.viewModel = viewModel;
            this.itemList = items;
        }

        @Override
        public int getCount() {
            return itemList != null ? itemList.size() : 0;
        }

        @Override
        public Object getItem(int i) {
            return itemList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            TMapPOIItem item = (TMapPOIItem) getItem(position);
            PoiListItemBinding binding;

            if(view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.poi_list_item, null);
            }

            binding = DataBindingUtil.bind(view);
            binding.setItem(item);
            binding.locationButton.setSelected(viewModel.selectedPoi.get() == item);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(itemClickListener != null){
                        itemClickListener.onItemClick(view, position);
                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if (itemLongClickListener != null) {
                        itemLongClickListener.onItemLongClick(view, position);
                    }

                    return false;
                }
            });

            return view;
        }

        public void updateList(List<TMapPOIItem> items){
            this.itemList = items;
            notifyDataSetChanged();
        }

        public void setItemClickListener(OnItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
            this.itemLongClickListener = itemLongClickListener;
        }

        public interface OnItemClickListener{

            void onItemClick(View view, int position);
        }

        public interface OnItemLongClickListener{

            void onItemLongClick(View view, int position);
        }

    }

}
