package data.utils;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimeZone;

public class SingletonJDBC{
    private static Connection connection = null;
    private static Connection connectionTesting = null;

    //eseguito sono una volta: quando la classe è caricata in memoria
    static{
        DataSource datasource=null;
        PoolProperties p = new PoolProperties();
        p.setUrl("jdbc:mysql://localhost:3306/spotibase?useSSL=false&serverTimezone=" + TimeZone.getDefault().getID());
        p.setDriverClassName("com.mysql.cj.jdbc.Driver");
        p.setUsername("root");
        p.setPassword("1234");
        //The maximum number of active connections that can be allocated from this pool at the same time.
        p.setMaxActive(1000);
        p.setInitialSize(10);
        p.setMinIdle(100);
        //p.setRemoveAbandonedTimeout(60);
        p.setRemoveAbandoned(false);
        datasource = new DataSource();
        datasource.setPoolProperties(p);

        try {
            connection = datasource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    static{
        DataSource datasource=null;
        PoolProperties p = new PoolProperties();
        p.setUrl("jdbc:mysql://localhost:3306/spotibasetesting?useSSL=false&serverTimezone=" + TimeZone.getDefault().getID());
        p.setDriverClassName("com.mysql.cj.jdbc.Driver");
        p.setUsername("root");
        p.setPassword("1234");
        //The maximum number of active connections that can be allocated from this pool at the same time.
        p.setMaxActive(1000);
        p.setInitialSize(10);
        p.setMinIdle(100);
        //p.setRemoveAbandonedTimeout(60);
        p.setRemoveAbandoned(false);
        datasource = new DataSource();
        datasource.setPoolProperties(p);

        try {
            connectionTesting = datasource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static Connection getConnection(){
        return connection;
    }
    public static Connection getConnectionTesting(){
        return connectionTesting;
    }

}
