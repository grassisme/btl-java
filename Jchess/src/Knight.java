import java.util.List;

public class Knight extends Piece {
    private final static int[] CANDIDATE_MOVE_CORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    Knight (final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }
    @Override
    public List<Move> calculateLegalMoves(Board board) {
        int candidatesDestinationCoordinate;
        for(final int currentCandidate : CANDIDATE_MOVE_CORDINATES) {
            candidatesDestinationCoordinate = this.piecePosition + currentCandidate;
        }
 
        return null;
    }


}
