package domain.piece;

import domain.board.Position;

public final class Guard extends SingleStepPiece {
    public Guard(Team team, Position position) {
        super(PieceType.GUARD, team, position);
    }

    @Override
    protected boolean mustStayInPalace() {
        return true;
    }

    @Override
    public Piece move(Position newPosition) {
        return new Guard(getTeam(), newPosition);
    }
}
