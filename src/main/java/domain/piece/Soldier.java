package domain.piece;

import domain.Direction;
import domain.Offset;
import domain.board.Position;
import exception.ErrorMessage;

import java.util.Map;

public final class Soldier extends SingleStepPiece {
    private static final Map<Team, Direction> BACKWARD_DIRECTIONS = Map.of(
            Team.CHO, Direction.DOWN,
            Team.HAN, Direction.UP
    );
    private final Direction backwardDirection;

    public Soldier(Team team) {
        super(PieceType.SOLDIER, team);
        this.backwardDirection = BACKWARD_DIRECTIONS.get(team);
    }

    @Override
    protected void validateMoveRule(Position from, Position to) {
        super.validateMoveRule(from, to);
        Offset offset = Offset.of(from, to);
        Direction direction = Direction.of(offset);
        if (direction.containsBackWardDirection(backwardDirection)) {
            throw new IllegalStateException(ErrorMessage.INVALID_MOVE_RULE.getMessage());
        }
    }
}
