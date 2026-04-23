package repository;

import domain.Game;
import domain.board.Board;
import domain.board.BoardFactory;
import domain.Position;
import domain.piece.Piece;
import domain.piece.PieceType;
import domain.piece.Team;

import exception.ErrorMessage;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameRepository {
    private final TransactionManager transactionManager;
    private final GameDao gameDao;
    private final PieceDao pieceDao;

    public GameRepository(TransactionManager transactionManager, GameDao gameDao, PieceDao pieceDao) {
        this.transactionManager = transactionManager;
        this.gameDao = gameDao;
        this.pieceDao = pieceDao;
    }

    public long create(Game game) {
        return transactionManager.execute(connection -> {
            long gameId = gameDao.create(connection, game.getTurn().name());
            pieceDao.saveAll(connection, gameId, toPieceRows(game.getPieces()));
            return gameId;
        });
    }

    private List<PieceRow> toPieceRows(List<Piece> pieces) {
        return pieces.stream()
                .map(p -> new PieceRow(
                        p.getPosition().x(),
                        p.getPosition().y(),
                        p.getPieceType().name(),
                        p.getTeam().name()
                ))
                .toList();
    }

    private List<Piece> toPieces(List<PieceRow> rows) {
        return rows.stream()
                .map(r -> BoardFactory.createPiece(
                        PieceType.valueOf(r.pieceType()),
                        Team.valueOf(r.team()),
                        new Position(r.x(), r.y())
                ))
                .toList();
    }

    public void movePiece(long gameId, Game game, Position from, Position to) {
        transactionManager.execute(connection -> {
            Optional<Long> capturedId = pieceDao.findIdByPosition(connection, gameId, to.x(), to.y());
            if (capturedId.isPresent()) {
                pieceDao.deleteById(connection, capturedId.get());
            }

            long movedId = pieceDao.findIdByPosition(connection, gameId, from.x(), from.y())
                    .orElseThrow(() -> new IllegalStateException(ErrorMessage.PIECE_NOT_FOUND.getMessage()));

            pieceDao.updatePosition(connection, movedId, to.x(), to.y());
            gameDao.updateTurn(connection, gameId, game.getTurn().name());
        });
    }

    public Game findById(long gameId) {
        return transactionManager.query(connection -> {
            GameRow gameRow = gameDao.findById(connection, gameId);
            Team turn = Team.valueOf(gameRow.turn());
            List<Piece> pieces = toPieces(pieceDao.findByGameId(connection, gameId));
            return new Game(new Board(pieces), turn);
        });
    }

    public Map<Long, String> findAll() {
        return transactionManager.query(connection -> {
            Map<Long, String> result = new LinkedHashMap<>();
            for (GameRow row : gameDao.findAll(connection)) {
                result.put(row.gameId(), row.turn());
            }
            return result;
        });
    }
}
