import controller.Controller;

import repository.ConnectionPool;
import repository.DataSource;

import repository.GameDao;
import repository.GameRepository;
import repository.PieceDao;
import repository.TransactionManager;
import view.InputView;
import view.OutputView;

import static repository.DBConfig.URL;
import static repository.DBConfig.USER;
import static repository.DBConfig.PASSWORD;
import static repository.DBConfig.POOL_SIZE;

public class Application {
    public static void main(String[] args) {

        InputView inputView = new InputView();
        OutputView outputView = new OutputView();

        DataSource dataSource = new ConnectionPool(URL, USER, PASSWORD, POOL_SIZE);
        TransactionManager transactionManager = new TransactionManager(dataSource);
        GameRepository gameRepository = new GameRepository(transactionManager, new GameDao(), new PieceDao());

        Controller controller = new Controller(inputView, outputView, gameRepository);
        controller.run();
    }
}
