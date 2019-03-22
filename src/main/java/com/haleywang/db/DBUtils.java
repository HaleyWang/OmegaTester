package com.haleywang.db;

import com.google.common.base.Splitter;
import com.haleywang.monitor.dao.*;

import org.apache.commons.io.FileUtils;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import com.haleywang.monitor.utils.PathUtils;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.session.Configuration;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by haley on 2018/8/15.
 */
public class DBUtils {

    private static final ThreadLocal<SqlSession> THREADLOCAL_SQLSESSION = new ThreadLocal<>();

    private static volatile SqlSessionFactory sqlSessionFactory;

    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {

            synchronized (DBUtils.class) {
                if (sqlSessionFactory == null) {
                    //it is a bad practice to use multiple connections when connecting to SQLite
                    //change to h2 db


                    String path = PathUtils.getRoot();
                    path = path.replace("test-classes/", "");
                    path = path.replace("classes/", "");
                    path = path.replace("target/", "");
                    System.out.println("------->>>>  " + path);

                    /*
                    String driver = "org.sqlite.JDBC";
                    String url = "jdbc:sqlite:file:{root}/data/dump.sqlite";
                    url = url.replace("{root}", path);
                    String username = null;
                    String password = null;
                    */
                    String driver = "org.h2.Driver";
                    String url = "jdbc:h2:file:{root}/data1/h2db;AUTO_SERVER=TRUE;MODE=MySQL;AUTO_RECONNECT=TRUE;DB_CLOSE_DELAY=-1";
                    url = url.replace("{root}", path);
                    String username = null;
                    String password = null;


                    DataSource dataSource = new PooledDataSource(driver, url, username, password);
                    TransactionFactory transactionFactory = new JdbcTransactionFactory();
                    Environment environment = new Environment("development", transactionFactory, dataSource);
                    Configuration configuration = new Configuration();
                    configuration.setEnvironment(environment);
                    configuration.setMapperHelper(new MapperHelper());

                    mapperRegistry(configuration);

                    sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
                }
            }
        }
        return sqlSessionFactory;

    }

    private static void mapperRegistry(Configuration configuration) {

        Class<?>[] types = getAllMapperClasses();

        for (Class<?> type : types) {
            configuration.addMapper(type);
        }
    }

    static Class<?>[] getAllMapperClasses() {
        return new Class<?>[]{
                BlogMapper.class,
                ReqSettingRepository.class,
                ReqAccountRepository.class,
                ReqBatchHistoryRepository.class,
                ReqBatchRepository.class,
                ReqGroupRepository.class,
                ReqInfoRepository.class,
                ReqMetaRepository.class,
                ReqRelationRepository.class,
                ReqTaskHistoryRepository.class,
                ReqTaskHistoryMetaRepository.class
        };
    }

    public static void doInitSql(SqlSession session) throws IOException {
        Class<?>[] types = DBUtils.getAllMapperClasses();
        for (Class<?> type : types) {
            String sqls = SqlHelper.getMapperSql(session, type, "createTableSql");


            List<String> sqlList = Splitter.on(";").omitEmptyStrings().trimResults().splitToList(sqls);

            for (String sql : sqlList) {
                sql += ";";
                System.out.println(sql);
                Connection con = session.getConnection();

                try {
                    Statement sm = con.createStatement();
                    sm.execute(sql);
                    con.commit();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }



        }


        System.out.println("init_sql >>>>>>>  ");
    }


    public static SqlSession getOrOpenSqlSession() {
        SqlSession session = THREADLOCAL_SQLSESSION.get();
        if (session == null) {
            session = openSession();
            THREADLOCAL_SQLSESSION.set(session);
        }
        return session;
    }

    private static SqlSession openSession() {
        SqlSessionFactory sqlSessionFactory = DBUtils.getSqlSessionFactory();
        return sqlSessionFactory.openSession();
    }

    public static void outputInitSql(SqlSession session) throws IOException {
        Class<?>[] types = getAllMapperClasses();
        StringBuilder sb = new StringBuilder();
        for (Class<?> type : types) {
            String sql = SqlHelper.getMapperSql(session, type, "createTableSql");
            System.out.println(sql);
            sb.append(sql).append("\n\n");
        }

        File file = new File("init_sql.sql");
        FileUtils.write(file, sb.toString());
        System.out.println("init_sql >>>>>>>  " + file.getAbsolutePath());
    }

    public static void commitSession(SqlSession session) {
        session.commit();
    }

    //closeSelectSession
    public static void closeSelectSession(SqlSession session) {
        closeSession(session, null);
    }

    public static void closeSession(SqlSession session) {
        closeSession(session, true);
    }

    public static void closeSession(SqlSession session, Boolean commit) {
        THREADLOCAL_SQLSESSION.remove();
        if (session == null) {
            return;
        }
        try {
            if (commit != null) {
                if (commit) {
                    session.commit();
                } else {
                    session.rollback();
                }
            }
        } finally {
            session.close();
        }
    }

    public static void closeSession(Boolean commit) {
        SqlSession session = THREADLOCAL_SQLSESSION.get();
        THREADLOCAL_SQLSESSION.remove();
        if (session == null) {
            return;
        }
        try {
            if (commit != null) {
                if (commit) {
                    session.commit();
                } else {
                    session.rollback();
                }
            }
        } finally {
            session.close();
        }
    }
}
