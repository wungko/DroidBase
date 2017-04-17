package com.droid.base.entity;

/**
 * Created by wungko on 2016/11/21.
 */

public class Page {
    public Page(){
    }

    public Page(Boolean hasMore){
        this.hasMore = hasMore;
    }
    public int page;
    public int totalpage;
    public boolean hasMore;

    public void reset(){
        page = 0;
        totalpage = 0;
    }

    public boolean hasNextPage(){
        return (totalpage > 0 && page < totalpage) || hasMore;
    }

    public void setPage(Page page) {
        if (page == null) {
            return;
        }
        this.totalpage = page.totalpage;
        this.page = page.page;
    }
}
