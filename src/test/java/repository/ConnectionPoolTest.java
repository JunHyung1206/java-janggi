package repository;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ConnectionPoolTest {

    private static final String TEST_URL = "jdbc:h2:mem:pool_test;DB_CLOSE_DELAY=-1";
    private static final String USER = "SA";
    private static final String PASSWORD = "";

    @Test
    void 풀에서_커넥션을_가져올_수_있다() throws SQLException {
        ConnectionPool pool = new ConnectionPool(TEST_URL, USER, PASSWORD, 3);

        try (Connection conn = pool.getConnection()) {
            assertThat(conn).isNotNull();
            assertThat(conn.isClosed()).isFalse();
        }
    }

    @Test
    void close_후_커넥션이_풀로_반납되어_재사용된다() throws SQLException {
        ConnectionPool pool = new ConnectionPool(TEST_URL, USER, PASSWORD, 1);

        try (Connection conn = pool.getConnection()) {
        }

        assertDoesNotThrow(() -> {
            try (Connection conn = pool.getConnection()) {
            }
        });
    }

    @Test
    void autoCommit이_false인_커넥션은_반납_시_true로_초기화된다() throws SQLException {
        ConnectionPool pool = new ConnectionPool(TEST_URL, USER, PASSWORD, 1);

        try (Connection conn = pool.getConnection()) {
            conn.setAutoCommit(false);
            assertThat(conn.getAutoCommit()).isFalse();
        }

        try (Connection conn = pool.getConnection()) {
            assertThat(conn.getAutoCommit()).isTrue();
        }
    }

    @Test
    void 풀이_고갈되면_타임아웃_후_예외가_발생한다() throws SQLException {
        ConnectionPool pool = new ConnectionPool(TEST_URL, USER, PASSWORD, 1, 1L);
        Connection held = pool.getConnection();

        assertThatThrownBy(pool::getConnection)
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("고갈");

        held.close();
    }

    @Test
    void 고갈된_풀에_커넥션이_반납되면_대기_스레드가_커넥션을_획득한다() throws Exception {
        ConnectionPool pool = new ConnectionPool(TEST_URL, USER, PASSWORD, 1, 5L);
        Connection held = pool.getConnection();

        AtomicBoolean acquired = new AtomicBoolean(false);
        Thread waiter = new Thread(() -> {
            try (Connection conn = pool.getConnection()) {
                acquired.set(true);
            } catch (Exception ignored) {
            }
        });

        waiter.start();
        Thread.sleep(300);
        held.close();
        waiter.join(3000);

        assertThat(acquired.get()).isTrue();
    }

    @Test
    void 동시에_여러_스레드가_접근해도_활성_커넥션이_풀_크기를_초과하지_않는다() throws InterruptedException {
        int poolSize = 3;
        int threadCount = 10;
        ConnectionPool pool = new ConnectionPool(TEST_URL, USER, PASSWORD, poolSize, 10L);

        AtomicInteger concurrent = new AtomicInteger(0);
        AtomicInteger maxConcurrent = new AtomicInteger(0);
        CountDownLatch done = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try (Connection conn = pool.getConnection()) {
                    int c = concurrent.incrementAndGet();
                    maxConcurrent.updateAndGet(prev -> Math.max(prev, c));
                    Thread.sleep(50);
                    concurrent.decrementAndGet();
                } catch (Exception ignored) {
                } finally {
                    done.countDown();
                }
            });
        }

        done.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(maxConcurrent.get()).isLessThanOrEqualTo(poolSize);
    }

    @Test
    void 초기화_이후_물리_커넥션을_새로_생성하지_않는다() throws SQLException {
        int poolSize = 3;
        AtomicInteger creationCount = new AtomicInteger(0);

        ConnectionPool pool = new ConnectionPool(TEST_URL, USER, PASSWORD, poolSize, 30L) {
            @Override
            Connection createPhysicalConnection() {
                creationCount.incrementAndGet();
                return super.createPhysicalConnection();
            }
        };

        assertThat(creationCount.get()).isEqualTo(poolSize);

        for (int i = 0; i < 20; i++) {
            try (Connection conn = pool.getConnection()) {
            }
        }

        assertThat(creationCount.get()).isEqualTo(poolSize);
    }

    @Test
    void 커넥션_재사용이_매번_생성보다_빠르다() throws SQLException {
        int iterations = 100000;
        ConnectionPool pool = new ConnectionPool(TEST_URL, USER, PASSWORD, 5);
        DriverManagerDataSource driverManager = new DriverManagerDataSource(TEST_URL, USER, PASSWORD);

        // warm-up
        for (int i = 0; i < 10; i++) {
            try (Connection conn = pool.getConnection()) {
            }
            try (Connection conn = driverManager.getConnection()) {
            }
        }

        long poolStart = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            try (Connection conn = pool.getConnection()) {
            }
        }
        long poolTime = System.currentTimeMillis() - poolStart;

        long dmStart = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            try (Connection conn = driverManager.getConnection()) {
            }
        }
        long dmTime = System.currentTimeMillis() - dmStart;

        System.out.println("커넥션 풀 : " + (double)poolTime / 1000 + "초");
        System.out.println("직접 생성 : " + (double)dmTime / 1000 + "초");

        assertThat(poolTime).isLessThan(dmTime);
    }
}
