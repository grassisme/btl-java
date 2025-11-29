package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

import java.time.Period;

public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;
    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }
    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }
    public abstract Board execute();
    public static final class MayjorMovve extends Move {
        public MayjorMovve(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            final Board.Builder builder = new Board.Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOppoment().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setMoveMaker(null);
            builder.setMoveMaker(this.board.currentPlayer().getOppoment().getAllience());
            return builder.build();
            return null;
        }
    }
    public static final class AttackrMove extends Move {

        final Piece attackedPiece;

        public AttackMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Piece attackedPiece){
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
        @Override
        public Board execute() {
            return null;
        }

    }
}
