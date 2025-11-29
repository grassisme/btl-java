
package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Move.MajorMove;


import java.util.*;

public final class King extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };
    private final static Map<Integer, int[]> PRECOMPUTED_CANDIDATES = computeCandidates();
    private final boolean isCastled;
    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;


    public King(final Alliance alliance,
                final int piecePosition,
                final boolean kingSideCastleCapable,
                final boolean queenSideCastleCapable) {
        super(PieceType.KING, alliance, piecePosition, true);
        this.isCastled = false;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public King(final Alliance alliance,
                final int piecePosition,
                final boolean isFirstMove,
                final boolean isCastled,
                final boolean kingSideCastleCapable,
                final boolean queenSideCastleCapable) {
        super(PieceType.KING, alliance, piecePosition, isFirstMove);
        this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    private static Map<Integer, int[]> computeCandidates() {
        final Map<Integer, int[]> candidates = new HashMap<>();
        for (int position = 0; position < BoardUtils.NUM_TILES; position++) {
            final int[] legalOffsets = new int[CANDIDATE_MOVE_COORDINATES.length];
            int numLegalOffsets = 0;
            for (int offset : CANDIDATE_MOVE_COORDINATES) {
                if (isFirstColumnExclusion(position, offset) ||
                        isEighthColumnExclusion(position, offset)) {
                    continue;
                }
                final int destination = position + offset;
                if (BoardUtils.isValidTileCoordinate(destination)) {
                    legalOffsets[numLegalOffsets++] = offset;
                }
            }
            if (numLegalOffsets > 0) {
                candidates.put(position, Arrays.copyOf(legalOffsets, numLegalOffsets));
            }
        }
        return Collections.unmodifiableMap(candidates);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : PRECOMPUTED_CANDIDATES.get(this.piecePosition)) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);

            final Move move;
            if (pieceAtDestination == null) {
                move = new MajorMove(board, this, candidateDestinationCoordinate);
            } else {
                final Alliance pieceAtDestinationAllegiance = pieceAtDestination.getPieceAllegiance();
                if (this.pieceAlliance != pieceAtDestinationAllegiance) {
                    move = new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination);
                } else {
                    continue;
                }
            }
            legalMoves.add(move);
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public King getMovedPiece(final Move move) {
        return PieceUtils.INSTANCE.getKing(move.getMovedPiece().getPieceAllegiance(), move.getDestinationCoordinate(), false, move.isCastlingMove(), true);
    }


    private static boolean isFirstColumnExclusion(final int currentCandidate,
                                                  final int offset) {
        return BoardUtils.FIRST_COLUMN.get(currentCandidate)
                && ((offset == -9) || (offset == -1) ||
                (offset == 7));
    }

    private static boolean isEighthColumnExclusion(final int currentCandidate,
                                                   final int offset) {
        return BoardUtils.EIGHTH_COLUMN.get(currentCandidate)
                && ((offset == -7) || (offset == 1) ||
                (offset == 9));
    }
}
