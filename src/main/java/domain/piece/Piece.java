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

    public final Piece move(Position to, List<Piece> allPieces) {
        validateMoveRule(to);
        List<Piece> blockedPieces = findBlockedPiecesOnPath(to, allPieces);
        validateMove(blockedPieces);
        validateTarget(findPiece(to, allPieces));
        return createMoved(to);
    }

    private List<Piece> findBlockedPiecesOnPath(Position to, List<Piece> allPieces) {
        return generatePaths(to).stream()
                .map(offset -> offset.applyTo(getPosition()))
                .map(pos -> findPiece(pos, allPieces))
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<Piece> findPiece(Position pos, List<Piece> allPieces) {
        return allPieces.stream()
                .filter(p -> p.getPosition().equals(pos))
                .findFirst();
    }

    protected void validateMove(List<Piece> blockedPieces) {
        if (!blockedPieces.isEmpty()) {
            throw new IllegalStateException(ErrorMessage.PATH_BLOCKED.getMessage());
        }
    }

    protected void validateTarget(Optional<Piece> target) {
        if (target.isPresent() && isSameTeam(target.get())) {
            throw new IllegalStateException(ErrorMessage.SAME_TEAM_OCCUPIED.getMessage());
        }
    }

    protected abstract void validateMoveRule(Position to);

    protected abstract List<Offset> generatePaths(Position to);

    protected abstract boolean isValidMove(Offset offset);

    protected abstract Piece createMoved(Position newPosition);

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
