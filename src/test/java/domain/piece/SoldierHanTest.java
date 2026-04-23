package domain.piece;

import domain.Offset;
import domain.board.Board;
import domain.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SoldierHanTest {
    private static final Position FROM = new Position(4, 6);
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(List.of(new Soldier(Team.HAN, FROM)));
    }

    @Test
    void 병은_앞으로_한칸_움직일_수_있다() {
        assertDoesNotThrow(() -> board.move(FROM, new Offset(0, -1).applyTo(FROM)));
    }

    @Test
    void 병은_좌로_한칸_움직일_수_있다() {
        assertDoesNotThrow(() -> board.move(FROM, new Offset(-1, 0).applyTo(FROM)));
    }

    @Test
    void 병은_우로_한칸_움직일_수_있다() {
        assertDoesNotThrow(() -> board.move(FROM, new Offset(1, 0).applyTo(FROM)));
    }

    @Test
    void 병은_뒤로_가지_못한다() {
        assertThrows(IllegalStateException.class,
                () -> board.move(FROM, new Offset(0, 1).applyTo(FROM)));
    }

    @Test
    void 병은_궁성_코너에서_중앙으로_대각선_이동할_수_있다() {
        Position corner = new Position(3, 2);
        Board b = new Board(List.of(new Soldier(Team.HAN, corner)));
        assertDoesNotThrow(() -> b.move(corner, new Offset(1, -1).applyTo(corner)));
    }

    @Test
    void 병은_궁성_중앙에서_코너로_대각선_이동할_수_있다() {
        Position center = new Position(4, 1);
        Board b = new Board(List.of(new Soldier(Team.HAN, center)));
        assertDoesNotThrow(() -> b.move(center, new Offset(-1, -1).applyTo(center)));
    }

    @Test
    void 병은_궁성에서_뒤쪽_대각선으로_갈_수_없다() {
        Position center = new Position(4, 1);
        Board b = new Board(List.of(new Soldier(Team.HAN, center)));
        assertThrows(IllegalStateException.class,
                () -> b.move(center, new Offset(-1, 1).applyTo(center)));
    }

    @Test
    void 병은_궁성_밖에서_대각선으로_갈_수_없다() {
        Position outside = new Position(1, 4);
        Board b = new Board(List.of(new Soldier(Team.HAN, outside)));
        assertThrows(IllegalStateException.class,
                () -> b.move(outside, new Offset(1, -1).applyTo(outside)));
    }
}
