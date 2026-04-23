package repository;

public final class DBConfig {
    private DBConfig() {}
    public static final String URL = "jdbc:h2:./janggi;AUTO_SERVER=TRUE;INIT=RUNSCRIPT FROM 'classpath:schema.sql'";
    public static final String USER = "SA";
    public static final String PASSWORD = "";
    public static final int POOL_SIZE = 10;
}
