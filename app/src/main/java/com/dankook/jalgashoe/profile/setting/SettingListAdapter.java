package com.dankook.jalgashoe.profile.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.data.vo.SettingItemVO;

import java.util.ArrayList;

/**
 * Created by yeseul on 2018-04-11.
 */

public class SettingListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SettingItemVO> itemList;

    public SettingListAdapter(Context context, ArrayList<SettingItemVO> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
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
    public View getView(int i, View view, ViewGroup parent) {
        SettingItemVO item = itemList.get(i);
        view = LayoutInflater.from(context).inflate(R.layout.layout_list_setting_item, null);

        TextView itemTextView = view.findViewById(R.id.setting_item_text);
        itemTextView.setText(item.getSettingItemText());

        ImageView itemImageView = view.findViewById(R.id.setting_item_image);
        itemImageView.setImageResource(item.getSettingItemImage());

        return view;
    }
}
