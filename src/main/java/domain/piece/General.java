package domain.piece;

import domain.board.Position;

public final class General extends SingleStepPiece {
    public General(Team team, Position position) {
        super(PieceType.GENERAL, team, position);
    }

    @Override
    protected boolean mustStayInPalace() {
        return true;
    }

    @Override
    public Piece move(Position newPosition) {
        return new General(getTeam(), newPosition);
    }
}
