package repository;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface VoidTransactionWork {
    void execute(Connection connection) throws SQLException;
}
