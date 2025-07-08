package Modelo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class M_ConexionBD {
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        config.setJdbcUrl("jdbc:mysql://localhost:3306/latienditadelbarrio?useUnicode=true&characterEncoding=UTF-8");
        config.setUsername("root");
        config.setPassword("");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);
    }

    private M_ConexionBD() {
    }

    public static Connection getConexion() throws SQLException {
        return ds.getConnection();
    }

    public static void cerrarPool() {
        ds.close();
    }

}
