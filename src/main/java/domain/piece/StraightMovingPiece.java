package domain.piece;

import domain.Direction;
import domain.Offset;
import domain.Position;
import exception.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

public abstract class StraightMovingPiece extends PalaceMovePiece {
    public StraightMovingPiece(PieceType pieceType, Team team) {
        super(pieceType, team);
    }

    @Override
    protected void validateMoveRule(Position from, Position to) {
        Offset offset = Offset.of(from, to);
        if (!isValidMove(offset)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_MOVE_RULE.getMessage());
        }
        if (offset.isDiagonalMoving()) {
            requireBothInSamePalace(from, to);
            if (!isValidDiagonalPath(from, to) && !areBothPalaceCorners(from, to)) {
                throw new IllegalStateException(ErrorMessage.INVALID_MOVE_RULE.getMessage());
            }
        }
    }

    private boolean areBothPalaceCorners(Position from, Position to) {
        return findPalaceCenter(from)
                .map(center -> isPalaceCorner(from, center) && isPalaceCorner(to, center))
                .orElse(false);
    }

    @Override
    protected List<Offset> generatePaths(Position from, Position to) {
        Offset offset = Offset.of(from, to);
        Direction direction = Direction.of(offset);
        int distance = offset.calculateDistance();
        return generateRoute(direction, distance);
    }

    private List<Offset> generateRoute(Direction mainDirection, int distance) {
        Offset step = new Offset(0, 0);
        List<Offset> route = new ArrayList<>();
        for (int i = 0; i < distance - 1; i++) {
            step = step.add(mainDirection.unit());
            route.add(step);
        }
        return route;
    }

    @Override
    protected boolean isValidMove(Offset offset) {
        return offset.isStraightMoving() || offset.isDiagonalMoving();
    }
}
