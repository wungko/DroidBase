package com.droid.base.entity;

/**
 * Created by wungko on 2016/11/21.
 */

public class Page {
    public Page(){
    }
    public int nowpage;
    public int totalpage;

    public void reset(){
        nowpage = 0;
        totalpage = 0;
    }

    public boolean hasNextPage(){
        return totalpage > 0 && nowpage < totalpage;
    }

    public void setPage(Page page) {
        if (page == null) {
            return;
        }
        this.totalpage = page.totalpage;
        this.nowpage = page.nowpage;
    }
}
