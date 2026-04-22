package domain.piece;

import domain.Offset;
import domain.board.Board;
import domain.board.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ElephantTest {
    private static final Position FROM = new Position(4, 5);
    private Piece elephant;

    @BeforeEach
    void setUp() {
        elephant = new Elephant(Team.CHO, FROM);
    }

    @Test
    void 상은_위로_세칸_왼쪽으로_두칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(-2, 3).applyTo(FROM);
        List<Offset> pathPositions = elephant.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of(new Offset(0, 1), new Offset(-1, 2)));
    }

    @Test
    void 상은_위로_세칸_오른쪽으로_두칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(2, 3).applyTo(FROM);
        List<Offset> pathPositions = elephant.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of(new Offset(0, 1), new Offset(1, 2)));
    }

    @Test
    void 상은_위로_두칸_왼쪽으로_세칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(-3, 2).applyTo(FROM);
        List<Offset> pathPositions = elephant.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of(new Offset(-1, 0), new Offset(-2, 1)));
    }

    @Test
    void 상은_아래로_두칸_왼쪽으로_세칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(-3, -2).applyTo(FROM);
        List<Offset> pathPositions = elephant.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of(new Offset(-1, 0), new Offset(-2, -1)));
    }

    @Test
    void 상은_아래로_세칸_왼쪽으로_두칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(-2, -3).applyTo(FROM);
        List<Offset> pathPositions = elephant.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of(new Offset(0, -1), new Offset(-1, -2)));
    }

    @Test
    void 상은_아래로_세칸_오른쪽으로_두칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(2, -3).applyTo(FROM);
        List<Offset> pathPositions = elephant.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of(new Offset(0, -1), new Offset(1, -2)));
    }

    @Test
    void 상은_위로_두칸_오른쪽으로_세칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(3, 2).applyTo(FROM);
        List<Offset> pathPositions = elephant.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of(new Offset(1, 0), new Offset(2, 1)));
    }

    @Test
    void 상은_아래로_두칸_오른쪽으로_세칸_움직일_수_있는_경로가_있다() {
        Position from = new Position(3, 5);
        Position to = new Offset(3, -2).applyTo(from);
        List<Offset> pathPositions = new Elephant(Team.CHO, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of(new Offset(1, 0), new Offset(2, -1)));
    }

    @Test
    void 기물이_이동할_경로에_대해_다른_기물이_없으면_상은_이동할_수_있다() {
        Board board = new Board(List.of(
                new Elephant(Team.CHO, new Position(4, 0)),
                new Chariot(Team.HAN, new Position(4, 2))
        ));

        board.move(new Position(4, 0), new Position(6, 3));

        Piece piece = board.getPiece(new Position(6, 3)).get();
        boolean result = piece.isSameTeam(Team.CHO) && piece.isSameType(PieceType.ELEPHANT);
        assertThat(result).isTrue();
    }

    @Test
    void 기물이_이동할_경로에_대해_다른_기물이_있으면_상은_이동할_수_없다() {
        Board board = new Board(List.of(
                new Elephant(Team.CHO, new Position(4, 0)),
                new Chariot(Team.HAN, new Position(4, 1))
        ));

        assertThrows(IllegalStateException.class, () -> board.move(new Position(4, 0), new Position(6, 3)));
    }

    @Test
    void 기물이_이동할_경로에_대해_다른_팀의_기물이_있다면_잡는다() {
        Board board = new Board(List.of(
                new Elephant(Team.CHO, new Position(4, 0)),
                new Chariot(Team.HAN, new Position(2, 3))
        ));

        board.move(new Position(4, 0), new Position(2, 3));

        Piece piece = board.getPiece(new Position(2, 3)).get();
        boolean result = piece.isSameTeam(Team.CHO) && piece.isSameType(PieceType.ELEPHANT);
        assertThat(result).isTrue();
    }
}
