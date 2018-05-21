package com.dankook.jalgashoe.searchPoi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dankook.jalgashoe.BaseActivity;
import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.data.vo.SearchItemVO;
import com.dankook.jalgashoe.databinding.ActivitySearchBinding;
import com.dankook.jalgashoe.databinding.HistoryListItemBinding;
import com.dankook.jalgashoe.searchPoi.poiList.PoiListFragment;
import com.dankook.jalgashoe.searchPoi.search.AutoSearchFragment;
import com.dankook.jalgashoe.sqliteDB.SQLiteHelper;
import com.dankook.jalgashoe.util.DateUtil;
import com.dankook.jalgashoe.util.FragmentUtil;
import com.dankook.jalgashoe.util.SnackbarUtils;
import com.skt.Tmap.TMapPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.dankook.jalgashoe.Constant.DATABASE_NAME_SEARCH;
import static com.dankook.jalgashoe.Constant.SQL_DELETE_SEARCH_ITEM_BY_ID;
import static com.dankook.jalgashoe.Constant.SQL_INSERT_INTO_SEARCH;
import static com.dankook.jalgashoe.Constant.SQL_SELECT_SEARCH_LIST;

public class SearchActivity extends BaseActivity<ActivitySearchBinding> implements SearchActivityNavigator{

    private SQLiteHelper dbHelper;
    private SQLiteDatabase database;
    private InputMethodManager keyboardManager;

    public static final int REQUEST_MAP_SEARCH = 101;
    public static final int RESULT_DESTINATION = 1;
    public static final int RESULT_DEPARTURE = 2;
    public static final String BUNDLE_EXTRA_LATITUDE = "BUNDLE_EXTRA_LATITUDE";
    public static final String BUNDLE_EXTRA_LONGITUDE = "BUNDLE_EXTRA_LONGITUDE";
    public static final String BUNDLE_EXTRA_POINT = "BUNDLE_EXTRA_POINT";
    public static final String STRING_EXTRA_NAME = "STRING_EXTRA_NAME";

    private SearchViewModel viewModel;
    private HistoryAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        keyboardManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        viewModel = new SearchViewModel();
        viewModel.setNavigator(this);

        binding.setViewModel(viewModel);

        viewModel.start();

        setupHistoryList();
        setupSearchText();
        changeToAutoSearchFragment();
    }

    private void setupSearchText() {
        // 검색 버튼 클릭시 DB 검색 히스토리 저장
        binding.searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean bool = false;
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    try {
                        String sql = SQL_INSERT_INTO_SEARCH
                                + "values('" + textView.getText().toString() + "', '" + DateUtil.getCurrentDate() + "');";
                        database.execSQL(sql);
                    } catch (SQLiteException e){
                        showSnackBar("Fail : INSERT_INTO_SEARCH");
                    }
                    startSearchPoi(textView.getText().toString());
                    bool = true;
                }
                return bool;
            }
        });
    }

    private void startSearchPoi(String keyword) {
        binding.searchText.setText(keyword);
        changeToPoiListFragment();
        viewModel.onSearchItemClick(keyword); // poi 검색 실행
    }

    private void setupHistoryList() {
        List<SearchItemVO> searchList = getSearchHistory();

        // set adapter
        adapter = new HistoryAdapter(this, searchList);
        adapter.setItemClickListener(new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startSearchPoi(adapter.getItemList().get(position).getText()); // poi 검색 실행
            }
        });
        adapter.setOnDeleteClickListener(new HistoryAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View view, int id) {
                database.execSQL(SQL_DELETE_SEARCH_ITEM_BY_ID + id + ";"); // DB 에서 삭제
            }
        });
        binding.historyList.setAdapter(adapter);

        // set list view header
        View header = getLayoutInflater().inflate(R.layout.history_list_header, null);
        binding.historyList.addHeaderView(header);

        // set list view footer
        View footer = getLayoutInflater().inflate(R.layout.history_list_footer, null);
        binding.historyList.addFooterView(footer);
    }

    private List<SearchItemVO> getSearchHistory(){
        List<SearchItemVO> searchList = new ArrayList<>();
        dbHelper = new SQLiteHelper(this, DATABASE_NAME_SEARCH, null, 1);

        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLiteException e){
            showSnackBar("Fail to get Search History");
            return searchList;
        }

        Cursor cursor = database.rawQuery(SQL_SELECT_SEARCH_LIST, null);
        while (cursor.moveToNext()) {
            SearchItemVO vo = new SearchItemVO(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
            searchList.add(vo);
        }

        return searchList;
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

    @Override
    public void changeToAutoSearchFragment() {
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActivityFinish();
            }
        });
        binding.buttonContainer.setVisibility(View.VISIBLE);

        // 검색 입력폼 활성화
        binding.searchText.setText("");
        binding.searchText.setFocusableInTouchMode(true);
        binding.searchText.setFocusable(true);

        keyboardManager.showSoftInput(binding.searchText, 0); // keyboard 열기

        // listView 첫 생성이 아닌경우 리스트 갱신
        if(binding.historyList.getHeaderViewsCount() != 0){
            adapter.updateList(getSearchHistory());
        }

        // AutoSearchFragment 로 교체
        AutoSearchFragment fragment = new AutoSearchFragment();
        fragment.setViewModel(viewModel);
        FragmentUtil.changeFragment(getSupportFragmentManager(), R.id.frame_layout, fragment);
    }

    @Override
    public void changeToPoiListFragment() {
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToAutoSearchFragment();
            }
        });
        binding.buttonContainer.setVisibility(View.GONE);

        // 검색 입력폼 비활성화
        binding.searchText.setFocusable(false);

        keyboardManager.hideSoftInputFromWindow(binding.searchText.getWindowToken(), 0); // keyboard 닫기

        // PoiListFragment 로 교체
        PoiListFragment fragment = new PoiListFragment();
        fragment.setViewModel(viewModel);
        FragmentUtil.changeFragment(getSupportFragmentManager(), R.id.frame_layout, fragment);
    }

    /** 검색 결과 나타낼 리스트뷰 어댑터 */
    public static class HistoryAdapter extends BaseAdapter {

        private Context context;
        private List<SearchItemVO> itemList;

        private OnItemClickListener itemClickListener;
        private OnDeleteClickListener onDeleteClickListener;

        public HistoryAdapter(Context context, List<SearchItemVO> items){
            this.context = context;
            this.itemList = items;
            Collections.reverse(items);
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

        public List<SearchItemVO> getItemList() {
            return itemList;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            final SearchItemVO item = (SearchItemVO) getItem(position);
            HistoryListItemBinding binding;

            if(view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.history_list_item, null);
            }

            binding = DataBindingUtil.bind(view);
            binding.setItem(item);

            binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDeleteClick(view, item.getId());
                    }

                    itemList.remove(position);
                    notifyDataSetChanged();
                }
            });

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

        public void updateList(List<SearchItemVO> items){
            this.itemList = items;
            notifyDataSetChanged();
            Collections.reverse(items);
        }

        public void setItemClickListener(OnItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
            this.onDeleteClickListener = onDeleteClickListener;
        }

        public interface OnItemClickListener{

            void onItemClick(View view, int position);
        }

        public interface OnDeleteClickListener{

            void onDeleteClick(View view, int id);
        }

    }

}
