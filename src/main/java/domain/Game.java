package domain;

import domain.board.Board;
import domain.piece.Piece;
import domain.piece.Team;
import exception.ErrorMessage;

import java.util.List;

public class Game {

    private final Board board;
    private Team turn;

    public Game(Board board) {
        this(board, Team.CHO);
    }

    public Game(Board board, Team turn) {
        this.board = board;
        this.turn = turn;
    }

    public void move(Position from, Position to) {
        validateMyPiece(from);
        board.move(from, to);
        nextTurn();
    }

    private void nextTurn() {
        this.turn = turn.opposite();
    }

    public void validateMyPiece(Position from) {
        Piece piece = board.getRequiredPiece(from);
        if (!piece.isSameTeam(turn)) {
            throw new IllegalStateException(ErrorMessage.NOT_MY_PIECE.getMessage());
        }
    }

    public double getCurrentScore(Team team) {
        return board.calculateScore(team);
    }

    public Team getTurn() {
        return turn;
    }

    public boolean isGameEnd() {
        return !(board.isAliveGeneral(Team.CHO) && board.isAliveGeneral(Team.HAN));
    }

    public List<Piece> getPieces() {
        return board.getPieces();
    }

    public Team getWinnerTeam() {
        if (!isGameEnd()) {
            throw new IllegalStateException(ErrorMessage.GAME_NOT_ENDED.getMessage());
        }
        if (board.isAliveGeneral(Team.CHO)) {
            return Team.CHO;
        }
        return Team.HAN;
    }
}
