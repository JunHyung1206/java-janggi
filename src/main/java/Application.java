import controller.Controller;

import repository.DataSource;
import repository.DriverManagerDataSource;
import repository.GameDao;
import repository.GameRepository;
import repository.PieceDao;
import repository.TransactionManager;
import view.InputView;
import view.OutputView;

import java.sql.SQLException;

import static repository.DBConfig.URL;
import static repository.DBConfig.USER;
import static repository.DBConfig.PASSWORD;

public class Application {
    public static void main(String[] args) throws SQLException {

        InputView inputView = new InputView();
        OutputView outputView = new OutputView();

        DataSource dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        TransactionManager transactionManager = new TransactionManager(dataSource);
        GameRepository gameRepository = new GameRepository(transactionManager, new GameDao(), new PieceDao());

        Controller controller = new Controller(inputView, outputView, gameRepository);
        controller.run();
    }
}
