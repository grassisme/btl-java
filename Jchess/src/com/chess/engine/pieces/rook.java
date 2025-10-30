import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class rook extends Piece {
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-8, -1, 1, 8};
    // xe di theo 2 huong thang dung len xuong, nhu vay xe di chuyen theo chuan do thi Oxy -> (+/-)1
    // tuy nhien khi tien len thi se theo dang lui/tien vuot cot/hang ngoai cung ->(+/-)8
    Rook(int piecePosition, Alliance pieceAlliance){
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final int currentCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestinationCoordinate = this.piecePosition;

            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                if(isFirstColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset) ||
                   isEighthColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset)){
                    break;
                }

                candidateDestinationCoordinate += currentCandidateOffset;
                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if(!candidateDestinationTile.isTileOccupied()){
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        if(this.pieceAlliance != pieceAlliance){
                            legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }
    }
    return ImutableList.copyOf(LegalMoves);
}
// tranh quan co roi khoi ban co
private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
    return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1); 
}
private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
    return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 1);
}

