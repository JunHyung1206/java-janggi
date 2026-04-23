package domain.piece;

import domain.Position;

public final class General extends SingleStepPiece {
    public General(Team team, Position position) {
        super(PieceType.GENERAL, team, position);
    }

    @Override
    protected boolean mustStayInPalace() {
        return true;
    }

    @Override
    protected Piece createMoved(Position newPosition) {
        return new General(getTeam(), newPosition);
    }
}
