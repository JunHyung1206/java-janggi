package domain.piece;

import domain.board.Position;
import exception.ErrorMessage;

import java.util.List;
import java.util.Optional;

public final class Cannon extends StraightMovingPiece {
    public Cannon(Team team, Position position) {
        super(PieceType.CANNON, team, position);
    }

    @Override
    public void validateMove(List<Piece> blockedPieces) {
        if (blockedPieces.isEmpty()) {
            throw new IllegalStateException(ErrorMessage.CANNON_NEEDS_BRIDGE.getMessage());
        }
        if (blockedPieces.size() >= 2) {
            throw new IllegalStateException(ErrorMessage.CANNON_CANNOT_OVER_PIECES.getMessage());
        }
        Piece piece = blockedPieces.getFirst();
        if (piece instanceof Cannon) {
            throw new IllegalStateException(ErrorMessage.CANNON_CANNOT_OVER_CANNON.getMessage());
        }
    }

    @Override
    public void validateTarget(Optional<Piece> target) {
        target.ifPresent(piece -> {
            if (piece.isSameType(PieceType.CANNON)) {
                throw new IllegalStateException(ErrorMessage.CANNON_CANNOT_TAKE_CANNON.getMessage());
            }
        });
    }

    @Override
    public Piece move(Position newPosition) {
        return new Cannon(getTeam(), newPosition);
    }
}
