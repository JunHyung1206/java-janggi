package domain.piece;

import domain.Direction;
import domain.Offset;
import domain.Position;
import exception.ErrorMessage;

import java.util.List;

public final class Elephant extends JumpMovingPiece {
    public Elephant(Team team, Position position) {
        super(PieceType.ELEPHANT, team, position);
    }

    @Override
    protected void validateMoveRule(Position to) {
        Offset offset = Offset.of(getPosition(), to);
        if (!isValidMove(offset)) {
            throw new IllegalStateException(ErrorMessage.INVALID_MOVE_RULE.getMessage());
        }
    }

    @Override
    protected List<Offset> generateRoute(Direction main, Direction sub) {
        Offset start = new Offset(0, 0);
        Offset step1 = start.add(main.unit());
        Offset step2 = step1.add(main.unit()).add(sub.unit());
        return List.of(step1, step2);
    }

    @Override
    protected boolean isValidMove(Offset offset) {
        return (offset.absX() == 3 && offset.absY() == 2) || (offset.absX() == 2 && offset.absY() == 3);
    }

    @Override
    protected Piece createMoved(Position newPosition) {
        return new Elephant(getTeam(), newPosition);
    }
}
