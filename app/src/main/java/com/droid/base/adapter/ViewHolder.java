package com.droid.base.adapter;

/**
 * @author wangke@kanzhun.com
 * @date 2018/6/6
 * 适用于viewHolder
 */
public abstract class ViewHolder<T> {
    public abstract void bindData(T t,int position);
}