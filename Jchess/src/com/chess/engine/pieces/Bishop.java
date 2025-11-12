package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class Bishop extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -7, 7, 9};
    private final static Map<Integer, MoveUtils.Line[]> PRECOMPUTED_CANDIDATES = computeCandidates();

    Bishop(final Alliance alliance,
           final int piecePosition,
           final boolean isFirstMove) {
        super(PieceType.BISHOP, alliance, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final MoveUtils.Line line : PRECOMPUTED_CANDIDATES.get(this.piecePosition)) {
            for (final int candidateDestinationCoordinate : line.getLineCoordinates()) {
                final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);
                if (pieceAtDestination == null) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAllegiance();
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate,
                                pieceAtDestination));
                    }
                    break;
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }


}

