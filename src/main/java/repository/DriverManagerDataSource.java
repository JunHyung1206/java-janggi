package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverManagerDataSource implements DataSource {

    private final String jdbcUrl;
    private final String username;
    private final String password;

    public DriverManagerDataSource(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 연결에 실패했습니다: " + e.getMessage(), e);
        }
    }
}
