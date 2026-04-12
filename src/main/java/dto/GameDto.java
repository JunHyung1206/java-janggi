package dto;

import java.util.List;

public record GameDto(String turn, double choScore, double hanScore, List<PieceDto> pieces) {}
