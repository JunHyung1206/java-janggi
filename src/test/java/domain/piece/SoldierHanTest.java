package domain.piece;

import domain.Offset;
import domain.board.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SoldierHanTest {

    private static final Position FROM = new Position(4, 6);
    private Piece soldier;

    @BeforeEach
    void setUp() {
        soldier = new Soldier(Team.HAN, FROM);
    }

    @Test
    void 병은_아래로_한칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(0, -1).applyTo(FROM);
        List<Offset> pathPositions = soldier.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 병은_좌로_한칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(-1, 0).applyTo(FROM);
        List<Offset> pathPositions = soldier.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 병은_우로_한칸_움직일_수_있는_경로가_있다() {
        Position to = new Offset(1, 0).applyTo(FROM);
        List<Offset> pathPositions = soldier.getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 병은_위로_가지_못한다() {
        Position to = new Offset(0, 1).applyTo(FROM);
        assertThrows(IllegalStateException.class, () -> soldier.getPathOffset(to));
    }

    @Test
    void 병은_궁성에_있을때_오른쪽_아래_방향으로_갈_수_있다() {
        Position from = new Position(3, 2);
        Position to = new Offset(1, -1).applyTo(from);
        List<Offset> pathPositions = new Soldier(Team.HAN, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 병은_궁성에_있을때_왼쪽_아래_방향으로_갈_수_있다() {
        Position from = new Position(4, 1);
        Position to = new Offset(-1, -1).applyTo(from);
        List<Offset> pathPositions = new Soldier(Team.HAN, from).getPathOffset(to);
        assertThat(pathPositions).isEqualTo(List.of());
    }

    @Test
    void 병은_궁성에_있을때_왼쪽_위_방향으로_갈_수_없다() {
        Position from = new Position(4, 1);
        Position to = new Offset(-1, 1).applyTo(from);
        assertThrows(IllegalStateException.class,
                () -> new Soldier(Team.HAN, from).getPathOffset(to));
    }

    @Test
    void 병은_궁성에_있을때_오른쪽_위_방향으로_갈_수_없다() {
        Position from = new Position(4, 1);
        Position to = new Offset(1, 1).applyTo(from);
        assertThrows(IllegalStateException.class,
                () -> new Soldier(Team.HAN, from).getPathOffset(to));
    }

    @Test
    void 병은_궁성_밖에서_오른쪽_아래_방향으로_갈_수_없다() {
        Position from = new Position(1, 4);
        Position to = new Offset(1, -1).applyTo(from);
        assertThrows(IllegalStateException.class,
                () -> new Soldier(Team.HAN, from).getPathOffset(to));
    }

    @Test
    void 병은_궁성_밖에서_왼쪽_아래_방향으로_갈_수_없다() {
        Position from = new Position(1, 4);
        Position to = new Offset(-1, -1).applyTo(from);
        assertThrows(IllegalStateException.class,
                () -> new Soldier(Team.HAN, from).getPathOffset(to));
    }
}
