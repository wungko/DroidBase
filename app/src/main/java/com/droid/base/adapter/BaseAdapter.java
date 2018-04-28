package com.droid.base.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droid.base.BaseApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wungko on 16/8/1.
 */
public abstract class BaseAdapter<T, K> extends android.widget.BaseAdapter {
    private List<T> mData = new ArrayList<>();

    public BaseAdapter() {
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    public void addData(List<T> data) {
        if (data == null) {
            return;
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return mData;
    }

    public void addData(int position, List<T> data) {
        if (data == null) {
            return;
        }
        mData.addAll(position, data);
        notifyDataSetChanged();
    }

    public void addData(int position, T t) {
        mData.add(position, t);
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }

    public void reset() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        K k = null;
        if (convertView == null) {
            int viewTypeCount = getViewTypeCount();
            //item 单type
            if (viewTypeCount > 1) {
                int itemViewType = getItemViewType(position);
                int layout = getLayout(itemViewType);
                if (layout == 0) {
                    throw new RuntimeException("请指定布局ID");
                }
                convertView = LayoutInflater.from(BaseApplication.getInstance()).inflate(layout, parent, false);
                k = initHolder(convertView, itemViewType);
                //item 多type
            } else {
                int layout = getLayout();
                if (layout == 0) {
                    throw new RuntimeException("请指定布局ID");
                }
                convertView = LayoutInflater.from(BaseApplication.getInstance()).inflate(layout, parent, false);
                k = initHolder(convertView);
            }
            convertView.setTag(k);
        } else {
            k = (K) convertView.getTag();
        }

        bindData(k, getItem(position), position);
        return convertView;
    }

    protected abstract int getLayout();

    protected int getLayout(int position) {
        return 0;
    }

    protected abstract void bindData(K k, T item);

    protected void bindData(K k, T item, int position) {
    }

    protected abstract K initHolder(View convertView);

    protected K initHolder(View convertView, int itemViewType) {
        return null;
    }


}
