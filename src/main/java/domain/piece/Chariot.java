package domain.piece;

import domain.board.Position;

public final class Chariot extends StraightMovingPiece {
    public Chariot(Team team, Position position) {
        super(PieceType.CHARIOT, team, position);
    }

    @Override
    public Piece move(Position newPosition) {
        return new Chariot(getTeam(), newPosition);
    }
}
