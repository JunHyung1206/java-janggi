package domain.piece;

import domain.Offset;
import domain.board.Board;
import domain.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SoldierChoTest {
    private static final Position FROM = new Position(4, 4);
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(List.of(new Soldier(Team.CHO, FROM)));
    }

    @Test
    void 졸은_앞으로_한칸_움직일_수_있다() {
        assertDoesNotThrow(() -> board.move(FROM, new Offset(0, 1).applyTo(FROM)));
    }

    @Test
    void 졸은_좌로_한칸_움직일_수_있다() {
        assertDoesNotThrow(() -> board.move(FROM, new Offset(-1, 0).applyTo(FROM)));
    }

    @Test
    void 졸은_우로_한칸_움직일_수_있다() {
        assertDoesNotThrow(() -> board.move(FROM, new Offset(1, 0).applyTo(FROM)));
    }

    @Test
    void 졸은_뒤로_가지_못한다() {
        assertThrows(IllegalStateException.class,
                () -> board.move(FROM, new Offset(0, -1).applyTo(FROM)));
    }

    @Test
    void 졸은_두칸을_가지_못한다() {
        assertThrows(IllegalStateException.class,
                () -> board.move(FROM, new Offset(0, 2).applyTo(FROM)));
    }

    @Test
    void 졸은_궁성_코너에서_중앙으로_대각선_이동할_수_있다() {
        Position corner = new Position(3, 7);
        Board b = new Board(List.of(new Soldier(Team.CHO, corner)));
        assertDoesNotThrow(() -> b.move(corner, new Offset(1, 1).applyTo(corner)));
    }

    @Test
    void 졸은_궁성_중앙에서_코너로_대각선_이동할_수_있다() {
        Position center = new Position(4, 8);
        Board b = new Board(List.of(new Soldier(Team.CHO, center)));
        assertDoesNotThrow(() -> b.move(center, new Offset(-1, 1).applyTo(center)));
    }

    @Test
    void 졸은_궁성에서_뒤쪽_대각선으로_갈_수_없다() {
        Position center = new Position(4, 8);
        Board b = new Board(List.of(new Soldier(Team.CHO, center)));
        assertThrows(IllegalStateException.class,
                () -> b.move(center, new Offset(-1, -1).applyTo(center)));
    }

    @Test
    void 졸은_궁성_밖에서_대각선으로_갈_수_없다() {
        Position outside = new Position(1, 8);
        Board b = new Board(List.of(new Soldier(Team.CHO, outside)));
        assertThrows(IllegalStateException.class,
                () -> b.move(outside, new Offset(1, 1).applyTo(outside)));
    }
}
