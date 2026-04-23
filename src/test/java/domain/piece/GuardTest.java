package domain.piece;

import domain.Offset;
import domain.board.Board;
import domain.board.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GuardTest {
    private static final Position CENTER = new Position(4, 8);
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(List.of(new Guard(Team.HAN, CENTER)));
    }

    @Test
    void 사는_위로_한칸_움직일_수_있다() {
        assertDoesNotThrow(() -> board.move(CENTER, new Offset(0, 1).applyTo(CENTER)));
    }

    @Test
    void 사는_아래로_한칸_움직일_수_있다() {
        assertDoesNotThrow(() -> board.move(CENTER, new Offset(0, -1).applyTo(CENTER)));
    }

    @Test
    void 사는_좌로_한칸_움직일_수_있다() {
        assertDoesNotThrow(() -> board.move(CENTER, new Offset(-1, 0).applyTo(CENTER)));
    }

    @Test
    void 사는_우로_한칸_움직일_수_있다() {
        assertDoesNotThrow(() -> board.move(CENTER, new Offset(1, 0).applyTo(CENTER)));
    }

    @Test
    void 사는_두칸을_이동할_수_없다() {
        Position from = new Position(3, 8);
        Board b = new Board(List.of(new Guard(Team.HAN, from)));
        assertThrows(IllegalStateException.class, () -> b.move(from, new Offset(2, 0).applyTo(from)));
    }

    @Test
    void 사는_중앙에서_대각선으로_이동할_수_있다() {
        assertDoesNotThrow(() -> board.move(CENTER, new Offset(1, 1).applyTo(CENTER)));
    }

    @Test
    void 사는_코너에서_중앙으로_대각선_이동할_수_있다() {
        Position corner = new Position(3, 7);
        Board b = new Board(List.of(new Guard(Team.HAN, corner)));
        assertDoesNotThrow(() -> b.move(corner, new Offset(1, 1).applyTo(corner)));
    }

    @Test
    void 사는_코너에서_오른쪽_아래로_이동할_수_있다() {
        Position corner = new Position(3, 9);
        Board b = new Board(List.of(new Guard(Team.HAN, corner)));
        assertDoesNotThrow(() -> b.move(corner, new Offset(1, -1).applyTo(corner)));
    }

    @Test
    void 사는_코너에서_왼쪽_위로_이동할_수_있다() {
        Position corner = new Position(5, 7);
        Board b = new Board(List.of(new Guard(Team.HAN, corner)));
        assertDoesNotThrow(() -> b.move(corner, new Offset(-1, 1).applyTo(corner)));
    }

    @Test
    void 사는_코너에서_왼쪽_아래로_이동할_수_있다() {
        Position corner = new Position(5, 9);
        Board b = new Board(List.of(new Guard(Team.HAN, corner)));
        assertDoesNotThrow(() -> b.move(corner, new Offset(-1, -1).applyTo(corner)));
    }

    @Test
    void 사는_궁성_밖으로_이동할_수_없다() {
        Position edge = new Position(3, 8);
        Board b = new Board(List.of(new Guard(Team.HAN, edge)));
        assertThrows(IllegalStateException.class, () -> b.move(edge, new Offset(-1, 0).applyTo(edge)));
    }
}
