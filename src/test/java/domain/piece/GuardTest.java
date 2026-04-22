package domain.piece;

import domain.Offset;
import domain.board.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GuardTest {
    private static final Position CENTER = new Position(4, 8);
    private Piece guard;

    @BeforeEach
    void setUp() {
        guard = new Guard(Team.HAN, CENTER);
    }

    @Test
    void 사는_위로_한칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(0, 1).applyTo(CENTER);
        List<Offset> pathPositions = guard.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 사는_아래로_한칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(0, -1).applyTo(CENTER);
        List<Offset> pathPositions = guard.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 사는_좌로_한칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(-1, 0).applyTo(CENTER);
        List<Offset> pathPositions = guard.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 사는_우로_한칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(1, 0).applyTo(CENTER);
        List<Offset> pathPositions = guard.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 사는_두칸을_이동할_수_없다() {
        Position from = new Position(3, 8);
        Position to = new Offset(2, 0).applyTo(from);
        assertThrows(IllegalStateException.class,
                () -> new Guard(Team.HAN, from).getPathOffset(to));
    }

    @Test
    void 사는_궁성_중앙에서_대각선으로_이동할_수_있다() {
        Position to = new Offset(1, 1).applyTo(CENTER);
        List<Offset> pathPositions = guard.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 사는_궁성_코너에서_오른쪽_위로_이동할_수_있다() {
        Position from = new Position(3, 7);
        Position to = new Offset(1, 1).applyTo(from);
        List<Offset> pathPositions = new Guard(Team.HAN, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 사는_궁성_코너에서_오른쪽_아래로_이동할_수_있다() {
        Position from = new Position(3, 9);
        Position to = new Offset(1, -1).applyTo(from);
        List<Offset> pathPositions = new Guard(Team.HAN, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 사는_궁성_코너에서_왼쪽_위로_이동할_수_있다() {
        Position from = new Position(5, 7);
        Position to = new Offset(-1, 1).applyTo(from);
        List<Offset> pathPositions = new Guard(Team.HAN, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 사는_궁성_코너에서_왼쪽_아래로_이동할_수_있다() {
        Position from = new Position(5, 9);
        Position to = new Offset(-1, -1).applyTo(from);
        List<Offset> pathPositions = new Guard(Team.HAN, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 사는_궁성_밖으로_이동할_수_없다() {
        Position from = new Position(3, 8);
        Position to = new Offset(-1, 0).applyTo(from);
        assertThrows(IllegalStateException.class,
                () -> new Guard(Team.HAN, from).getPathOffset(to));
    }
}
