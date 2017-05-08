import java.util.*;

public class Utils implements Pieces {

// mapping of piece repreentation
public static HashMap< Character, Integer > toInt;

// mapping of piece representation
public static HashMap< Integer, Character > toChar;

// mapping of square representation from algebraic to integer (0-63)
public static TreeMap< String, Integer > toIN;

// mapping of square representation from integer (0-63) to algebraic.
public static TreeMap< Integer, String > toAlg;

// mapping of piece representation to piece description.
public static HashMap< Integer, String > pieceType;

// mapping of epTarget squares to Zobrist randoms
public static TreeMap< Integer, Long > eptZ;


static final long SEED = 2095199257273629677L;
static Random rand;

static long[][] z64;
static long zw;
static long zwck;
static long zwcq;
static long zbck;
static long zbcq;
static long eptZa6;
static long eptZb6;
static long eptZc6;
static long eptZd6;
static long eptZe6;
static long eptZf6;
static long eptZg6;
static long eptZh6;
static long eptZa3;
static long eptZb3;
static long eptZc3;
static long eptZd3;
static long eptZe3;
static long eptZf3;
static long eptZg3;
static long eptZh3;


public static long zKey( GameState gs, int[] posn ) {

	long z = 0L;
	
	for( int i=0; i<64; i++ ) {
	
		switch(posn[i]) {
		
			case WP: z = z ^ z64[zWP][i]; break;
			case BP: z = z ^ z64[zBP][i]; break;
			case WN: z = z ^ z64[zWN][i]; break;
			case BN: z = z ^ z64[zBN][i]; break;
			case WB: z = z ^ z64[zWB][i]; break;
			case BB: z = z ^ z64[zBB][i]; break;
			case WR: z = z ^ z64[zWR][i]; break;
			case BR: z = z ^ z64[zBR][i]; break;
			case WQ: z = z ^ z64[zWQ][i]; break;
			case BQ: z = z ^ z64[zBQ][i]; break;
			case WK: z = z ^ z64[zWK][i]; break;
			case BK: z = z ^ z64[zBK][i];
		}
	}

	if( gs.white )	z = z ^ zw;

	if( gs.wck )	z = z ^ zwck;
	if( gs.wcq )	z = z ^ zwcq;
	if( gs.bck )	z = z ^ zbck;
	if( gs.bcq )	z = z ^ zbcq;
	
	if( gs.ept != -1 ) z = z ^ eptZ.get(gs.ept);
	
	return z;
}



// This method initializes the Maps It is called from the Table Constructor
public static void initialize() {

	rand = new Random(SEED);

	z64 = new long[12][64];
	for( int i = 0; i<12; i++ ) {
		for( int j=0; j<12; j++ ) {
			z64[i][j] = rand.nextLong();
		}
	}
	
	zw = rand.nextLong();
	zwck = rand.nextLong();
	zwcq = rand.nextLong();
	zbck = rand.nextLong();
	zbcq = rand.nextLong();
	eptZa6 = rand.nextLong();
	eptZb6 = rand.nextLong();
	eptZc6 = rand.nextLong();
	eptZd6 = rand.nextLong();
	eptZe6 = rand.nextLong();
	eptZf6 = rand.nextLong();
	eptZg6 = rand.nextLong();
	eptZh6 = rand.nextLong();
	eptZa3 = rand.nextLong();
	eptZb3 = rand.nextLong();
	eptZc3 = rand.nextLong();
	eptZd3 = rand.nextLong();
	eptZe3 = rand.nextLong();
	eptZf3 = rand.nextLong();
	eptZg3 = rand.nextLong();
	eptZh3 = rand.nextLong();
	
	eptZ = new TreeMap<Integer, Long>();
	eptZ.put( 16, eptZa6 );
	eptZ.put( 17, eptZb6 );
	eptZ.put( 18, eptZc6 );
	eptZ.put( 19, eptZd6 );
	eptZ.put( 20, eptZe6 );
	eptZ.put( 21, eptZf6 );
	eptZ.put( 22, eptZg6 );
	eptZ.put( 23, eptZh6 );
	eptZ.put( 40, eptZa3 );
	eptZ.put( 41, eptZb3 );
	eptZ.put( 42, eptZc3 );
	eptZ.put( 43, eptZd3 );
	eptZ.put( 44, eptZe3 );
	eptZ.put( 45, eptZf3 );
	eptZ.put( 46, eptZg3 );
	eptZ.put( 47, eptZh3 );
	
	pieceType = new HashMap< Integer, String >();
	pieceType.put( WK, "white king" );
	pieceType.put( BK, "black king" );
	pieceType.put( WQ, "white queen" );
	pieceType.put( BQ, "black queen" );
	pieceType.put( WR, "white rook" );
	pieceType.put( BR, "black rook" );
	pieceType.put( WB, "white bishop" );
	pieceType.put( BB, "black bishop" );
	pieceType.put( WN, "white knight" );
	pieceType.put( BN, "black knight" );
	pieceType.put( WP, "white pawn" );
	pieceType.put( BP, "black pawn" );
	pieceType.put( 0, "empty square" );


	toInt = new HashMap< Character, Integer >();
	toInt.put( 'k', BK );
	toInt.put( 'K', WK );
	toInt.put( 'q', BQ );
	toInt.put( 'Q', WQ );
	toInt.put( 'r', BR );
	toInt.put( 'R', WR );
	toInt.put( 'b', BB );
	toInt.put( 'B', WB );
	toInt.put( 'n', BN );
	toInt.put( 'N', WN );
	toInt.put( 'p', BP );
	toInt.put( 'P', WP );
	toInt.put( '1', 0 );
	
	toChar = new HashMap< Integer, Character >();
	toChar.put( BK, 'k' );
	toChar.put( WK, 'K' );
	toChar.put( BQ, 'q' );
	toChar.put( WQ, 'Q' );
	toChar.put( BR, 'r' );
	toChar.put( WR, 'R' );
	toChar.put( BB, 'b' );
	toChar.put( WB, 'B' );
	toChar.put( BN, 'n' );
	toChar.put( WN, 'N' );
	toChar.put( BP, 'p' );
	toChar.put( WP, 'P' );
	toChar.put( 0, '1' );
	
	toIN = new TreeMap< String, Integer >();
	toIN.put( "a1", 56 );
	toIN.put( "a2", 48 );
	toIN.put( "a3", 40 );
	toIN.put( "a4", 32 );
	toIN.put( "a5", 24 );
	toIN.put( "a6", 16 );
	toIN.put( "a7", 8 );
	toIN.put( "a8", 0 );
	toIN.put( "b1", 57 );
	toIN.put( "b2", 49 );
	toIN.put( "b3", 41 );
	toIN.put( "b4", 33 );
	toIN.put( "b5", 25 );
	toIN.put( "b6", 17 );
	toIN.put( "b7", 9 );
	toIN.put( "b8", 1 );
	toIN.put( "c1", 58 );
	toIN.put( "c2", 50 );
	toIN.put( "c3", 42 );
	toIN.put( "c4", 34 );
	toIN.put( "c5", 26 );
	toIN.put( "c6", 18 );
	toIN.put( "c7", 10 );
	toIN.put( "c8", 2 );
	toIN.put( "d1", 59 );
	toIN.put( "d2", 51 );
	toIN.put( "d3", 43 );
	toIN.put( "d4", 35 );
	toIN.put( "d5", 27 );
	toIN.put( "d6", 19 );
	toIN.put( "d7", 11 );
	toIN.put( "d8", 3 );
	toIN.put( "e1", 60 );
	toIN.put( "e2", 52 );
	toIN.put( "e3", 44 );
	toIN.put( "e4", 36 );
	toIN.put( "e5", 28 );
	toIN.put( "e6", 20 );
	toIN.put( "e7", 12 );
	toIN.put( "e8", 4 );
	toIN.put( "f1", 61 );
	toIN.put( "f2", 53 );
	toIN.put( "f3", 45 );
	toIN.put( "f4", 37 );
	toIN.put( "f5", 29 );
	toIN.put( "f6", 21 );
	toIN.put( "f7", 13 );
	toIN.put( "f8", 5 );
	toIN.put( "g1", 62 );
	toIN.put( "g2", 54 );
	toIN.put( "g3", 46 );
	toIN.put( "g4", 38 );
	toIN.put( "g5", 30 );
	toIN.put( "g6", 22 );
	toIN.put( "g7", 14 );
	toIN.put( "g8", 6 );
	toIN.put( "h1", 63 );
	toIN.put( "h2", 55 );
	toIN.put( "h3", 47 );
	toIN.put( "h4", 39 );
	toIN.put( "h5", 31 );
	toIN.put( "h6", 23 );
	toIN.put( "h7", 15 );
	toIN.put( "h8", 7 );
	
	toAlg = new TreeMap< Integer, String >();
	toAlg.put( 0, "a8" );
	toAlg.put( 1, "b8" );
	toAlg.put( 2, "c8" );
	toAlg.put( 3, "d8" );
	toAlg.put( 4, "e8" );
	toAlg.put( 5, "f8" );
	toAlg.put( 6, "g8" );
	toAlg.put( 7, "h8" );
	toAlg.put( 8, "a7" );
	toAlg.put( 9, "b7" );
	toAlg.put( 10, "c7" );
	toAlg.put( 11, "d7" );
	toAlg.put( 12, "e7" );
	toAlg.put( 13, "f7" );
	toAlg.put( 14, "g7" );
	toAlg.put( 15, "h7" );
	toAlg.put( 16, "a6" );
	toAlg.put( 17, "b6" );
	toAlg.put( 18, "c6" );
	toAlg.put( 19, "d6" );
	toAlg.put( 20, "e6" );
	toAlg.put( 21, "f6" );
	toAlg.put( 22, "g6" );
	toAlg.put( 23, "h6" );
	toAlg.put( 24, "a5" );
	toAlg.put( 25, "b5" );
	toAlg.put( 26, "c5" );
	toAlg.put( 27, "d5" );
	toAlg.put( 28, "e5" );
	toAlg.put( 29, "f5" );
	toAlg.put( 30, "g5" );
	toAlg.put( 31, "h5" );
	toAlg.put( 32, "a4" );
	toAlg.put( 33, "b4" );
	toAlg.put( 34, "c4" );
	toAlg.put( 35, "d4" );
	toAlg.put( 36, "e4" );
	toAlg.put( 37, "f4" );
	toAlg.put( 38, "g4" );
	toAlg.put( 39, "h4" );
	toAlg.put( 40, "a3" );
	toAlg.put( 41, "b3" );
	toAlg.put( 42, "c3" );
	toAlg.put( 43, "d3" );
	toAlg.put( 44, "e3" );
	toAlg.put( 45, "f3" );
	toAlg.put( 46, "g3" );
	toAlg.put( 47, "h3" );
	toAlg.put( 48, "a2" );
	toAlg.put( 49, "b2" );
	toAlg.put( 50, "c2" );
	toAlg.put( 51, "d2" );
	toAlg.put( 52, "e2" );
	toAlg.put( 53, "f2" );
	toAlg.put( 54, "g2" );
	toAlg.put( 55, "h2" );
	toAlg.put( 56, "a1" );
	toAlg.put( 57, "b1" );
	toAlg.put( 58, "c1" );
	toAlg.put( 59, "d1" );
	toAlg.put( 60, "e1" );
	toAlg.put( 61, "f1" );
	toAlg.put( 62, "g1" );
	toAlg.put( 63, "h1" );
}

/*******************************************************************/

// convert an int[64] position array to the position component of a fen string

public static String toFen( int[] posn ) {

	char p; int piece;
	StringBuilder sb = new StringBuilder();

	for( int r=0; r<8; r++ ) {
		for( int c=0; c<8; c++ ) {
			piece = posn[8*r+c];
			p = toChar.get(piece);
			sb.append(p);
		}
		sb.append("/");
	}

	int j;
	for( int i=0; i<sb.length(); i++ ) {
		if( sb.charAt(i) != '1' ) continue;
		j=0; while( sb.charAt( i+(++j) ) == '1' );
		sb.replace( i, i+j, "" +j );
	}
	
	sb.deleteCharAt( sb.length()-1 );
	
	return sb.toString();
}

/*******************************************************************/

// converts the position component of a fen String to a position array

public static int[] toArray( String fen ) {

	int[] temp = new int[64];
	String s; char p; int index = 0; int n;
	StringTokenizer fenTok = new StringTokenizer( fen, "/" );
	
	for( int r = 0; r<8; r++ ) {
	
		s = fenTok.nextToken();

		for( int i=0; i<s.length(); i++ ) {
			p = s.charAt(i);
			if( Character.isLetter(p) ) {
				temp[index++] = toInt.get(p);
			}
			else {
				n = p-48;
				for( int j=0; j<n; j++ ) {
					temp[index++] = toInt.get('1');
				}
			}
		}
	}

	return temp;
}

/*******************************************************************/

// called to verify the legality of a move attempted by drag and drop in the GUI.

public static boolean isLegal( int src, int dest ) {

	return true;
}

/*******************************************************************/
}