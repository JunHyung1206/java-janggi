package domain.board;

import domain.piece.Piece;
import domain.piece.PieceType;
import domain.piece.Team;
import exception.ErrorMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Board {
    private final List<Piece> pieces;

    public Board(List<Piece> pieces) {
        this.pieces = new ArrayList<>(pieces);
    }

    public void move(Position from, Position to) {
        validateActualMove(from, to);

        Piece sourcePiece = getRequiredPiece(from);
        Piece movedPiece = sourcePiece.move(to, pieces);

        getPiece(to).ifPresent(pieces::remove);
        pieces.remove(sourcePiece);
        pieces.add(movedPiece);
    }

    public Optional<Piece> getPiece(Position position) {
        return pieces.stream()
                .filter(p -> p.getPosition().equals(position))
                .findFirst();
    }

    public Piece getRequiredPiece(Position position) {
        return getPiece(position)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.EMPTY_SOURCE.getMessage()));
    }

    private void validateActualMove(Position from, Position to) {
        if (from.equals(to)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_MOVE.getMessage());
        }
    }

    public double calculateScore(Team team) {
        return team.getScore() + pieces.stream()
                .filter(piece -> piece.isSameTeam(team))
                .mapToInt(Piece::score)
                .sum();
    }

    public boolean isAliveGeneral(Team team) {
        return pieces.stream()
                .filter(piece -> piece.isSameTeam(team))
                .anyMatch(piece -> piece.isSameType(PieceType.GENERAL));
    }

    public List<Piece> getPieces() {
        return List.copyOf(pieces);
    }
}
