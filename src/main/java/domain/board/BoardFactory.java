package domain.board;

import domain.piece.Cannon;
import domain.piece.Chariot;
import domain.piece.Elephant;
import domain.piece.General;
import domain.piece.Guard;
import domain.piece.Horse;
import domain.piece.Piece;
import domain.piece.PieceType;
import domain.piece.Soldier;
import domain.piece.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class BoardFactory {

    private static final Map<PieceType, BiFunction<Team, Position, Piece>> PIECE_GENERATORS = Map.of(
            PieceType.ELEPHANT, Elephant::new,
            PieceType.HORSE, Horse::new,
            PieceType.SOLDIER, Soldier::new,
            PieceType.CHARIOT, Chariot::new,
            PieceType.CANNON, Cannon::new,
            PieceType.GUARD, Guard::new,
            PieceType.GENERAL, General::new
    );

    private static final int GENERAL_COLUMN = 4;
    private static final List<Integer> SOLDIER_COLUMNS = List.of(0, 2, 4, 6, 8);
    private static final List<Integer> CHARIOT_COLUMNS = List.of(0, 8);
    private static final List<Integer> GUARD_COLUMNS = List.of(3, 5);
    private static final List<Integer> CANNON_COLUMNS = List.of(1, 7);
    private static final List<Integer> CHO_DYNAMIC_COLUMNS = List.of(1, 2, 6, 7);
    private static final List<Integer> HAN_DYNAMIC_COLUMNS = List.of(7, 6, 2, 1);

    private BoardFactory() {
    }

    public static Board createBoard(InitializeSetting choInitialSetting, InitializeSetting hanInitialSetting) {
        List<Piece> pieces = new ArrayList<>();
        placeSoldiers(pieces);
        placeChariots(pieces);
        placeGuards(pieces);
        placeCannons(pieces);
        placeGeneral(pieces);
        placeDynamicPieces(pieces, choInitialSetting, hanInitialSetting);
        return new Board(pieces);
    }

    public static Piece createPiece(PieceType pieceType, Team team, Position position) {
        return PIECE_GENERATORS.get(pieceType).apply(team, position);
    }

    private static void placeGeneral(List<Piece> pieces) {
        int choGeneralRow = 1;
        int hanGeneralRow = 8;
        pieces.add(createPiece(PieceType.GENERAL, Team.CHO, new Position(GENERAL_COLUMN, choGeneralRow)));
        pieces.add(createPiece(PieceType.GENERAL, Team.HAN, new Position(GENERAL_COLUMN, hanGeneralRow)));
    }

    private static void placeSoldiers(List<Piece> pieces) {
        int choSoldiersRow = 3;
        int hanSoldiersRow = 6;
        for (Integer column : SOLDIER_COLUMNS) {
            pieces.add(createPiece(PieceType.SOLDIER, Team.CHO, new Position(column, choSoldiersRow)));
            pieces.add(createPiece(PieceType.SOLDIER, Team.HAN, new Position(column, hanSoldiersRow)));
        }
    }

    private static void placeCannons(List<Piece> pieces) {
        int choCannonsRow = 2;
        int hanCannonsRow = 7;
        for (Integer column : CANNON_COLUMNS) {
            pieces.add(createPiece(PieceType.CANNON, Team.CHO, new Position(column, choCannonsRow)));
            pieces.add(createPiece(PieceType.CANNON, Team.HAN, new Position(column, hanCannonsRow)));
        }
    }

    private static void placeGuards(List<Piece> pieces) {
        int choGuardsRow = 0;
        int hanGuardsRow = 9;
        for (Integer column : GUARD_COLUMNS) {
            pieces.add(createPiece(PieceType.GUARD, Team.CHO, new Position(column, choGuardsRow)));
            pieces.add(createPiece(PieceType.GUARD, Team.HAN, new Position(column, hanGuardsRow)));
        }
    }

    private static void placeChariots(List<Piece> pieces) {
        int choChariotsRow = 0;
        int hanChariotsRow = 9;
        for (Integer column : CHARIOT_COLUMNS) {
            pieces.add(createPiece(PieceType.CHARIOT, Team.CHO, new Position(column, choChariotsRow)));
            pieces.add(createPiece(PieceType.CHARIOT, Team.HAN, new Position(column, hanChariotsRow)));
        }
    }

    private static void placeDynamicPieces(List<Piece> pieces, InitializeSetting choSetting, InitializeSetting hanSetting) {
        List<PieceType> choTypes = choSetting.getInitialSetting();
        List<PieceType> hanTypes = hanSetting.getInitialSetting();

        for (int i = 0; i < 4; i++) {
            pieces.add(createPiece(choTypes.get(i), Team.CHO, new Position(CHO_DYNAMIC_COLUMNS.get(i), 0)));
            pieces.add(createPiece(hanTypes.get(i), Team.HAN, new Position(HAN_DYNAMIC_COLUMNS.get(i), 9)));
        }
    }
}
