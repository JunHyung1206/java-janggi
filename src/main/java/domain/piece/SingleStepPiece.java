package domain.piece;

import domain.Offset;
import domain.Position;
import exception.ErrorMessage;

import java.util.List;

public abstract class SingleStepPiece extends PalaceMovePiece {
    public SingleStepPiece(PieceType pieceType, Team team, Position position) {
        super(pieceType, team, position);
    }

    @Override
    protected List<Offset> generatePaths(Position to) {
        return List.of();
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
            if (!isValidDiagonalPath(from, to)) {
                throw new IllegalStateException(ErrorMessage.INVALID_MOVE_RULE.getMessage());
            }
            return;
        }
        if (mustStayInPalace()) {
            requireBothInSamePalace(from, to);
        }
    }

    protected boolean mustStayInPalace() {
        return false;
    }

    @Override
    protected boolean isValidMove(Offset offset) {
        return offset.equals(offset.normalize());
    }
}
