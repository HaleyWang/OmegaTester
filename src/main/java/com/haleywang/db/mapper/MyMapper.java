package com.haleywang.db.mapper;

import tk.mybatis.mapper.common.Mapper;

/**
 * 通用Mapper接口,其他接口继承该接口即可
 * <p/>
 * <p>这是一个例子，自己扩展时可以参考</p>
 * <p/>
 * <p>项目地址 : <a href="https://github.com/abel533/Mapper" target="_blank">https://github.com/abel533/Mapper</a></p>
 *
 * @param <T> 不能为空
 * @author liuzh
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface MyMapper<T> extends Mapper<T>, BaseCreateTableMapper<T> {

}