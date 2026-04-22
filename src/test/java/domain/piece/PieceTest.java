package domain.piece;

import domain.board.Position;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PieceTest {
    @Test
    void 같은_팀의_기물인지_확인한다() {
        Piece piece = new Horse(Team.CHO, new Position(0, 0));
        Piece anotherPiece = new Horse(Team.CHO, new Position(1, 0));

        Assertions.assertThat(piece.isSameTeam(anotherPiece)).isTrue();
    }

    @Test
    void 다른_팀의_기물인지_확인한다() {
        Piece piece = new Horse(Team.CHO, new Position(0, 0));
        Piece anotherPiece = new Horse(Team.HAN, new Position(1, 0));

        Assertions.assertThat(piece.isSameTeam(anotherPiece)).isFalse();
    }
}
