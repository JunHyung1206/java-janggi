package domain.piece;

import domain.Direction;
import domain.Offset;
import domain.Position;
import exception.ErrorMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class StraightMovingPiece extends PalaceMovePiece {
    public StraightMovingPiece(PieceType pieceType, Team team, Position position) {
        super(pieceType, team, position);
    }

    @Override
    protected void validateMoveRule(Position to) {
        Position from = getPosition();
        Offset offset = Offset.of(from, to);
        if (!isValidMove(offset)) {
            throw new IllegalStateException(ErrorMessage.INVALID_MOVE_RULE.getMessage());
        }
        if (offset.isDiagonalMoving()) {
            requireBothInSamePalace(from, to);
            if (!isValidDiagonalPath(from, to) && !areBothPalaceCorners(from, to)) {
                throw new IllegalStateException(ErrorMessage.INVALID_MOVE_RULE.getMessage());
            }
        }
    }

    private boolean areBothPalaceCorners(Position from, Position to) {
        Optional<Position> centerOpt = findPalaceCenter(from);
        if (centerOpt.isEmpty()) return false;
        Position center = centerOpt.get();
        return isPalaceCorner(from, center) && isPalaceCorner(to, center);
    }

    @Override
    protected List<Offset> generatePaths(Position to) {
        Position from = getPosition();
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
