package domain.piece;

import domain.Position;
import exception.ErrorMessage;

import java.util.Optional;

public abstract class PalaceMovePiece extends Piece {
    private static final Position CHO_PALACE_CENTER = new Position(4, 1);
    private static final Position HAN_PALACE_CENTER = new Position(4, 8);

    public PalaceMovePiece(PieceType pieceType, Team team) {
        super(pieceType, team);
    }

    protected Optional<Position> findPalaceCenter(Position pos) {
        if (isInPalace(pos, CHO_PALACE_CENTER)) {
            return Optional.of(CHO_PALACE_CENTER);
        }
        if (isInPalace(pos, HAN_PALACE_CENTER)) {
            return Optional.of(HAN_PALACE_CENTER);
        }
        return Optional.empty();
    }

    private boolean isInPalace(Position pos, Position center) {
        return pos.x() >= center.x() - 1 && pos.x() <= center.x() + 1
                && pos.y() >= center.y() - 1 && pos.y() <= center.y() + 1;
    }

    protected boolean isPalaceCenter(Position pos, Position center) {
        return center.equals(pos);
    }

    protected boolean isPalaceCorner(Position pos, Position center) {
        return Math.abs(pos.x() - center.x()) == 1 && Math.abs(pos.y() - center.y()) == 1;
    }

    protected void requireBothInSamePalace(Position from, Position to) {
        Optional<Position> centerOpt = findPalaceCenter(from);
        if (centerOpt.isEmpty() || !isInPalace(to, centerOpt.get())) {
            throw new IllegalStateException(ErrorMessage.INVALID_MOVE_RULE.getMessage());
        }
    }

    protected boolean isValidDiagonalPath(Position from, Position to) {
        Optional<Position> centerOpt = findPalaceCenter(from);
        if (centerOpt.isEmpty()) return false;
        Position center = centerOpt.get();
        return (isPalaceCenter(from, center) && isPalaceCorner(to, center))
                || (isPalaceCorner(from, center) && isPalaceCenter(to, center));
    }
}
