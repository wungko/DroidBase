package com.droid.base.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.droid.base.adapter.BaseAdapter;
import com.droid.base.entity.Page;

import java.util.List;

/**
 * Created by wungko on 16/8/1.
 */
public abstract class BaseListFragment extends BaseFragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    /**翻页信息**/
    Page mPage = new Page() {
        @Override
        public boolean hasNextPage() {
            return false;
        }

        @Override
        public int pageNext() {
            return 1;
        }

        @Override
        public void reset() {

        }
    };
    /**数据适配**/
    BaseAdapter mAdapter;
    /**ListView 初始化**/
    void initListView(){

        mAdapter = initAdapter();
        if (mAdapter == null) {
            throw new RuntimeException("BaseListActivity please specific listView adapter");
        }

        GridView gridView = getGridView();
        if (gridView != null) {
            gridView.setOnScrollListener(this);

            gridView.setAdapter(mAdapter);

            gridView.setOnItemClickListener(this);
            return;
        }

        ListView listView = getListView();
        if (listView == null) {
            throw new RuntimeException("BaseListFragment please specific listView");
        }
        listView.setOnScrollListener(this);
        mAdapter = initAdapter();
        if (mAdapter == null) {
            throw new RuntimeException("BaseListFragment please specific listView adapter");
        }
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(this);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListView();
    }

    /**
     * 页面是gridView
     * @return
     */
    protected GridView getGridView(){
        return null;
    }

    /**子类 需要实现此方法 返回listView**/
    protected abstract ListView getListView();

    /**子类需要实现此方法 返回adapter 适配器**/
    protected abstract BaseAdapter initAdapter();

    /**子类需要实现此方法 实现加载下一页功能**/
    protected abstract void loadNextPage();

    /**滚动 加载下一页的判断条件**/
    boolean canLoadNext = false;

    /**子类调用方法**/
    protected void addData(List data, Page page) {
        getAdapter().addData(data);
        setPage(page);
        if (getAdapter().getData().isEmpty()) {
            showEmpty();
        }
        canLoadNext = true;
    }

    private void setPage(Page page){
        if (page == null) {
            this.mPage = new Page() {
                @Override
                public boolean hasNextPage() {
                    return false;
                }

                @Override
                public int pageNext() {
                    return 1;
                }

                @Override
                public void reset() {

                }
            };
        }
        this.mPage = page;
    }

    protected Page getPage(){
        return this.mPage;
    }

    /**设置 加载下一页判断条件**/
    protected void setCanLoadNext(boolean b){
        this.canLoadNext = b;
    }

    /**获取 加载下一页判断条件**/
    private boolean getCanLoadNext() {
        return this.canLoadNext;
    }

    protected BaseAdapter getAdapter() {
        return mAdapter;
    }

    int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mTotalItemCount != 0 && mFirstVisibleItem + mVisibleItemCount == mTotalItemCount && scrollState == SCROLL_STATE_IDLE && mPage.hasNextPage() && getCanLoadNext()) {
            loadNextPage();
            setCanLoadNext(false);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.mFirstVisibleItem = firstVisibleItem;
        this.mVisibleItemCount = visibleItemCount;
        this.mTotalItemCount = totalItemCount;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
