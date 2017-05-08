import java.util.*;

public class GameState implements Pieces {

	boolean white;	// side to move
	boolean wck;	// white can castle kingside
	boolean wcq;	// white can castle queenside
	boolean bck;	// black can castle kingside
	boolean bcq;	// black can castle queenside
	int ept;		// en-passant target square
	int hmc;		// half move clock;
	int mn;			// Move Number;
	int wkp;		// White King Position
	int bkp;		// Black King position
	
	boolean ckFlag = false, cqFlag = false, epFlag = false;
	boolean isLegal = false;
	
public GameState() {}
	
public GameState( boolean white, boolean wck, boolean wcq, boolean bck, boolean bcq,
							int ept, int hmc, int mn, int wkp, int bkp ) {

		this.white = white;
		this.wck = wck;
		this.wcq = wcq;
		this.bck = bck;
		this.bcq = bcq;
		this.ept = ept;
		this.hmc = hmc;
		this.mn = mn;
		this.wkp = wkp;
		this.bkp = bkp;
	}

	// constructor that builds this GameState object from a fen String
	public GameState( String fenStr ) {
	
		StringTokenizer fenTok = new StringTokenizer( fenStr, " " );
		String s;
		
		// build the position array
		int[] posn = Utils.toArray( fenTok.nextToken() );

		// side to move.
		white = fenTok.nextToken().equals("w");
		
		// parsing the castling wights field
		s = fenTok.nextToken();
		wck = s.contains("K");
		wcq = s.contains("Q");
		bck = s.contains("k");
		bcq = s.contains("q");
		
		// epTarget square is converted from algebraic to integer notation
		s = fenTok.nextToken();
		if( s.equals("-") ) {
			ept = -1;
		}
		else ept = Utils.toIN.get(s);
		
		// Sometimes fen string omits halfMoveClock and moveNum
		if( !fenTok.hasMoreTokens() ) {
			hmc = 0;
			mn = 1;
		}
		else {
			hmc = Integer.parseInt( fenTok.nextToken() );
			mn = Integer.parseInt( fenTok.nextToken() );
		}
		
		for( int sq=0; sq<64; sq++ ) {
			if( posn[sq] == WK ) wkp = sq;
			else if( posn[sq] == BK ) bkp = sq;
		}
	}

	// convert this GameState object to a fen string
	public String toString() {
	
		String s;
	
		StringBuilder sb = new StringBuilder();
		
		sb.append( (white ? "w" : "b") +" " );
		
		sb.append( wck ? "K" : "" );
		sb.append( wcq ? "Q" : "" );
		sb.append( bck ? "k" : "" );
		sb.append( bcq ? "q" : "" );
		sb.append( sb.substring(sb.length()-1).equals(" ") ? "- " : " " );
		
		sb.append( ((ept == -1) ? "-" : Utils.toAlg.get(ept)) +" " );
		sb.append( hmc +" " );
		sb.append( mn );
		
		return sb.toString();
	}
	
	public GameState clone() {
	
		return new GameState( white, wck, wcq, bck, bcq, ept, hmc, mn, wkp, bkp );

	}
	
}