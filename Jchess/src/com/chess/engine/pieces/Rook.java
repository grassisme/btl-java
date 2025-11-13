package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorAttackMove; 
import com.chess.engine.board.Move.MajorMove;       
import com.chess.engine.board.MoveUtils;           

import java.util.*;

public final class Rook extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-8, -1, 1, 8}; 
    
    private final static Map<Integer, MoveUtils.Line[]> PRECOMPUTED_CANDIDATES = computeCandidates();

    public Rook(final Alliance alliance,
                final int piecePosition,
                final boolean isFirstMove) {
        super(PieceType.ROOK, alliance, piecePosition, isFirstMove);
    }
    
    public Rook(final Alliance alliance,
                final int piecePosition) {
        this(alliance, piecePosition, false);
    }
    
    
    private static Map<Integer, MoveUtils.Line[]> computeCandidates() {
        final Map<Integer, MoveUtils.Line[]> candidates = new HashMap<>();
        
        for (int position = 0; position < BoardUtils.NUM_TILES; position++) {
            final List<MoveUtils.Line> lines = new ArrayList<>();
            
            for (int offset : CANDIDATE_MOVE_COORDINATES) {
                int destination = position;
                MoveUtils.Line line = new MoveUtils.Line();
                
                while (BoardUtils.isValidTileCoordinate(destination)) {
                    
                    if (isFirstColumnExclusion(destination, offset) ||
                        isEighthColumnExclusion(destination, offset)) {
                        break;
                    }
                    
                    destination += offset;
                    
                    if (BoardUtils.isValidTileCoordinate(destination)) {
                        line.addCoordinate(destination);
                    }
                }
                
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
            
            if (!lines.isEmpty()) {
                candidates.put(position, lines.toArray(new MoveUtils.Line[0]));
            }
        }
        return Collections.unmodifiableMap(candidates);
    }
    
    private static boolean isFirstColumnExclusion(final int position,
                                                  final int offset) {
        return (BoardUtils.FIRST_COLUMN.get(position) && (offset == -1));
    }

    private static boolean isEighthColumnExclusion(final int position,
                                                  final int offset) {
        return BoardUtils.EIGHTH_COLUMN.get(position) && (offset == 1);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        
        final MoveUtils.Line[] lines = PRECOMPUTED_CANDIDATES.get(this.piecePosition);
        if (lines == null) {
            return Collections.emptyList();
        }
        
        for (final MoveUtils.Line line : lines) {
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

    @Override
    public int locationBonus() {
        return this.pieceAlliance.rookBonus(this.piecePosition); 
    }

    @Override
    public Rook getMovedPiece(final Move move) {
        return PieceUtils.INSTANCE.getRook(move.getMovedPiece().getPieceAllegiance(), move.getDestinationCoordinate(), true);
    }

    @Override
    public String toString() {
        return PieceType.ROOK.toString();
    }
}
