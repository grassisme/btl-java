package com.chess.engine.board;

import com.chess.engine.player.Player;

import java.util.ArrayList;
import java.util.List;

import static com.chess.engine.board.Move.*;

public enum MoveUtils {

    INSTANCE;

    public static final Move NULL_MOVE = new NullMove();

    public static List<Move> getQuiescenceMoves(final Player player) {
        final List<Move> interestingMoves = new ArrayList<>();
        for (final Move move : player.getLegalMoves()) {
            if (move.isAttack()) {
                interestingMoves.add(move);
            }
        }
        return interestingMoves;
    }

    public static List<Move> getPromotionOnlyMoves(final Player player) {
        final List<Move> promotions = new ArrayList<>();
        for (final Move move : player.getLegalMoves()) {
            if (move instanceof PawnPromotion) {
                promotions.add(move);
            }
        }
        return promotions;
    }

    public static Move getMove(final Board board,
                               final String from,
                               final String to) {
        return MoveFactory.createMove(board, BoardUtils.INSTANCE.getCoordinateAtPosition(from),
                BoardUtils.INSTANCE.getCoordinateAtPosition(to));
    }

    public static class Line {
        private final List<Integer> coordinates;

        public Line() {
            this.coordinates = new ArrayList<>();
        }

        public void addCoordinate(final int coordinate) {
            this.coordinates.add(coordinate);
        }

        public List<Integer> getLineCoordinates() {
            return this.coordinates;
        }

        public boolean isEmpty() {
            return this.coordinates.isEmpty();
        }
    }
}
