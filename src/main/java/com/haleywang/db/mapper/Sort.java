package com.haleywang.db.mapper;

/**
 * Created by haley on 2018/8/18.
 */
public class Sort {

    String feild;
    boolean asc;

    public static Sort of(String feild) {
        Sort sort = new Sort();
        sort.setAsc(true);
        sort.setFeild(feild);
        return sort;
    }

    public static Sort of(String feild, boolean asc) {
        Sort sort = new Sort();
        sort.setAsc(asc);
        sort.setFeild(feild);
        return sort;
    }

    public String getFeild() {
        return feild;
    }

    public void setFeild(String feild) {
        this.feild = feild;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }
}
