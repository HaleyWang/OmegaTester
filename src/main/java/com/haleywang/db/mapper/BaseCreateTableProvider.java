package com.haleywang.db.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by haley on 2018/8/19.
 */
public class BaseCreateTableProvider  extends MapperTemplate {


    public BaseCreateTableProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 查询
     *
     * @param ms
     * @return
     */
    public String selectOne(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        //修改返回值类型为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.whereAllIfColumns(entityClass, isNotEmpty()));
        return sql.toString();
    }

    /**
     * 查询
     *
     * @param ms
     * @return
     */
    public String select(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        //修改返回值类型为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.whereAllIfColumns(entityClass, isNotEmpty()));
        sql.append(SqlHelper.orderByDefault(entityClass));
        return sql.toString();
    }

    /**
     * 查询
     *
     * @param ms
     * @return
     */
    public String selectByRowBounds(MappedStatement ms) {
        return select(ms);
    }

    /**
     * 根据主键进行查询
     *
     * @param ms
     */
    public String selectByPrimaryKey(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //将返回值修改为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.wherePKColumns(entityClass));
        return sql.toString();
    }

    /**
     * 查询总数
     *
     * @param ms
     * @return
     */
    public String selectCount(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectCount(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.whereAllIfColumns(entityClass, isNotEmpty()));
        return sql.toString();
    }

    /**
     * 根据主键查询总数
     *
     * @param ms
     * @return
     */
    public String existsWithPrimaryKey(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectCountExists(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.wherePKColumns(entityClass));
        return sql.toString();
    }

    /**
     * 查询全部结果
     *
     * @param ms
     * @return
     */
    public String createTableSql(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //修改返回值类型为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();

        Set<EntityColumn> pks = EntityHelper.getPKColumns(entityClass);
        String tName = tableName(entityClass);
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);

        sql.append("create table ")
                .append(tName)
                .append(" (");

        int i = 0;
        int n = pks.size();
        for(EntityColumn pk : pks) {
            sql.append(pk.getColumn()).append(" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE ");
            if(i < n-1) {
                sql.append(',');
            }
            i++;
        }

        sql.append(" );");
        sql.append("\n");


        // alter sql
        Map<String, String> typeMap = new HashMap<String, String>();
        typeMap.put("java.lang.String", "TEXT");
        typeMap.put("java.lang.Long", "INTEGER");
        typeMap.put("java.lang.Date", "DATE");
        for(EntityColumn columnObj : columns) {
            if(columnObj.isId()) {
                continue;
            }
            sql.append("ALTER TABLE ").append(tName).append(" ADD ")
                    .append(columnObj.getColumn())
                    .append(" ")
                    .append(typeMap.getOrDefault(columnObj.getJavaType().getName(), "TEXT"))
                    .append(";")
            .append("\n");

        }


        return sql.toString();
    }
}
