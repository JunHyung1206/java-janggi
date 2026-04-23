package domain.piece;

import domain.Position;

public final class Chariot extends StraightMovingPiece {
    public Chariot(Team team, Position position) {
        super(PieceType.CHARIOT, team, position);
    }

    @Override
    protected Piece createMoved(Position newPosition) {
        return new Chariot(getTeam(), newPosition);
    }
}
