package domain.piece;

import domain.Position;

public final class Guard extends SingleStepPiece {
    public Guard(Team team, Position position) {
        super(PieceType.GUARD, team, position);
    }

    @Override
    protected boolean mustStayInPalace() {
        return true;
    }

    @Override
    protected Piece createMoved(Position newPosition) {
        return new Guard(getTeam(), newPosition);
    }
}
