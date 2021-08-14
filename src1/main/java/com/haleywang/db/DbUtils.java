package com.haleywang.db;

import com.google.common.base.Splitter;
import com.haleywang.monitor.dao.ReqAccountRepository;
import com.haleywang.monitor.dao.ReqBatchHistoryRepository;
import com.haleywang.monitor.dao.ReqBatchRepository;
import com.haleywang.monitor.dao.ReqGroupRepository;
import com.haleywang.monitor.dao.ReqInfoRepository;
import com.haleywang.monitor.dao.ReqMetaRepository;
import com.haleywang.monitor.dao.ReqRelationRepository;
import com.haleywang.monitor.dao.ReqSettingRepository;
import com.haleywang.monitor.dao.ReqTaskHistoryMetaRepository;
import com.haleywang.monitor.dao.ReqTaskHistoryRepository;
import com.haleywang.monitor.utils.PathUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.session.Configuration;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author haley
 * Created by haley on 2018/8/15.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DbUtils {

    private static final ThreadLocal<SqlSession> THREADLOCAL_SQLSESSION = new ThreadLocal<>();

    private static volatile SqlSessionFactory sqlSessionFactory;

    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {

            synchronized (DbUtils.class) {
                if (sqlSessionFactory == null) {
                    //it is a bad practice to use multiple connections when connecting to SQLite
                    //change to h2 db

                    String path = PathUtils.getRoot();
                    path = path.replace("test-classes/", "");
                    path = path.replace("classes/", "");
                    path = path.replace("target/", "");
                    log.info("path------->>>> {}", path);

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

    public static void doInitSql(SqlSession session, boolean dropTableBefore) throws SQLException {
        Class<?>[] types = DbUtils.getAllMapperClasses();
        for (Class<?> type : types) {
            List<String> sqlList = new ArrayList<>();

            if(dropTableBefore) {
                String dropTableSqls = SqlHelper.getMapperSql(session, type, "dropTableSql");
                List<String> dropTableSqlList = Splitter.on(";").omitEmptyStrings().trimResults().splitToList(dropTableSqls);
                sqlList.addAll(dropTableSqlList);
            }
            String sqls = SqlHelper.getMapperSql(session, type, "createTableSql");
            List<String> createSqlList = Splitter.on(";").omitEmptyStrings().trimResults().splitToList(sqls);
            sqlList.addAll(createSqlList);

            for (String sql : sqlList) {
                sql += ";";
                log.info("doInitSql: {}", sql);
                Connection con = session.getConnection();
                try (Statement sm = con.createStatement()) {
                    try {
                        sm.execute(sql);
                    } catch (SQLException e) {
                        if (!e.getMessage().contains("Duplicate column name")) {
                            throw e;
                        } else {
                            log.warn("doInitSql warn: {}", e.getMessage());
                        }
                    }
                    con.commit();
                }

            }
        }
        log.info("init_sql done >>>>>>>  ");
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
        SqlSessionFactory sqlSessionFactory = DbUtils.getSqlSessionFactory();
        return sqlSessionFactory.openSession();
    }

    public static void outputInitSql(SqlSession session) throws IOException {
        Class<?>[] types = getAllMapperClasses();
        StringBuilder sb = new StringBuilder();
        for (Class<?> type : types) {
            String sql = SqlHelper.getMapperSql(session, type, "createTableSql");
            log.info("outputInitSql: {}", sql);
            sb.append(sql).append("\n\n");
        }

        File file = new File("init_sql.sql");
        FileUtils.write(file, sb.toString());
        log.info("outputInitSql path >>>>>>> {}", file.getAbsolutePath());
    }

    public static void commitSession(SqlSession session) {
        session.commit();
    }

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
                if (BooleanUtils.isTrue(commit)) {
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
                if (BooleanUtils.isTrue(commit)) {
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
