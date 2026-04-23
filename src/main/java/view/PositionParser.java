package view;

import domain.Position;

public final class PositionParser {

    private PositionParser() {
    }

    public static Position parse(String input) {
        String cleaned = input.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

        if (cleaned.length() != 2) {
            throw new IllegalArgumentException("올바른 형식이 아닙니다. (예: A0)");
        }

        char xChar = cleaned.charAt(0);
        char yChar = cleaned.charAt(1);

        if (xChar < 'A' || xChar > 'I') {
            throw new IllegalArgumentException("X 좌표는 A부터 I 사이의 알파벳이어야 합니다.");
        }
        if (yChar < '0' || yChar > '9') {
            throw new IllegalArgumentException("Y 좌표는 0부터 9 사이의 숫자이어야 합니다.");
        }

        int x = xChar - 'A';
        int y = yChar - '0';

        return new Position(x, y);
    }
}
