package com.chess.engine.board;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

public abstract class Tile{
    protected final int tileCoordinate;
    private static final Map<Integer, EmptyTile> EMPTY_TILES = creatAllPossibleEmptyTiles();
    
    private static Map<Integer, EmptyTile> creatAllPossibleEmptyTiles(){
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        for(int i =0;i<64;i++){
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }

    public static Tile creatTile(final int tileCoordinate, final Peice peice){
        return peice != null ? new OccuipiedTile(tileCoordinate, peice): EmptyTile.get(tileCoordinate);
    }

    private Tile (int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }
    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();
    public static final class EmptyTile extends Tile{
        EmptyTile(final int coordinate){
            super(coordinate);
        }

        @Override
        public boolean isTileOccupied(){
            return false;
        }

        @Override
        public Piece getPiece(){
            return null;
        }
    }
    public static final class OccuipiedTile extends Tile{
        private final Piece pieceOnTile;

        public OccuipiedTile(int tileCoordinate, Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isTileOccupied(){
            return true;
        }
        
        @Override
        public Piece getPiece(){
            return this.pieceOnTile;
            
        }
    }
}