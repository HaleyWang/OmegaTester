package com.haleywang.monitor.dto;
public class MetaDataResponse {
    private ResponseMeta _meta;
    private Object data;

    public ResponseMeta get_meta() {
        return _meta;
    }

    public void set_meta(ResponseMeta _meta) {
        this._meta = _meta;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
