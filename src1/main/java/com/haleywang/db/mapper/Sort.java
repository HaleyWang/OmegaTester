package com.haleywang.db.mapper;

/**
 * @author haley
 * @date 2018/12/16
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
