package domain.piece;

import domain.board.Board;
import domain.board.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CannonTest {

    private Board boardWith(Piece... pieces) {
        return new Board(List.of(pieces));
    }

    @Test
    void 포는_직선으로_이동할_수_있다() {
        Board board = boardWith(
                new Cannon(Team.CHO, new Position(0, 0)),
                new Horse(Team.HAN, new Position(3, 0))
        );
        assertDoesNotThrow(() -> board.move(new Position(0, 0), new Position(5, 0)));
    }

    @Test
    void 포는_궁성에서_대각선으로_이동할_수_있다() {
        Board board = boardWith(
                new Cannon(Team.CHO, new Position(5, 2)),
                new General(Team.CHO, new Position(4, 1))
        );
        board.move(new Position(5, 2), new Position(3, 0));

        Piece piece = board.getRequiredPiece(new Position(3, 0));
        assertThat(piece.isSameTeam(Team.CHO) && piece.isSameType(PieceType.CANNON)).isTrue();
    }

    @Test
    void 포는_사이에_기물이_하나_있으면_이동할_수_있다() {
        Board board = boardWith(
                new Cannon(Team.CHO, new Position(0, 0)),
                new Horse(Team.HAN, new Position(3, 0)),
                new Chariot(Team.HAN, new Position(6, 0))
        );
        assertDoesNotThrow(() -> board.move(new Position(0, 0), new Position(6, 0)));
    }

    @Test
    void 포는_사이에_기물이_없으면_이동할_수_없다() {
        Board board = boardWith(new Cannon(Team.CHO, new Position(0, 0)));
        assertThrows(IllegalStateException.class, () -> board.move(new Position(0, 0), new Position(5, 0)));
    }

    @Test
    void 포는_사이에_기물이_두개_이상이면_이동할_수_없다() {
        Board board = boardWith(
                new Cannon(Team.CHO, new Position(0, 0)),
                new Horse(Team.HAN, new Position(2, 0)),
                new Horse(Team.HAN, new Position(4, 0))
        );
        assertThrows(IllegalStateException.class, () -> board.move(new Position(0, 0), new Position(6, 0)));
    }

    @Test
    void 포는_사이에_포가_있으면_이동할_수_없다() {
        Board board = boardWith(
                new Cannon(Team.CHO, new Position(0, 0)),
                new Cannon(Team.HAN, new Position(3, 0))
        );
        assertThrows(IllegalStateException.class, () -> board.move(new Position(0, 0), new Position(5, 0)));
    }

    @Test
    void 포는_목적지에_포가_있으면_잡을_수_없다() {
        Board board = boardWith(
                new Cannon(Team.CHO, new Position(5, 1)),
                new Horse(Team.CHO, new Position(5, 3)),
                new Cannon(Team.HAN, new Position(5, 7))
        );
        assertThrows(IllegalStateException.class, () -> board.move(new Position(5, 1), new Position(5, 7)));
    }
}
