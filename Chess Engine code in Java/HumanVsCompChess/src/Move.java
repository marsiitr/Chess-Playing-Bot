
public class Move implements Pieces {
	int src, dest;
	int piece;
	GameState gs;	// GameState after the move is made
	int[] posn;
	int eval;
	String comment = "";
	boolean isLegal;
	int promoPiece;

	public Move( int piece, int src, int dest ) {

		this.piece = piece;
		this.src = src;
		this.dest = dest;
		isLegal = true;
	}
	
	public Move( String comment ) {
		this.comment = comment;
		this.isLegal = false;
	}
	
	public Move( int piece, int src, int dest, GameState gs ) {

		this.piece = piece;
		this.src = src;
		this.dest = dest;
		this.gs = gs;
		isLegal = true;
	}
	
	public Move( Game game, int[] m, GameState gs ) {
	
		posn = game.getPosn();
		this.gs = gs;

		src = m[0];
		dest = m[1];
		piece = posn[src];

		posn[src] = 0;
		posn[dest] = (m[2] != 0) ? m[2] : piece;
		
		if( gs.epFlag ) {
			if( gs.white ) posn[dest-8] = 0;
			else posn[dest+8] = 0;
		}

		else if( gs.ckFlag ) {
			if( gs.white ) {
				posn[H8] = 0;
				posn[F8] = BR;
			}
			else {
				posn[H1] = 0;
				posn[F1] = WR;
			}
		}
		
		else if( gs.cqFlag ) {
			if( gs.white ) {
				posn[A8] = 0;
				posn[D8] = BR;
			}
			else {
				posn[A1] = 0;
				posn[D1] = WR;
			}
		}
	}
		

	public String alg() {
	
		return Utils.toAlg.get(src) +"-" +Utils.toAlg.get(dest);
	}
}