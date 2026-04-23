package domain.piece;

import domain.Direction;
import domain.Offset;
import domain.board.Position;
import exception.ErrorMessage;

import java.util.List;

public final class Horse extends JumpMovingPiece {
    public Horse(Team team, Position position) {
        super(PieceType.HORSE, team, position);
    }

    @Override
    protected void validateMoveRule(Position to) {
        Offset offset = Offset.of(getPosition(), to);
        if (!isValidMove(offset)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_MOVE_RULE.getMessage());
        }
    }

    @Override
    protected List<Offset> generateRoute(Direction main, Direction sub) {
        Offset step1 = new Offset(0, 0).add(main.unit());
        return List.of(step1);
    }

    @Override
    protected boolean isValidMove(Offset offset) {
        return (offset.absX() == 2 && offset.absY() == 1) || (offset.absX() == 1 && offset.absY() == 2);
    }

    @Override
    protected Piece createMoved(Position newPosition) {
        return new Horse(getTeam(), newPosition);
    }
}
