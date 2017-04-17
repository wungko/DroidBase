package com.droid.base.entity;

/**
 * Created by wungko on 2016/11/21.
 */

public abstract class Page {
    //return (totalpage > 0 && page < totalpage) || hasMore;
    public abstract boolean hasNextPage();

    public abstract int pageNext();

    public void setPage(Page page) {
        if (page == null) {
            return;
        }
    }


}
