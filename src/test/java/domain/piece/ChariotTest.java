package domain.piece;

import domain.Offset;
import domain.board.Board;
import domain.board.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChariotTest {
    private static final Position FROM = new Position(4, 5);

    private Board boardWith(Piece... pieces) {
        return new Board(List.of(pieces));
    }

    @Test
    void 차는_왼쪽으로_이동할_수_있다() {
        assertDoesNotThrow(() -> boardWith(new Chariot(Team.CHO, FROM))
                .move(FROM, new Offset(-3, 0).applyTo(FROM)));
    }

    @Test
    void 차는_오른쪽으로_이동할_수_있다() {
        assertDoesNotThrow(() -> boardWith(new Chariot(Team.CHO, FROM))
                .move(FROM, new Offset(3, 0).applyTo(FROM)));
    }

    @Test
    void 차는_위쪽으로_이동할_수_있다() {
        assertDoesNotThrow(() -> boardWith(new Chariot(Team.CHO, FROM))
                .move(FROM, new Offset(0, 1).applyTo(FROM)));
    }

    @Test
    void 차는_아래쪽으로_이동할_수_있다() {
        assertDoesNotThrow(() -> boardWith(new Chariot(Team.CHO, FROM))
                .move(FROM, new Offset(0, -5).applyTo(FROM)));
    }

    @Test
    void 차는_궁성_코너에서_반대쪽_코너로_이동할_수_있다() {
        Position from = new Position(5, 2);
        assertDoesNotThrow(() -> boardWith(new Chariot(Team.CHO, from))
                .move(from, new Offset(-2, -2).applyTo(from)));
    }

    @Test
    void 차는_궁성_코너에서_중앙으로_대각선_이동할_수_있다() {
        Position from = new Position(3, 2);
        assertDoesNotThrow(() -> boardWith(new Chariot(Team.CHO, from))
                .move(from, new Offset(1, -1).applyTo(from)));
    }

    @Test
    void 차는_궁성_중앙에서_코너로_대각선_이동할_수_있다() {
        Position from = new Position(4, 1);
        assertDoesNotThrow(() -> boardWith(new Chariot(Team.CHO, from))
                .move(from, new Offset(1, 1).applyTo(from)));
    }

    @Test
    void 경로에_기물이_없으면_이동할_수_있다() {
        Board board = boardWith(
                new Chariot(Team.CHO, new Position(0, 0)),
                new Chariot(Team.HAN, new Position(5, 0))
        );
        board.move(new Position(0, 0), new Position(4, 0));

        Piece piece = board.getPiece(new Position(4, 0)).get();
        assertThat(piece.isSameTeam(Team.CHO) && piece.isSameType(PieceType.CHARIOT)).isTrue();
    }

    @Test
    void 경로에_기물이_있으면_이동할_수_없다() {
        Board board = boardWith(
                new Chariot(Team.CHO, new Position(0, 0)),
                new Chariot(Team.HAN, new Position(5, 0))
        );
        assertThrows(IllegalStateException.class, () -> board.move(new Position(0, 0), new Position(7, 0)));
    }

    @Test
    void 다른_팀의_기물이_있다면_잡는다() {
        Board board = boardWith(
                new Chariot(Team.CHO, new Position(0, 0)),
                new Chariot(Team.HAN, new Position(5, 0))
        );
        board.move(new Position(0, 0), new Position(5, 0));

        Piece piece = board.getPiece(new Position(5, 0)).get();
        assertThat(piece.isSameTeam(Team.CHO) && piece.isSameType(PieceType.CHARIOT)).isTrue();
    }

    @Test
    void 차가_대각선으로_이동하는지_확인한다() {
        Board board = boardWith(
                new Chariot(Team.CHO, new Position(4, 1)),
                new General(Team.CHO, new Position(3, 1))
        );
        board.move(new Position(4, 1), new Position(5, 2));

        Piece piece = board.getRequiredPiece(new Position(5, 2));
        assertThat(piece.isSameTeam(Team.CHO) && piece.isSameType(PieceType.CHARIOT)).isTrue();
    }
}
