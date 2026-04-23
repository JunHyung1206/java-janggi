package domain.piece;

import domain.Direction;
import domain.Offset;
import domain.Position;
import exception.ErrorMessage;

import java.util.Map;

public final class Soldier extends SingleStepPiece {
    private static final Map<Team, Direction> backwardDirections = Map.of(Team.CHO, Direction.DOWN, Team.HAN, Direction.UP);
    private final Direction backwardDirection;

    public Soldier(Team team, Position position) {
        super(PieceType.SOLDIER, team, position);
        this.backwardDirection = backwardDirections.get(team);
    }

    @Override
    protected void validateMoveRule(Position to) {
        super.validateMoveRule(to);
        Position from = getPosition();
        Offset offset = Offset.of(from, to);
        Direction direction = Direction.of(offset);
        if (direction.hasSameVerticalDirectionAs(backwardDirection)) {
            throw new IllegalStateException(ErrorMessage.INVALID_MOVE_RULE.getMessage());
        }
    }

    @Override
    protected Piece createMoved(Position newPosition) {
        return new Soldier(getTeam(), newPosition);
    }
}
