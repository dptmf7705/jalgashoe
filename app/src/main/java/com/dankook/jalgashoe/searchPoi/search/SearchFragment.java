package com.dankook.jalgashoe.searchPoi.search;

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
import com.dankook.jalgashoe.databinding.FragmentSearchBinding;
import com.dankook.jalgashoe.databinding.SearchListItemBinding;
import com.dankook.jalgashoe.searchPoi.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeseul on 2018-05-19.
 */

public class SearchFragment extends BaseFragment<FragmentSearchBinding> {

    private AutoCompListAdapter adapter;
    private SearchViewModel viewModel;

    public void setViewModel(SearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.setViewModel(viewModel);

        setupSearchList();
    }

    private void setupSearchList() {
        adapter = new AutoCompListAdapter(context, new ArrayList<String>(0));
        adapter.setItemClickListener(new AutoCompListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                keyboard.hideKeyboard();
                viewModel.onSearchItemClick(position);
            }
        });
        binding.searchListView.setAdapter(adapter);
    }

    /** 검색어 자동완성 어댑터 */
    public static class AutoCompListAdapter extends BaseAdapter {

        private List<String> itemList;
        private Context context;
        private OnItemClickListener itemClickListener;
        private OnItemLongClickListener itemLongClickListener;

        public AutoCompListAdapter(Context context, List<String> items){
            this.context = context;
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
            String item = (String) getItem(position);
            SearchListItemBinding binding;

            if(view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.search_list_item, null);
            }

            binding = DataBindingUtil.bind(view);

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

            binding.textName.setText(item);

            return view;
        }

        public void updateList(List<String> items){
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
