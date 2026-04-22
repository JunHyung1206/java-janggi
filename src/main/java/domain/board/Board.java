package domain.board;

import domain.Offset;
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
        if (pieces == null) {
            throw new IllegalArgumentException("pieces는 null값일 수 없습니다.");
        }
        this.pieces = new ArrayList<>(pieces);
    }

    public void move(Position from, Position to) {
        validateActualMove(from, to);

        Piece sourcePiece = getRequiredPiece(from);
        Optional<Piece> targetPiece = getPiece(to);
        validateNotSameTeam(sourcePiece, targetPiece);

        List<Offset> pathOffsets = sourcePiece.getPathOffset(to);
        List<Piece> blockedPieces = getBlockedPieces(from, pathOffsets);

        sourcePiece.validateMove(blockedPieces);
        sourcePiece.validateTarget(targetPiece);

        targetPiece.ifPresent(pieces::remove);
        pieces.remove(sourcePiece);
        pieces.add(sourcePiece.move(to));
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

    private void validateNotSameTeam(Piece fromPiece, Optional<Piece> toPiece) {
        if (toPiece.isPresent() && fromPiece.isSameTeam(toPiece.get())) {
            throw new IllegalStateException(ErrorMessage.SAME_TEAM_OCCUPIED.getMessage());
        }
    }

    private List<Piece> getBlockedPieces(Position from, List<Offset> offsets) {
        return offsets.stream()
                .map(offset -> offset.applyTo(from))
                .map(this::getPiece)
                .flatMap(Optional::stream)
                .toList();
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
