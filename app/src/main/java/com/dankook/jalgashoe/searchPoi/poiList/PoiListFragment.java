package com.dankook.jalgashoe.searchPoi.poiList;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dankook.jalgashoe.BaseFragment;
import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.databinding.FragmentPoiListBinding;
import com.dankook.jalgashoe.databinding.PoiListItemBinding;
import com.dankook.jalgashoe.searchPoi.SearchViewModel;
import com.dankook.jalgashoe.util.BitmapTextUtil;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapView;

import java.util.List;

public class PoiListFragment extends BaseFragment<FragmentPoiListBinding> implements PoiListNavigator {
    private final String TAG = "PoiListFragment";

    private TMapView tMapView;
    private PoiListAdapter adapter;
    private SearchViewModel viewModel;

    private Runnable addAddress = new Runnable() {
        @Override
        public void run() {
            adapter.notifyDataSetChanged();
//            adapter.updateList(viewModel.poiItems);
        }
    };
    private Runnable notifyChange = new Runnable() {
        @Override
        public void run() {
            if (viewModel.poiItems != null) {
                for (int i = 0; i < viewModel.poiItems.size(); i++) {
                    addMarker(i);
                }
            }
            adapter.updateList(viewModel.poiItems);
        }
    };

    public void setViewModel(SearchViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.setPoiNavigator(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_poi_list;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tMapView = new TMapView(context);
        viewModel.setTMapView(tMapView);

        binding.poiMapView.addView(tMapView);

        binding.setViewModel(viewModel);

        setupPoiList();
    }

    private void setupPoiList() {
        Log.d(TAG, "setupPoiList() viewModel.poiItems size: " + viewModel.poiItems.size());
        adapter = new PoiListAdapter(context, viewModel, viewModel.poiItems);
        binding.poiListView.setAdapter(adapter);
        binding.poiListView.setEmptyView(binding.textEmpty);

        adapter.setItemClickListener(new PoiListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                viewModel.getTMapView().getMarkerItemFromID(viewModel.selectedPoi.get().getPOIID())
                        .setIcon(BitmapTextUtil.writeTextOnDrawable(getResources(), R.drawable.ic_marker_deactive, 100, viewModel.poiItems.indexOf(viewModel.selectedPoi.get()) + 1));

                viewModel.setSelectedPoi(position);
                viewModel.getTMapView().getMarkerItemFromID(viewModel.selectedPoi.get().getPOIID())
                        .setIcon(BitmapTextUtil.writeTextOnDrawable(getResources(), R.drawable.ic_marker, 120, position+1));

                adapter.notifyDataSetChanged();
            }
        });
    }

    public void addMarker(int position) {
        TMapMarkerItem marker = new TMapMarkerItem();
        marker.setIcon(BitmapTextUtil.writeTextOnDrawable(getResources(), R.drawable.ic_marker_deactive, 100, position+1));
        marker.setName(viewModel.poiItems.get(position).getPOIName());
        marker.setTMapPoint(viewModel.poiItems.get(position).getPOIPoint());

        tMapView.addMarkerItem(viewModel.poiItems.get(position).getPOIID(), marker); // 지도에 마커 추가
        tMapView.sendMarkerToBack(marker);
    }

    @Override
    public void notifyAdapter() {
        Log.d(TAG, "notifyAdapter() viewModel.poiItems size: " + viewModel.poiItems.size());
        getActivity().runOnUiThread(notifyChange);
    }

    @Override
    public void addAddress() {
        getActivity().runOnUiThread(addAddress);
    }

    /** 검색 결과 나타낼 리스트뷰 어댑터 */
    public static class PoiListAdapter extends BaseAdapter {

        private Context context;
        private SearchViewModel viewModel;
        private List<TMapPOIItem> itemList;

        private OnItemClickListener itemClickListener;

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
            binding.textAddress.setText(item.address);

            boolean isSelectedItem = viewModel.selectedPoi.get() == item;
            if(isSelectedItem){
                binding.locationButton.setImageBitmap(BitmapTextUtil.writeTextOnDrawable(context.getResources(), R.drawable.ic_marker, 100, position+1));
            } else {
                binding.locationButton.setImageBitmap(BitmapTextUtil.writeTextOnDrawable(context.getResources(), R.drawable.ic_marker_deactive, 100, position+1));
            }
            binding.locationButton.setSelected(isSelectedItem);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(itemClickListener != null){
                        itemClickListener.onItemClick(view, position);
                    }
                }
            });

            return view;
        }

        public void updateList(List<TMapPOIItem> items){
            this.itemList = items;
            if(items != null && items.size() > 0){
                viewModel.getTMapView().getMarkerItemFromID(items.get(0).getPOIID())
                        .setIcon(BitmapTextUtil.writeTextOnDrawable(context.getResources(), R.drawable.ic_marker, 120, 1));
            }
            notifyDataSetChanged();
        }

        public void setItemClickListener(OnItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public interface OnItemClickListener{

            void onItemClick(View view, int position);
        }
    }

}
