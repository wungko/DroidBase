package com.droid.base.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.droid.base.adapter.BaseAdapter;
import com.droid.base.entity.Page;

import java.util.List;

/**
 * Created by wungko on 16/1/14.
 */
public abstract class BaseListActivity extends BaseActivity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    /**翻页信息**/
    Page mPage = new Page();
    /**数据适配**/
    BaseAdapter mAdapter;
    /**ListView 初始化**/
    void initListView() {
        ListView listView = getListView();
        if (listView == null) {
            throw new RuntimeException("BaseListActivity please specific listView");
        }
        listView.setOnScrollListener(this);
        mAdapter = initAdapter();
        if (mAdapter == null) {
            throw new RuntimeException("BaseListActivity please specific listView adapter");
        }
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListView();
    }

    /**子类 需要实现此方法 返回listView**/
    protected abstract ListView getListView();

    /**子类需要实现此方法 返回adapter 适配器**/
    protected abstract BaseAdapter initAdapter();

    /**子类需要实现此方法 实现加载下一页功能**/
    protected abstract void loadNextPage();

    /**子类调用方法**/
    protected void addData(List data,Page page) {
        getAdapter().addData(data);
        this.mPage.setPage(page);
        if (getAdapter().getData().isEmpty()) {
            showEmpty();
        }
        setCanLoadNext(true);
    }

    /**设置 加载下一页判断条件**/
    protected void setCanLoadNext(boolean b){
        this.canLoadNext = b;
    }

    /**获取 加载下一页判断条件**/
    private boolean canLoadNext(){
        return canLoadNext;
    }

    protected BaseAdapter getAdapter() {
        return mAdapter;
    }

    /**滚动 加载下一页的判断条件**/
    boolean canLoadNext = false;
    private int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mTotalItemCount != 0 && mFirstVisibleItem + mVisibleItemCount == mTotalItemCount && scrollState == SCROLL_STATE_IDLE && mPage.hasNextPage() && canLoadNext()) {
            setCanLoadNext(false);
            loadNextPage();
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

    /**
     * 获取当前页数
     * @return
     */
    protected int pageCurrent(){
        if (mPage == null) {
            return 1;
        }
        return mPage.nowpage+1;
    }

    /**
     * 获取当前页数
     * @return
     */
    protected int pageNext(){
        if (mPage == null) {
            return 1;
        }
        return mPage.nowpage +1;
    }

    /**
     * 重置页数 默认为1 总页数为0
     */
    protected void pageReset(){
        if (mPage != null) {
            mPage.reset();
        }
    }
}
