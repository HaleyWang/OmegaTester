package com.haleywang.db.mapper;

/**
 * Created by haley on 2018/8/18.
 */
public class Sort {

    String field;
    boolean asc;

    public static Sort of(String feild) {
        Sort sort = new Sort();
        sort.setAsc(true);
        sort.setField(feild);
        return sort;
    }

    public static Sort of(String feild, boolean asc) {
        Sort sort = new Sort();
        sort.setAsc(asc);
        sort.setField(feild);
        return sort;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }
}
