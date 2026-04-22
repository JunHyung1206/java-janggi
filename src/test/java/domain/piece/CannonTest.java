package domain.piece;

import domain.Offset;
import domain.board.Board;
import domain.board.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CannonTest {

    private static final Position FROM = new Position(4, 5);
    private Piece cannon;

    @BeforeEach
    void setUp() {
        cannon = new Cannon(Team.CHO, FROM);
    }

    @Test
    void 포는_왼쪽_직선으로_가는_경로가_있다() {
        Position to = new Offset(-3, 0).applyTo(FROM);
        List<Offset> pathPositions = cannon.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of(new Offset(-1, 0), new Offset(-2, 0)));
    }

    @Test
    void 포는_오른쪽_직선으로_가는_경로가_있다() {
        Position to = new Offset(3, 0).applyTo(FROM);
        List<Offset> pathPositions = cannon.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of(new Offset(1, 0), new Offset(2, 0)));
    }

    @Test
    void 포는_위쪽_직선으로_가는_경로가_있다() {
        Position from = new Position(4, 1);
        Position to = new Offset(0, 5).applyTo(from);
        List<Offset> pathPositions = new Cannon(Team.CHO, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of(
                new Offset(0, 1), new Offset(0, 2),
                new Offset(0, 3), new Offset(0, 4)));
    }

    @Test
    void 포는_아래쪽_직선으로_가는_경로가_있다() {
        Position from = new Position(4, 8);
        Position to = new Offset(0, -6).applyTo(from);
        List<Offset> pathPositions = new Cannon(Team.CHO, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of(
                new Offset(0, -1), new Offset(0, -2),
                new Offset(0, -3), new Offset(0, -4), new Offset(0, -5)));
    }

    @Test
    void 포는_기물이_사이에_하나의_기물이_있으면_정상적으로_움직일_수_있다() {
        List<Piece> blockedPieces = List.of(new Horse(Team.HAN, new Position(0, 0)));
        assertDoesNotThrow(() -> cannon.validateMove(blockedPieces));
    }

    @Test
    void 포는_기물이_사이에_하나라도_존재하지_않으면_예외를_반환한다() {
        assertThrows(IllegalStateException.class, () -> cannon.validateMove(List.of()));
    }

    @Test
    void 포는_기물이_사이에_두개_이상_존재하면_예외를_반환한다() {
        List<Piece> blockedPieces = List.of(
                new Horse(Team.HAN, new Position(0, 0)),
                new Horse(Team.HAN, new Position(1, 0)));
        assertThrows(IllegalStateException.class, () -> cannon.validateMove(blockedPieces));
    }

    @Test
    void 포는_기물이_사이에_포가_존재하면_예외를_반환한다() {
        List<Piece> blockedPieces = List.of(new Cannon(Team.HAN, new Position(0, 0)));
        assertThrows(IllegalStateException.class, () -> cannon.validateMove(blockedPieces));
    }

    @Test
    void 포는_목적지에_포가_존재하면_예외를_반환한다() {
        Board board = new Board(List.of(
                new Cannon(Team.CHO, new Position(5, 1)),
                new Horse(Team.CHO, new Position(5, 3)),
                new Cannon(Team.HAN, new Position(5, 7))
        ));

        assertThrows(IllegalStateException.class,
                () -> board.move(new Position(5, 1), new Position(5, 7)));
    }

    @Test
    void 포는_궁성에서_왼쪽_아래_대각선으로_이동할_수_있다() {
        Position from = new Position(5, 2);
        Position to = new Offset(-2, -2).applyTo(from);
        List<Offset> pathPositions = new Cannon(Team.CHO, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of(new Offset(-1, -1)));
    }

    @Test
    void 포는_궁성에서_오른쪽_아래_대각선으로_이동할_수_있다() {
        Position from = new Position(3, 2);
        Position to = new Offset(2, -2).applyTo(from);
        List<Offset> pathPositions = new Cannon(Team.CHO, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of(new Offset(1, -1)));
    }

    @Test
    void 포는_궁성에서_왼쪽_위_대각선으로_이동할_수_있다() {
        Position from = new Position(5, 0);
        Position to = new Offset(-2, 2).applyTo(from);
        List<Offset> pathPositions = new Cannon(Team.CHO, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of(new Offset(-1, 1)));
    }

    @Test
    void 포는_궁성에서_오른쪽_위_대각선으로_이동할_수_있다() {
        Position from = new Position(5, 2);
        Position to = new Offset(-2, -2).applyTo(from);
        List<Offset> pathPositions = new Cannon(Team.CHO, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of(new Offset(-1, -1)));
    }

    @Test
    void 포가_대각선으로_이동하는지_확인한다() {
        Board board = new Board(List.of(
                new Cannon(Team.CHO, new Position(5, 2)),
                new General(Team.CHO, new Position(4, 1))
        ));

        board.move(new Position(5, 2), new Position(3, 0));
        Piece piece = board.getRequiredPiece(new Position(3, 0));

        boolean result = piece.isSameTeam(Team.CHO) && piece.isSameType(PieceType.CANNON);
        assertThat(result).isTrue();
    }
}
