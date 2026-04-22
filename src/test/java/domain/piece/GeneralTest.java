package domain.piece;

import domain.Offset;
import domain.board.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GeneralTest {
    private static final Position CENTER = new Position(4, 1);
    private Piece general;

    @BeforeEach
    void setUp() {
        general = new General(Team.CHO, CENTER);
    }

    @Test
    void 궁은_위로_한칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(0, 1).applyTo(CENTER);
        List<Offset> pathPositions = general.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 궁은_아래로_한칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(0, -1).applyTo(CENTER);
        List<Offset> pathPositions = general.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 궁은_좌로_한칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(-1, 0).applyTo(CENTER);
        List<Offset> pathPositions = general.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 궁은_우로_한칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(1, 0).applyTo(CENTER);
        List<Offset> pathPositions = general.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 궁은_두칸을_이동할_수_없다() {
        Position from = new Position(3, 1);
        Position to = new Offset(2, 0).applyTo(from);
        assertThrows(IllegalStateException.class,
                () -> new General(Team.CHO, from).getPathOffset(to));
    }

    @Test
    void 궁은_궁성_중앙에서_대각선으로_이동할_수_있다() {
        Position to = new Offset(1, 1).applyTo(CENTER);
        List<Offset> pathPositions = general.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 궁은_궁성_코너에서_오른쪽_위로_이동할_수_있다() {
        Position from = new Position(3, 0);
        Position to = new Offset(1, 1).applyTo(from);
        List<Offset> pathPositions = new General(Team.CHO, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 궁은_궁성_코너에서_오른쪽_아래로_이동할_수_있다() {
        Position from = new Position(3, 2);
        Position to = new Offset(1, -1).applyTo(from);
        List<Offset> pathPositions = new General(Team.CHO, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 궁은_궁성_코너에서_왼쪽_위로_이동할_수_있다() {
        Position from = new Position(5, 0);
        Position to = new Offset(-1, 1).applyTo(from);
        List<Offset> pathPositions = new General(Team.CHO, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 궁은_궁성_코너에서_왼쪽_아래로_이동할_수_있다() {
        Position from = new Position(5, 2);
        Position to = new Offset(-1, -1).applyTo(from);
        List<Offset> pathPositions = new General(Team.CHO, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 궁은_궁성_밖으로_이동할_수_없다() {
        Position from = new Position(3, 0);
        Position to = new Offset(-1, 0).applyTo(from);
        assertThrows(IllegalStateException.class,
                () -> new General(Team.CHO, from).getPathOffset(to));
    }
}
