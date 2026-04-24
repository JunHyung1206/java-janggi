package domain.piece;

import domain.Offset;
import domain.board.Position;
import exception.ErrorMessage;

import java.util.List;

public abstract class SingleStepPiece extends PalaceMovePiece {
    public SingleStepPiece(PieceType pieceType, Team team) {
        super(pieceType, team);
    }

    @Override
    protected List<Offset> generatePaths(Position from, Position to) {
        return List.of();
    }

    @Override
    protected void validateMoveRule(Position from, Position to) {
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
