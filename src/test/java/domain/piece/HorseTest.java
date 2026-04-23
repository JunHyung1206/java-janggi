package domain.piece;

import domain.Offset;
import domain.board.Board;
import domain.board.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

class HorseTest {
    private static final Position FROM = new Position(4, 5);

    private Board boardWith(Piece... pieces) {
        return new Board(List.of(pieces));
    }

    @Test
    void 마는_위로_두칸_왼쪽으로_한칸_움직일_수_있다() {
        Board board = boardWith(new Horse(Team.CHO, FROM));
        assertDoesNotThrow(() -> board.move(FROM, new Offset(-1, 2).applyTo(FROM)));
    }

    @Test
    void 마는_위로_두칸_오른쪽으로_한칸_움직일_수_있다() {
        Board board = boardWith(new Horse(Team.CHO, FROM));
        assertDoesNotThrow(() -> board.move(FROM, new Offset(1, 2).applyTo(FROM)));
    }

    @Test
    void 마는_위로_한칸_왼쪽으로_두칸_움직일_수_있다() {
        Board board = boardWith(new Horse(Team.CHO, FROM));
        assertDoesNotThrow(() -> board.move(FROM, new Offset(-2, 1).applyTo(FROM)));
    }

    @Test
    void 마는_아래로_한칸_왼쪽으로_두칸_움직일_수_있다() {
        Board board = boardWith(new Horse(Team.CHO, FROM));
        assertDoesNotThrow(() -> board.move(FROM, new Offset(-2, -1).applyTo(FROM)));
    }

    @Test
    void 마는_아래로_두칸_왼쪽으로_한칸_움직일_수_있다() {
        Board board = boardWith(new Horse(Team.CHO, FROM));
        assertDoesNotThrow(() -> board.move(FROM, new Offset(-1, -2).applyTo(FROM)));
    }

    @Test
    void 마는_아래로_두칸_오른쪽으로_한칸_움직일_수_있다() {
        Board board = boardWith(new Horse(Team.CHO, FROM));
        assertDoesNotThrow(() -> board.move(FROM, new Offset(1, -2).applyTo(FROM)));
    }

    @Test
    void 마는_위로_한칸_오른쪽으로_두칸_움직일_수_있다() {
        Board board = boardWith(new Horse(Team.CHO, FROM));
        assertDoesNotThrow(() -> board.move(FROM, new Offset(2, 1).applyTo(FROM)));
    }

    @Test
    void 마는_아래로_한칸_오른쪽으로_두칸_움직일_수_있다() {
        Board board = boardWith(new Horse(Team.CHO, FROM));
        assertDoesNotThrow(() -> board.move(FROM, new Offset(2, -1).applyTo(FROM)));
    }

    @Test
    void 경로에_기물이_없으면_이동할_수_있다() {
        Board board = boardWith(
                new Horse(Team.CHO, new Position(4, 0)),
                new Chariot(Team.HAN, new Position(4, 2))
        );
        board.move(new Position(4, 0), new Position(5, 2));

        Piece piece = board.getPiece(new Position(5, 2)).get();
        assertThat(piece.isSameTeam(Team.CHO) && piece.isSameType(PieceType.HORSE)).isTrue();
    }

    @Test
    void 경로에_기물이_있으면_이동할_수_없다() {
        Board board = boardWith(
                new Horse(Team.CHO, new Position(4, 0)),
                new Chariot(Team.HAN, new Position(4, 1))
        );
        assertThrows(IllegalStateException.class, () -> board.move(new Position(4, 0), new Position(5, 2)));
    }

    @Test
    void 다른_팀의_기물이_있다면_잡는다() {
        Board board = boardWith(
                new Horse(Team.CHO, new Position(4, 0)),
                new Chariot(Team.HAN, new Position(2, 1))
        );
        board.move(new Position(4, 0), new Position(2, 1));

        Piece piece = board.getPiece(new Position(2, 1)).get();
        assertThat(piece.isSameTeam(Team.CHO) && piece.isSameType(PieceType.HORSE)).isTrue();
    }
}
