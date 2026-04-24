package repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConnectionPool implements DataSource {
    private static final long DEFAULT_TIMEOUT_SECONDS = 30;

    private final BlockingQueue<Connection> pool;

    private final String jdbcUrl;
    private final String username;
    private final String password;

    private final long timeoutSeconds;

    public ConnectionPool(String jdbcUrl, String username, String password, int poolSize) {
        this(jdbcUrl, username, password, poolSize, DEFAULT_TIMEOUT_SECONDS);
    }

    ConnectionPool(String jdbcUrl, String username, String password, int poolSize, long timeoutSeconds) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.timeoutSeconds = timeoutSeconds;
        this.pool = new ArrayBlockingQueue<>(poolSize);
        initializePool(poolSize);
    }

    private void initializePool(int size) {
        for (int i = 0; i < size; i++) {
            pool.offer(createPhysicalConnection());
        }
    }

    Connection createPhysicalConnection() {
        try {
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 연결에 실패했습니다.", e);
        }
    }

    @Override
    public Connection getConnection() {
        try {
            Connection physical = pool.poll(timeoutSeconds, TimeUnit.SECONDS);
            if (physical == null) {
                throw new RuntimeException("커넥션 풀이 고갈되었습니다. (" + timeoutSeconds + "초 대기 초과)");
            }
            return wrapWithPoolReturn(physical);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("커넥션 획득 중 인터럽트가 발생했습니다.", e);
        }
    }

    private Connection wrapWithPoolReturn(Connection physical) {
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> {
                    if ("close".equals(method.getName())) {
                        returnToPool(physical);
                        return null;
                    }
                    try {
                        return method.invoke(physical, args);
                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
                }
        );
    }

    private void returnToPool(Connection connection) {
        try {
            if (!connection.getAutoCommit()) {
                connection.rollback();
                connection.setAutoCommit(true);
            }
            pool.offer(connection);
        } catch (SQLException e) {
            pool.offer(createPhysicalConnection());
        }
    }
}
