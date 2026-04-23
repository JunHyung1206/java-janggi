package repository;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    private final DBConnection dbConnection;

    public TransactionManager(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public <T> T query(TransactionWork<T> work) {
        try (Connection connection = dbConnection.getConnection()) {
            return work.execute(connection);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(VoidTransactionWork work) {
        execute(connection -> {
            work.execute(connection);
            return null;
        });
    }

    public <T> T execute(TransactionWork<T> work) {
        try (Connection connection = dbConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                T result = work.execute(connection);
                connection.commit();
                return result;
            } catch (Exception e) {
                rollbackQuietly(connection);
                throw toRuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void rollbackQuietly(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException ignored) {
        }
    }

    private RuntimeException toRuntimeException(Exception e) {
        if (e instanceof RuntimeException re) {
            return re;
        }
        return new RuntimeException(e);
    }
}
