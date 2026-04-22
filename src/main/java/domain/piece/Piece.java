package domain.piece;

import domain.Offset;
import domain.board.Position;
import exception.ErrorMessage;

import java.util.List;
import java.util.Optional;

public abstract class Piece {
    private final PieceType pieceType;
    private final Team team;
    private final Position position;

    public Piece(PieceType pieceType, Team team, Position position) {
        this.pieceType = pieceType;
        this.team = team;
        this.position = position;
    }

    public final List<Offset> getPathOffset(Position to) {
        validateMoveRule(to);
        return generatePaths(to);
    }

    public void validateMove(List<Piece> blockedPieces) {
        if (!blockedPieces.isEmpty()) {
            throw new IllegalStateException(ErrorMessage.PATH_BLOCKED.getMessage());
        }
    }

    public void validateTarget(Optional<Piece> target) {
    }

    protected abstract void validateMoveRule(Position to);

    protected abstract List<Offset> generatePaths(Position to);

    protected abstract boolean isValidMove(Offset offset);

    public abstract Piece move(Position newPosition);

    public Position getPosition() {
        return position;
    }

    public boolean isSameTeam(Team team) {
        return this.team == team;
    }

    public boolean isSameTeam(Piece another) {
        return isSameTeam(another.team);
    }

    public boolean isSameType(PieceType type) {
        return this.pieceType == type;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public Team getTeam() {
        return team;
    }

    public int score() {
        return pieceType.getScore();
    }
}
