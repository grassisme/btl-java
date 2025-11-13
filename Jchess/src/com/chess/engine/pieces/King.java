package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move.KingSideCastleMove; 
import com.chess.engine.board.Move.QueenSideCastleMove; 
import com.chess.engine.board.MoveUtils;

import java.util.*;

public final class King extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    private final static Map<Integer, List<Integer>> PRECOMPUTED_CANDIDATES = computeCandidates();

    public King(final Alliance alliance,
                final int piecePosition,
                final boolean isFirstMove) {
        super(PieceType.KING, alliance, piecePosition, isFirstMove);
    }

    private static Map<Integer, List<Integer>> computeCandidates() {
        final Map<Integer, List<Integer>> candidates = new HashMap<>();
        
        for (int position = 0; position < BoardUtils.NUM_TILES; position++) {
            final List<Integer> destinationCoordinates = new ArrayList<>();
            
            for (final int offset : CANDIDATE_MOVE_COORDINATES) {
                
                final int candidateDestinationCoordinate = position + offset;
                
                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    
                    if (isColumnExclusion(position, offset)) {
                        continue; 
                    }

                    destinationCoordinates.add(candidateDestinationCoordinate);
                }
            }
            candidates.put(position, Collections.unmodifiableList(destinationCoordinates));
        }
        return Collections.unmodifiableMap(candidates);
    }

    private static boolean isColumnExclusion(final int currentPosition, final int offset) {
        final boolean isFirstColumnExclusion = BoardUtils.FIRST_COLUMN.get(currentPosition) &&
                                               (offset == -9 || offset == -1 || offset == 7);
        
        final boolean isEighthColumnExclusion = BoardUtils.EIGHTH_COLUMN.get(currentPosition) &&
                                                (offset == -7 || offset == 1 || offset == 9);
        
        return isFirstColumnExclusion || isEighthColumnExclusion;
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        final List<Integer> candidateDestinations = PRECOMPUTED_CANDIDATES.get(this.piecePosition);
        if (candidateDestinations != null) {
            for (final int candidateDestinationCoordinate : candidateDestinations) {
                final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);
                
                if (pieceAtDestination == null) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } 
                else {
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAllegiance();
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate,
                                                           pieceAtDestination));
                    }
                }
            }
        }
        
        legalMoves.addAll(calculateKingCastles(board, legalMoves));

        return Collections.unmodifiableList(legalMoves);
    }
    
    private Collection<Move> calculateKingCastles(final Board board, final List<Move> currentLegalMoves) {
        final List<Move> kingCastles = new ArrayList<>();
        
        if (this.isFirstMove()) {
            if (this.pieceAlliance.isWhite()) {
                if (BoardUtils.isKingSideCastleCapable(board, this)) {
                    kingCastles.add(new KingSideCastleMove(board, this, 62, 63, 61)); 
                }
                if (BoardUtils.isQueenSideCastleCapable(board, this)) {
                    kingCastles.add(new QueenSideCastleMove(board, this, 58, 56, 59));
                }
            } 
            else if (this.pieceAlliance.isBlack()) {
                if (BoardUtils.isKingSideCastleCapable(board, this)) {
                    kingCastles.add(new KingSideCastleMove(board, this, 6, 7, 5));
                }
                if (BoardUtils.isQueenSideCastleCapable(board, this)) {
                    kingCastles.add(new QueenSideCastleMove(board, this, 2, 0, 3));
                }
            }
        }
        return kingCastles;
    }

    @Override
    public int locationBonus() {
        return this.pieceAlliance.kingBonus(this.piecePosition);
    }

    @Override
    public King getMovedPiece(final Move move) {
        return PieceUtils.INSTANCE.getKing(move.getMovedPiece().getPieceAllegiance(), move.getDestinationCoordinate(), true);
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }
}
