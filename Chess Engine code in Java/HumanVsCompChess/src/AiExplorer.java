/*
  The AiExplorer class will contain all the methods for evaluating and selecting moves,
  as opposed to merely generating the moves, which is a function of MoveGen. In the Engine
  class you will find all the recursive tree methods such as negamax, alphabeta, quiescent
  search, evaluation functions, etc.
*/

import java.util.*;
import javax.swing.*;

public class AiExplorer implements Pieces {
	
/**********************************************************************************/

int nodes;
Output output;
int maxPly;
boolean think = false;
boolean abp = true;
boolean qs = true;
boolean pE = true;
boolean diags = false;
boolean pO, mO;

// arrays and variables used for generation of candidate moves.
int[][][] pList;
int[] np;

int[][][] mList;
int[] nm;

int[][][] cList;
int[] nc;

int[][][] capList;
int[] nCap;

int[] posn;

int[] fm;

int[][] PV;

/**********************************************************************************/
	
public AiExplorer( Output output ) {
	this.output = output;
}

/**********************************************************************************/

public void init ( int[] posn, int ply ) {

	this.posn = Arrays.copyOf(posn,64);
	
	pList = new int[ply+1][16][2];
	np = new int[ply+1];
	
	mList = new int[ply+1][100][3];
	nm = new int[ply+1];
	
	cList = new int[ply+1][25][5];
	nc = new int[ply+1];
	
	capList = new int[10][25][5];
	nCap = new int[10];
	
}

/**********************************************************************************/

public int[] getMove( GameState gs, int maxPly ) {

	fm = new int[3];

	this.maxPly = maxPly;
	think = true;
	nodes = 0;
	int alpha = -100000;
	int beta = 100000;
	int[] m;

	int eval = abp ? alphaBeta( gs, maxPly, alpha, beta ) : negaMax( gs, maxPly );
	
	output.setText( "eval = " +eval);
	output.append( "leaf nodes = " +nodes +"\n");
	
	return Arrays.copyOf( fm, 3 );
}

/**********************************************************************************/

int negaMax( GameState gs, int ply ) {
	
	if( !think || ply == 0 ) return evaluate(gs);
		
	int eval;
	int count=0;
	GameState gs1;
	int[] m;
	int maxEval = -100000;
	int p0, p1;
		
	moveGen( gs, ply );

	for( int i=0; i<nm[ply]; i++ ) {
		m = mList[ply][i];
		
		p0 = posn[ m[0] ]; p1 = posn[ m[1] ];

		gs1 = makeMove( gs, m );
		
		if( gs1.isLegal ) {
			count++;

			eval = -negaMax( gs1, ply-1 );

			unmakeMove( gs1, m, p0, p1 );

			if( eval > maxEval ) {
				maxEval = eval;
				if( ply == maxPly ) fm = Arrays.copyOf( m, 3 );
			}
		}
		else unmakeMove( gs1, m, p0, p1 );
	}
		
	// the chekmate value is based on the ply, so that 
	if( count == 0 ) return inCheck(gs) ? -(100000-(maxPly-ply)+1) : 0;

	return maxEval;
}

/**********************************************************************************/
	
int alphaBeta( GameState gs, int ply, int alpha, int beta ) {

	if( !think || (ply==0 && !qs) ) return evaluate(gs);
	if( ply == 0 ) return qSearch( gs, 0, alpha, beta );
		
	int eval;
	int count=0;
	GameState gs1;
	int[] m;
	int p0, p1;
		
	moveGen( gs, ply );

	// captures are processed first
	for( int i=0; i<nc[ply]; i++ ) {
		m = cList[ply][i];

		p0 = posn[m[0]]; p1 = posn[m[1]];
		gs1 = makeMove( gs, m );

		if( gs1.isLegal ) {
			count++;
			
			// the revised gamestate is passed in the recursion call.
			eval = -alphaBeta( gs1, ply-1, -beta, -alpha );
			
			unmakeMove( gs1, m, p0, p1 );

			if( eval >= beta ) {
				if( ply == maxPly ) fm = Arrays.copyOf(m,3);
				return beta;
			}
			if( eval > alpha ) {
				if( ply == maxPly ) fm = Arrays.copyOf(m,3);
				alpha = eval;
			}
		}
		else unmakeMove( gs1, m, p0, p1 );
	}

	for( int i=0; i<nm[ply]; i++ ) {
		m = mList[ply][i];

		p0 = posn[m[0]]; p1 = posn[m[1]];
		gs1 = makeMove( gs, m );

		if( gs1.isLegal ) {
			count++;
			
			// the revised gamestate is passed in the recursion call.
			eval = -alphaBeta( gs1, ply-1, -beta, -alpha );
			
			unmakeMove( gs1, m, p0, p1 );

			if( eval >= beta ) {
				if( ply == maxPly ) fm = Arrays.copyOf(m,3);
				return beta;
			}
			if( eval > alpha ) {
				if( ply == maxPly ) fm = Arrays.copyOf(m,3);
				alpha = eval;
			}
		}
		else unmakeMove( gs1, m, p0, p1 );
	}
		
	if( count == 0 ) {
		if( inCheck(gs) ) return -(100000-(maxPly-ply)+1);
		else return 0;
	}
		
	return alpha;
}

/**********************************************************************************/

private int qSearch( GameState gs, int ply, int alpha, int beta ) {

//	if( inCheck(gs) ) return alphaBeta( gs, 1, alpha, beta );

	GameState gs1;

	int eval = evaluate(gs);
	if( ply > 9 ) return eval;
	if( eval >= beta ) return beta;
	if( eval > alpha ) alpha = eval;
	
	capGen( gs, ply );
	int[] m;
	int p0, p1;
	
	for( int i=0; i<nCap[ply]; i++ ) {
	
		m = capList[ply][i];
		p0 = posn[m[0]]; p1 = posn[m[1]];
		
		gs1 = makeMove( gs, m );
		if( gs1.isLegal ) {

			eval = -qSearch( gs1, ply+1, -beta, -alpha );
			unmakeMove( gs1, m, p0, p1 );
			
			if( eval >= beta ) return beta;
			if( eval > alpha ) alpha = eval;
		}
		else unmakeMove( gs1, m, p0, p1 );
	}
	
	return alpha;
}

/**********************************************************************************/


public void unmakeMove( GameState gs1, int[] m, int p0, int p1 ) {

	posn[m[0]] = p0;
	posn[m[1]] = p1;
	
	if( gs1.ckFlag ) {
		if( gs1.white ) { posn[H8] = BR; posn[F8] = 0; }
		else { posn[H1] = WR; posn[F1] = 0; }
		return;
	}
	
	if( gs1.cqFlag ) {
		if( gs1.white ) { posn[A8] = BR; posn[D8] = 0; }
		else { posn[A1] = WR; posn[D1] = 0; }
		return;
	}
	
	if( gs1.epFlag ) {
		if( gs1.white ) posn[m[1]-8] = WP;
		else posn[m[1]+8] = BP;
	}
}

/**********************************************************************************/

	
// right now the evaluation function just calculates the material balance.

int evaluate( GameState gs ) {
	
	int sum = 0;
	for( int sq=0; sq<64; sq++ ) {
		sum += posn[sq];
	}
	nodes++;
	
	if( pE ) {
		for( int sq=0; sq<64; sq++ ) {

			switch( posn[sq] ) {

				case WP: sum += Data.wpv[sq]; break;
				case WN: sum += Data.wnv[sq]; break;
				case WB: sum += Data.wbv[sq]; break;
				case WR: sum += Data.wrv[sq]; break;
				case WQ: sum += Data.wqv[sq]; break;
				case WK: sum += Data.wkv[sq]; break;
				case BP: sum += Data.bpv[sq]; break;
				case BN: sum += Data.bnv[sq]; break;
				case BB: sum += Data.bbv[sq]; break;
				case BR: sum += Data.brv[sq]; break;
				case BQ: sum += Data.bqv[sq]; break;
				case BK: sum += Data.bkv[sq]; break;
			}
		}
	}
	
	return gs.white ? sum : -sum;
}

/**********************************************************************************/

boolean inCheck( GameState gs ) {

	if( gs.white ) return (isSquareAttackedW( gs, gs.wkp ));
	return (isSquareAttackedB( gs, gs.bkp ));
}
	
/**********************************************************************************/

// called to generate a list of candidate moves
public void moveGen( GameState gs, int ply ) {

	nm[ply] = 0;
	nc[ply] = 0;

	if( gs.white ) {
	
		for( int src=0; src<64; src++ ) {
			if( posn[src] > 0 ) {
				genMoves( gs, src, ply );
			}
		}
	}
	
	else {

		for( int src=0; src<64; src++ ) {
			if( posn[src] < 0 ) {
				genMoves( gs, src, ply );
			}
		}
	}
	
	int[] temp;
	
	// MVV-LVA move ordering on captures is done here.

	if( mO ) {
	for( int i=0; i < (nc[ply]-1); i++ ) {
		for( int j=i+1; j < nc[ply]; j++ ) {
	
			if( cList[ply][i][4] > cList[ply][j][4] ) continue;
			if( (cList[ply][i][4] == cList[ply][j][4]) && (cList[ply][i][3] < cList[ply][j][3]) ) continue;
			temp = cList[ply][i];
			cList[ply][i] = cList[ply][j];
			cList[ply][j] = temp;
		}
	}
	}
}

/**********************************************************************************/

void genMoves( GameState gs, int src, int ply ) {

	int piece;
	int r,c;
	int dest;
	
	piece = posn[src];
		
	switch( piece ) {

		case WP:	// white pawn
			for( int dst : Data.P[src] ) {
				
				// eliminate non-captures where the square is blocked.
				if( src%8 == dst%8 ) {
					if( posn[src-8] != 0) continue;
					if( posn[dst] != 0 ) continue;
				}	

				// eliminate diagonal moves where there is no capture.
				if( (src%8 != dst%8) ) {
					if( (posn[dst] >= 0) && (dst != gs.ept) ) continue;
				}

				// if promotion add four moves include promopiece
				if( src>7 && src<16 ) {	
					addMove(ply, gs, src, dst, WQ);
					addMove(ply, gs, src, dst, WN);
					addMove(ply, gs, src, dst, WR);
					addMove(ply, gs, src, dst, WB);
				}
				else addMove(ply, gs, src, dst, 0);
			}
		break;
			
		case BP:	// black pawn
			for( int dst : Data.p[src] ) {
				
				if( src%8 == dst%8 ) {
					if( posn[src+8] != 0 ) continue;
					if( posn[dst] != 0 ) continue;
				}

				if( (src%8 != dst%8) ) {
					if( (posn[dst] <= 0) && (dst != gs.ept) ) continue;
				}

				if( src>47 && src<56 ) {	// if promotion add four moves include promopiece
					addMove(ply, gs, src, dst, BQ);
					addMove(ply, gs, src, dst, BN);
					addMove(ply, gs, src, dst, BR);
					addMove(ply, gs, src, dst, BB);
				}
				else addMove(ply, gs, src, dst, 0);
			}
		break;
		
		case WK:	// white King
			for( int dst : Data.K[src] ) {
				
				if( posn[dst] > 0 ) continue;

				// if squares in the castling path are occupied, discard (kingside)
				if( src==E1 && dst==G1 ) {
					if( !gs.wck ) continue;
					if( (posn[F1] != 0) || (posn[G1] != 0) || (posn[H1] != WR) ) continue;
					if( isSquareAttackedW(gs,E1) || isSquareAttackedW(gs,F1) ) continue;
				}
							
				// if squares in the castling path are occupied, discard (queenside)
				else if( src==E1 && dst==C1 ) {
					if( !gs.wcq ) continue;
					if( (posn[D1] != 0) || (posn[C1] != 0) || (posn[B1] != 0) ) continue;
					if( posn[A1] != WR ) continue;
					if( isSquareAttackedW(gs,E1) || isSquareAttackedW(gs,D1) ) continue;
				}

				addMove(ply, gs, src, dst, 0);
			}
			break;
			
		case BK:	// black king
			for( int dst : Data.k[src] ) {
				
				if( posn[dst] < 0 ) continue;
			
				// if squares in the castling path are occupied, discard (kingside)
				if( src==E8 && dst==G8 ) {
					if(!gs.bck) continue;
					if ( (posn[F8] != 0) || (posn[G8] != 0) || (posn[H8] != BR) ) continue;
					if( isSquareAttackedB(gs,E8) || isSquareAttackedB(gs,F8) ) continue;
				}
							
				// if squares in the castling path are occupied, discard (queenside)
				else if( src==E8 && dst==C8 ) {
					if(!gs.bcq) continue;
					if( (posn[D8] != 0) || (posn[C8] != 0) || (posn[B8] != 0) ) continue;
					if( posn[A8] != BR ) continue;
					if( isSquareAttackedB(gs,E8) || isSquareAttackedB(gs,D8) ) continue;
				}

				addMove(ply, gs, src, dst, 0);
			}
			break;

		case WN:	// white knight
		case BN:	// black knight
			
			for( int dst : Data.N[src] ) {
				if( piece * posn[dst] > 0 ) continue;
				addMove(ply, gs, src, dst, 0);
			}
			break;

		case WQ:
		case BQ:
		case WR:
		case BR:
			
			r = src/8; c = src%8;
			while( ++r < 8 ) {	// movement forward on a file
				dest = 8*r + c;
				if( addMove(ply, gs, src, dest, 0) ) break;
			}
					
			r = src/8; c = src%8;
			while( --r >= 0 ) { // movement backwards on a file
				dest = 8*r + c;
				if( addMove(ply, gs, src, dest, 0) ) break;
			}
					
			r = src/8; c = src%8;
			while( ++c < 8 ) {	// movment along a rank towards the h-file
				dest = 8*r + c;
				if( addMove(ply, gs, src, dest, 0) ) break;
			}
					
			r = src/8; c = src%8;
			while( --c >= 0 ) {	// movement along a rank towards the a-file
				dest = 8*r + c;
				if( addMove(ply, gs, src, dest, 0) ) break;
			}
				
			if( (piece == WR) || (piece == BR) ) break;
				
		case WB:
		case BB:

			r = src/8; c = src%8;
			while( ++r<8 && ++c<8 ) {	// top left to bottom right
				dest = 8*r + c;
				if( addMove(ply, gs, src, dest, 0) ) break;
			}

			r = src/8; c = src%8;
			while( --r>=0 && ++c<8 ) {	// bottom left to top right
				dest = 8*r + c;
				if( addMove(ply, gs, src, dest, 0) ) break;
			}
			
			r = src/8; c = src%8;
			while( ++r<8 && --c>=0 ) {	// top right to bottom left
				dest = 8*r + c;
				if( addMove(ply, gs, src, dest, 0) ) break;
			}

			r = src/8; c = src%8;
			while( --r>=0 && --c>=0 ) {	// bottom right to top left
				dest = 8*r + c;
				if( addMove(ply, gs, src, dest, 0) ) break;
			}
			
			break;
			
	}

}

/**********************************************************************************/

// adds the candidate move to the moveList array
boolean addMove( int ply, GameState gs, int src, int dest, int promo ) {
	
	
	if( posn[src] * posn[dest] >0 ) return true;


	if( !mO  ) {

		mList[ply][nm[ply]][0] = src;
		mList[ply][nm[ply]][1] = dest;
		mList[ply][nm[ply]++][2] = promo;
	}
	else {
		if( posn[src]*posn[dest] < 0 ) {
			cList[ply][nc[ply]][0] = src;
			cList[ply][nc[ply]][1] = dest;
			cList[ply][nc[ply]][2] = promo;
			cList[ply][nc[ply]][3] = gs.white ? posn[src] : -posn[src];
			cList[ply][nc[ply]++][4] = (promo==0) ? (gs.white ? -posn[dest] : posn[dest]) : (gs.white ? promo : -promo);
		}
		else {
			mList[ply][nm[ply]][0] = src;
			mList[ply][nm[ply]][1] = dest;
			mList[ply][nm[ply]++][2] = promo;
		}
	}
	if(ply == 0) Board.posMov[dest] = 1;
	return posn[dest] == 0 ? false : true;
	
}

/**********************************************************************************/

// called to generate a list of candidate captures
public void capGen( GameState gs, int ply ) {

	nCap[ply] = 0;

	if( gs.white ) {
	
		for( int src=0; src<64; src++ ) {
			if( posn[src] > 0 ) {
				genCaps( gs, src, ply );
			}
		}
	}
	
	else {

		for( int src=0; src<64; src++ ) {
			if( posn[src] < 0 ) {
				genCaps( gs, src, ply );
			}
		}
	}
	
	int[] temp;
	
	// MVV-LVA move ordering on captures is done here.

	if( mO ) {
	for( int i=0; i < (nCap[ply]-1); i++ ) {
		for( int j=i+1; j < nCap[ply]; j++ ) {
	
			if( capList[ply][i][4] > capList[ply][j][4] ) continue;
			if( (capList[ply][i][4] == capList[ply][j][4]) && (capList[ply][i][3] < capList[ply][j][3]) ) continue;
			temp = capList[ply][i];
			capList[ply][i] = capList[ply][j];
			capList[ply][j] = temp;
		}
	}
	}
}



/**********************************************************************************/

void genCaps( GameState gs, int src, int ply ) {

	int piece;
	boolean capture, enPassant;
	int r,c;
	int dest;
	
	piece = posn[src];
		
	switch( piece ) {

		case WP:	// white pawn
			for( int dst : Data.P[src] ) {
				
				if( src%8 == dst%8 ) {
					if( dst/8 > 0 ) continue;
					if( posn[src-8] != 0 ) continue;
					if( posn[dst] != 0 ) continue;
				}

				if( (src%8 != dst%8) ) {
					if( (posn[dst] >= 0 ) && (dst != gs.ept) ) continue;
				}

				if( dst>=A8 && dst<=H8 ) {	// if promotion add four moves include promopiece
					addCap(ply, gs, src, dst, BQ);
					addCap(ply, gs, src, dst, BN);
					addCap(ply, gs, src, dst, BR);
					addCap(ply, gs, src, dst, BB);
				}
				else addCap(ply, gs, src, dst, 0);
			}
		break;
			
		case BP:	// black pawn
			for( int dst : Data.p[src] ) {
				
				if( src%8 == dst%8 ) {
					if( dst/8 < 7 ) continue;
					if( posn[src+8] != 0 ) continue;
					if( posn[dst] != 0 ) continue;
				}

				if( (src%8 != dst%8) ) {
					if( (posn[dst] <= 0 ) && (dst != gs.ept) ) continue;
				}

				if( dst>=A1 && dst<=H1 ) {	// if promotion add four moves include promopiece
					addCap(ply, gs, src, dst, BQ);
					addCap(ply, gs, src, dst, BN);
					addCap(ply, gs, src, dst, BR);
					addCap(ply, gs, src, dst, BB);
				}
				else addCap(ply, gs, src, dst, 0);
			}
		break;
		
		case WK:	// white King
			for( int dst : Data.K[src] ) {
				
				if( posn[dst] >= 0 ) continue;
				if( src==E1 && dst==G1 ) continue;
				if( src==E1 && dst==C1 ) continue;

				addCap(ply, gs, src, dst, 0);
			}
			break;
			
		case BK:	// black king
			for( int dst : Data.k[src] ) {
				
				if( posn[dst] <= 0 ) continue;
				if( src==E8 && dst==G8 ) continue;
				if( src==E8 && dst==C8 ) continue;

				addCap(ply, gs, src, dst, 0);
			}
			break;

		case WN:	// white knight
		case BN:	// black knight
			
			for( int dst : Data.N[src] ) {
				if( piece * posn[dst] < 0 ) addCap(ply, gs, src, dst, 0);
			}
			break;

		case WQ:
		case BQ:
		case WR:
		case BR:
			
			r = src/8; c = src%8;
			while( ++r < 8 ) {	// movement forward on a file
				dest = 8*r+c;
				if(posn[dest]==0) continue;
				if(posn[src]*posn[dest]<0) addCap(ply, gs, src, dest, 0);
				break;
			}
					
			r = src/8; c = src%8;
			while( --r >= 0 ) { // movement backwards on a file
				dest = 8*r+c;
				if(posn[dest]==0) continue;
				if(posn[src]*posn[dest]<0) addCap(ply, gs, src, dest, 0);
				break;
			}
					
			r = src/8; c = src%8;
			while( ++c < 8 ) {	// movment along a rank towards the h-file
				dest = 8*r+c;
				if(posn[dest]==0) continue;
				if(posn[src]*posn[dest]<0) addCap(ply, gs, src, dest, 0);
				break;
			}
					
			r = src/8; c = src%8;
			while( --c >= 0 ) {	// movement along a rank towards the a-file
				dest = 8*r+c;
				if(posn[dest]==0) continue;
				if(posn[src]*posn[dest]<0) addCap(ply, gs, src, dest, 0);
				break;
			}
				
			if( (piece == WR) || (piece == BR) ) break;
				
		case WB:
		case BB:

			r = src/8; c = src%8;
			while( ++r<8 && ++c<8 ) {	// top left to bottom right
				dest = 8*r+c;
				if(posn[dest]==0) continue;
				if(posn[src]*posn[dest]<0) addCap(ply, gs, src, dest, 0);
				break;
			}

			r = src/8; c = src%8;
			while( --r>=0 && ++c<8 ) {	// bottom left to top right
				dest = 8*r+c;
				if(posn[dest]==0) continue;
				if(posn[src]*posn[dest]<0) addCap(ply, gs, src, dest, 0);
				break;
			}
			
			r = src/8; c = src%8;
			while( ++r<8 && --c>=0 ) {	// top right to bottom left
				dest = 8*r+c;
				if(posn[dest]==0) continue;
				if(posn[src]*posn[dest]<0) addCap(ply, gs, src, dest, 0);
				break;
			}

			r = src/8; c = src%8;
			while( --r>=0 && --c>=0 ) {	// bottom right to top left
				dest = 8*r+c;
				if(posn[dest]==0) continue;
				if(posn[src]*posn[dest]<0) addCap(ply, gs, src, dest, 0);
				break;
			}
			
			break;
			
	}
}

/**********************************************************************************/

void addCap( int ply, GameState gs, int src, int dest, int pP ) {

	capList[ply][nCap[ply]][0] = src;
	capList[ply][nCap[ply]][1] = dest;
	capList[ply][nCap[ply]][2] = pP;
	capList[ply][nCap[ply]][3] = gs.white ? posn[src] : -posn[src];
	capList[ply][nCap[ply]++][4] = (pP==0) ? (gs.white ? -posn[dest] : posn[dest]) : (gs.white ? pP : -pP);
	
}

/**********************************************************************************/

// The final legality check and update of game state variables is done here.

GameState makeMove( GameState gs, int[] m ) {

	int src = m[0];
	int dest = m[1];

	GameState gs1 = gs.clone();
	gs1.isLegal = false;
	
	gs1.ckFlag = false; gs1.cqFlag = false; gs1.epFlag = false;
	
	int piece = posn[src];
	boolean cap = posn[dest] != 0;
	
	switch( piece ) {
	
		case BR:
			if( src==A8 && gs1.bcq ) gs1.bcq = false;
			if( src==H8 && gs1.bck ) gs1.bck = false;
			gs1.hmc = cap ? 0 : gs1.hmc+1;
			gs1.ept = -1;
			break;

		case WR:
			if( src==A1 && gs1.wcq ) gs1.wcq = false;
			if( src==H1 && gs1.wck ) gs1.wck = false;
			gs1.hmc = cap ? 0 : gs1.hmc+1;
			gs1.ept = -1;
			break;
			
		case WQ:
		case BQ:
		case WB:
		case BB:
		case WN:
		case BN:
			gs1.hmc = cap ? 0 : gs1.hmc+1;
			gs1.ept = -1;
			break;

		case WP:
			if( (src-dest) == 16 ) gs1.ept = dest + 8;
			else {
				if( dest == gs1.ept ) { posn[dest+8] = 0; gs1.epFlag = true; }
				gs1.ept = -1;
			}
			gs1.hmc = 0;
			break;
		
		case BP:
			if( (dest-src) == 16 ) gs1.ept = dest - 8;
			else {
				if( dest == gs1.ept ) { posn[dest-8] = 0; gs1.epFlag = true; }
				gs1.ept = -1;
			}
			gs1.hmc = 0;
			break;
			
		case WK:
			if( (src==E1) && (dest==G1) ) {
				posn[F1] = WR;
				posn[H1] = 0;
				gs1.ckFlag = true;
			}
			else if( (src==E1) && (dest==C1) ) {
				posn[D1] = WR;
				posn[A1] = 0;
				gs1.cqFlag = true;
			}
			
			gs1.wkp = dest;
			if( gs1.wck ) gs1.wck = false;
			if( gs1.wcq ) gs1.wcq = false;
			gs1.hmc = cap ? 0 : gs1.hmc+1;
			gs1.ept = -1;
			break;
		
		case BK:
			if( (src==E8) && (dest==G8) ) {
				posn[F8] = BR;
				posn[H8] = 0;
				gs1.ckFlag = true;
			}
			else if( (src==E8) && (dest==C8) ) {
				posn[D8] = BR;
				posn[A8] = 0;
				gs1.cqFlag = true;
			}

			gs1.bkp = dest;
			if( gs1.bck ) gs1.bck = false;
			if( gs1.bcq ) gs1.bcq = false;
			gs1.hmc = cap ? 0 : gs1.hmc+1;
			gs1.ept = -1;
	}

	// if necessary, revoke castling privelege if rook captured,
	if( (dest == A8) && gs1.bcq ) gs1.bcq = false;
	else if( (dest == H8 ) && gs1.bck ) gs1.bck = false;
	else if( (dest == A1) && gs1.wcq ) gs1.wcq = false;
	else if( (dest == H1) && gs1.wck ) gs1.wck = false;
	
	posn[src] = 0;
	posn[dest] = (m[2] != 0) ? m[2] : piece;
	
	gs1.white = !gs1.white;
	if(gs1.white) gs1.mn++;

	if( !((gs1.white && isSquareAttackedB(gs1, gs1.bkp)) || (!gs1.white && isSquareAttackedW(gs1, gs1.wkp))) ) {
		gs1.isLegal = true;
	}
	
	return gs1;
}

/**********************************************************************************/

// is white under attack on square sq

boolean isSquareAttackedW( GameState gs, int sq ) {

	int p, r, c;
	
	// forward along file
	r = sq / 8;	c = sq % 8;
	while( --r >= 0 ) {
		p = posn[8*r + c];
		if( p == 0 ) continue;
		if( p==BR || p==BQ ) return true;
		break;
	}
	
	// forward left along diagonal
	r = sq / 8;	c = sq % 8;
	while( (--r >= 0) && (--c >= 0) ) {
		p = posn[8*r + c];
		if( p == 0 ) continue;
		if( p==BB || p==BQ ) return true;
		break;
	}

	// forward right along diagonal
	r = sq / 8;	c = sq % 8;
	while( (--r >= 0) && (++c < 8) ) {
		p = posn[8*r + c];
		if( p == 0 ) continue;
		if( p==BB || p==BQ ) return true;
		break;
	}
	
	// atack by knight
	for( int sq1 : Data.N[sq] ) {
		if( posn[sq1] == BN ) return true;
	}
	
	// to left along rank
	r = sq / 8;	c = sq % 8;
	while( --c >= 0 ) {
		p = posn[8*r + c];
		if( p == 0 ) continue;
		if( p==BR || p==BQ ) return true;
		break;
	}

	// to right along rank
	r = sq / 8;	c = sq % 8;
	while( ++c < 8 ) {
		p = posn[8*r + c];
		if( p == 0 ) continue;
		if( p==BR || p==BQ ) return true;
		break;
	}
	
	// backward along file
	r = sq / 8;	c = sq % 8;
	while( ++r < 8 ) {
		p = posn[8*r + c];
		if( p == 0 ) continue;
		if( p==BR || p==BQ ) return true;
		break;
	}
	
	// backward left along diagonal
	r = sq / 8;	c = sq % 8;
	while( (++r < 8) && (--c >= 0) ) {
		p = posn[8*r + c];
		if( p == 0 ) continue;
		if( p==BB || p==BQ ) return true;
		break;
	}
	
	// backward right along diagonal
	r = sq / 8;	c = sq % 8;
	while( (++r < 8) && (++c < 8) ) {
		p = posn[8*r + c];
		if( p == 0 ) continue;
		if( p==BB || p==BQ ) return true;
		break;
	}
	
	// attack by pawn
	r = sq / 8;	c = sq % 8;
	if( r > 1 ) {
		if( c==0 ) {
			if( posn[sq-7] == BP ) return true;
		}
		else if ( c==7 ) {
			if( posn[sq-9] == BP ) return true;
		}
		else if( (posn[sq-7] == BP)  ||  (posn[sq-9] == BP) ) return true;
	}
	
	// attack by king
	int bkr = gs.bkp/8, bkc = gs.bkp%8;
	if( (Math.abs(r - bkr) <= 1) && (Math.abs(c - bkc) <= 1) ) return true;
	
	return false;
}

/**********************************************************************************/

// is black under attack on square sq

boolean isSquareAttackedB( GameState gs, int sq ) {

	int p, r, c;
	
	// forward along file
	r = sq / 8;	c = sq % 8;
	while( ++r < 8 ) {
		p = posn[8*r + c];
		if( p == 0 ) continue;
		if( p==WR || p==WQ ) return true;
		break;
	}
	
	// forward left along diagonal
	r = sq / 8;	c = sq % 8;
	while( (++r < 8) && (--c >= 0) ) {
		p = posn[8*r + c];
		if( p == 0 ) continue;
		if( p==WB || p==WQ ) return true;
		break;
	}

	// forward right along diagonal
	r = sq / 8;	c = sq % 8;
	while( (++r < 8) && (++c < 8) ) {
		p = posn[8*r + c];
		if( p == 0 ) continue;
		if( p==WB || p==WQ ) return true;
		break;
	}
	
	// atack by knight
	for( int sq1 : Data.N[sq] ) {
		if( posn[sq1] == WN ) return true;
	}
	
	// to left along rank
	r = sq / 8;	c = sq % 8;
	while( --c >= 0 ) {
		p = posn[8*r + c];
		if( p == 0 ) continue;
		if( p==WR || p==WQ ) return true;
		break;
	}

	// to right along rank
	r = sq / 8;	c = sq % 8;
	while( ++c < 8 ) {
		p = posn[8*r + c];
		if( p == 0 ) continue;
		if( p==WR || p==WQ ) return true;
		break;
	}
	
	// backward along file
	r = sq / 8;	c = sq % 8;
	while( --r >= 0 ) {
		p = posn[8*r + c];
		if( p == 0 ) continue;
		if( p==WR || p==WQ ) return true;
		break;
	}
	
	// backward left along diagonal
	r = sq / 8;	c = sq % 8;
	while( (--r >= 0) && (--c >= 0) ) {
		p = posn[8*r + c];
		if( p == 0 ) continue;
		if( p==WB || p==WQ ) return true;
		break;
	}
	
	// backward right along diagonal
	r = sq / 8;	c = sq % 8;
	while( (--r >= 0) && (++c < 8) ) {
		p = posn[8*r + c];
		if( p == 0 ) continue;
		if( p==WB || p==WQ ) return true;
		break;
	}
	
	// attack by pawn
	r = sq / 8;	c = sq % 8;
	if( r < 6 ) {
		if( c==0 ) {
			if( posn[sq+9] == WP ) return true;
		}
		else if ( c==7 ) {
			if( posn[sq+7] == WP ) return true;
		}
		else if( (posn[sq+7] == WP)  ||  (posn[sq+9] == WP) ) return true;
	}
	
	// attack by king
	int wkr = gs.wkp/8, wkc = gs.wkp%8;
	if( (Math.abs(r - wkr) <= 1) && (Math.abs(c - wkc) <= 1) ) return true;
	
	return false;
}

/**********************************************************************************/

void buildPieceList( GameState gs, int ply ) {

	np[ply] = 0;

	if( gs.white ) {
		for( int src=0; src<64; src++ ) {
			if( posn[src] > 0 ) {
				pList[ply][np[ply]][0] = src;
				pList[ply][np[ply]++][1] = posn[src];
			}
		}
	}
	else {
		for( int src=0; src<64; src++ ) {
			if( posn[src] < 0 ) {
				pList[ply][np[ply]][0] = src;
				pList[ply][np[ply]++][1] = posn[src];
			}
		}
	}		
}

/**********************************************************************************/

}
		
		