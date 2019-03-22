package com.haleywang.db;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.List;

/**
 * Created by haley on 2018/12/7.
 */
public class DBUtilsTest {
    @Test
    public void demo() throws Exception {

    }


    public static void main(String[] args) throws InterruptedException, IOException {

        SqlSession session = DBUtils.getOrOpenSqlSession();
        try {
            outputInitSql();
            //demoBlog(session);
            //doInitSql(session);


            DBUtils.commitSession(session);

        } finally {
            DBUtils.closeSession(session);
        }
    }




    public static void outputInitSql() throws IOException {

        SqlSession session = DBUtils.getOrOpenSqlSession();
        try {
            DBUtils.outputInitSql(session);
            DBUtils.commitSession(session);
        } finally {
            DBUtils.closeSession(session);
        }
    }

    public static void demoBlog(SqlSession session) {
        BlogMapper mapper = session.getMapper(BlogMapper.class);


        Blog blog = mapper.selectByPrimaryKey(2);
        System.out.println(blog);

        mapper.insert(new Blog("aa"));
        List<Blog> list = mapper.selectAll();

        System.out.println("size: " + list.size());
        System.out.println(list.get(0).name);


        Example example = new Example(Blog.class);
        //example.setForUpdate(true);
        example.setOrderByClause(" id desc ");
        example.createCriteria().andGreaterThan("id", 5).andLessThan("id", 500);
        example.or().andLessThan("id", 2);

        Example example1 = new Example(Blog.class);
        example1.setOrderByClause(" id desc ");
        List<Blog> ll = mapper.selectByExampleAndRowBounds(example1, new RowBounds(6, 5));
        System.out.println(ll.size());
    }
}