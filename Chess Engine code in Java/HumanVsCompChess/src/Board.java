/*
  The Board class is responsible for graphically depicting the board and
  pieces on the screen. The drawing surface is a JPanel. The primitive shapes
  were created by using draw commands on the graphics contexts of blank transparent
  BufferedImages. The BuffereImages were then darwn to the graphics context of the
  JPanel. This strategy was chosen to facilitate switching between "hand drawn"
  pieces and external loaded image files.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class Board extends JPanel implements Pieces {

int[] posn;
public static int [] posMov = new int[65];
private static final Color light = Color.white;
private static final Color dark = Color.gray;

// width & height of board, and square size.
int w, h, ss;

boolean invert = false;

/*
  Variables to support the painting of a piece being dragged. Since a piece being dragged
  is not part of the position array, paintComponent must be flagged to look elsewhere for
  information. xDrag & yDrag is the top left corner of the virtual square that is moved along
  with the piece. We need this because pieces are always painted relative to the occupied square.
*/
int pieceMoved;
boolean isDrag = false;
int xDrag, yDrag;

// Piece objects to support different piece sets.
Piece wk, wq, wb, wn, wr, wp, bk, bq, bb, bn, br, bp;

/**********************************************************************************/

public Board( boolean web, int w, int h ) {

	setPreferredSize( new Dimension(w,h) );
	
	posn = new int[64];

	this.w = w;
	this.h = h;
	this.ss = w/8;
	
	if( web ) buildPieceSetW();
	else buildPieceSet();	// initiates building the piece set.
}

/**********************************************************************************/

/**
  The paint() method is responsible for redrawing the board. The x and y variables
  represent the top left point of the square on which a pieces is being drawn.
 */

public void paintComponent( Graphics g ) {

	super.paintComponent(g);

	int p;
	int x, y;

	// loop through the position array, painting the square, then drawing the
	// image for the piece if the position array element is non zero.
	for( int r=0; r<8; r++ ) {
		for( int c=0; c<8; c++ ) {
		
			x = c*ss; y = r*ss;
			if(posMov[8*r+c] == 1) g.setColor(Color.yellow);
			else g.setColor( (r+c)%2 == 0 ? light : dark );
			g.fillRect( c*ss, r*ss, ss, ss );
		}
	}
	
	for( int r=0; r<8; r++ ) {
		for( int c=0; c<8; c++ ) {
		
			if( invert ) {
				x = ss*( 7 - c );
				y = ss*( 7 - r );
			}
			else {
				x = ss*c;
				y = ss*r;
			}
		
			p = posn[8*r+c];
			if( p == 0) continue;
			drawPiece( g, p, x, y );
		}
	}
	
	// draw the dragged piece if there is one
	if( isDrag ) drawPiece( g, pieceMoved, xDrag, yDrag );
}

/***********************************************************************************/

/*
  Called from the paintComponent method. drawPiece() selects the appropriate
  Piece object based on the cell entry of the posn array (p), then places the
  image, which is an instance variable of the Piece object, according to the
  x,y co-ordinates of the square, and the offset for that particular piece object.
*/

private void drawPiece( Graphics g, int p, int x, int y ) {

	Piece piece = null;

	switch(p) {
	
		case WK: piece = wk; break;
		case WQ: piece = wq; break;
		case WB: piece = wb; break;
		case WN: piece = wn; break;
		case WR: piece = wr; break;
		case WP: piece = wp; break;
		case BK: piece = bk; break;
		case BQ: piece = bq; break;
		case BB: piece = bb; break;
		case BN: piece = bn; break;
		case BR: piece = br; break;
		case BP: piece = bp; break;
		
	}
	
	g.drawImage( piece.img, x + piece.x, y + piece.y, null );
}

/**********************************************************************************/

/*
  For each piece type. this method does the following steps...
  1. Construct a blank transparent BufferedImage object
  2. calls createPieceImage which draws the piece on to the blank image
  3. sends the modified image to a new Piece constructor.
*/

private void buildPieceSet() {

	BufferedImage img;
	
	wk = new Piece( AiChess.wk );
	wq = new Piece( AiChess.wq );
	wr = new Piece( AiChess.wr );
	wb = new Piece( AiChess.wb );
	wn = new Piece( AiChess.wn );
	wp = new Piece( AiChess.wp );
	bk = new Piece( AiChess.bk );
	bq = new Piece( AiChess.bq );
	br = new Piece( AiChess.br );
	bb = new Piece( AiChess.bb );
	bn = new Piece( AiChess.bn );
	bp = new Piece( AiChess.bp );



}

private void buildPieceSetW() {

	wk = new Piece( HumanVsComp.wk );
	wq = new Piece( HumanVsComp.wq );
	wr = new Piece( HumanVsComp.wr );
	wb = new Piece( HumanVsComp.wb );
	wn = new Piece( HumanVsComp.wn );
	wp = new Piece( HumanVsComp.wp );
	bk = new Piece( HumanVsComp.bk );
	bq = new Piece( HumanVsComp.bq );
	br = new Piece( HumanVsComp.br );
	bb = new Piece( HumanVsComp.bb );
	bn = new Piece( HumanVsComp.bn );
	bp = new Piece( HumanVsComp.bp );
}

/**********************************************************************************/

// This method cotains draw commands which determine the shape and relative size of the piece. Right now,
// the size of the pieces, and the board, is fixed, later it will be changed so that
// the piece size and board size is scaleable.

private void createPieceImage( BufferedImage img, char p ) {

	// This associates a graphics2D context with the blank BufferedImage objects
	// This enables us to paint shapes on an image.
	Graphics2D g2 = img.createGraphics();

	if( Character.isLowerCase( p ) ) 
		g2.setColor( Color.BLACK );
	else
		g2.setColor( Color.WHITE );

	if( Character.toUpperCase( p ) == 'R' ) {

		g2.fillRect( (int) (.14*ss), (int) (.84*ss), (int) (.72*ss), (int) (.08*ss) );

		int[] x1 = { 7, 13, 37, 43 };
		int[] y1 = { 42, 36, 36, 42};
		g2.fillPolygon( x1, y1, 4 );

		g2.fillRect( 13, 18, 24, 18 );

		int[] x2 = { 13, 10, 40, 37 };
		int[] y2 = { 18, 15, 15, 18 };
		g2.fillPolygon( x2, y2, 4 );

		g2.fillRect( 10, 12, 30, 3 );

		g2.fillRect( 10, 8, 4, 4 );
		g2.fillRect( 36, 8, 4, 4 );
		g2.fillRect( 21, 8, 8, 4 );

		return;
	}

	if( p == 'N' || p == 'n' ) {

		g2.fillArc( -10, -5, 53, 100, 0, 45 );
		g2.fillArc( 15, 9, 26, 33, 60, 120 );

		return;
	}
	
	if( Character.toUpperCase( p ) == 'B' ) {

		g2.fillRect( 7, 44, 36, 3);

		int[] x1 = { 7, 20, 30, 43 };
		int[] y1 = { 44, 40, 40, 44};
		g2.fillPolygon( x1, y1, 4 );

		g2.fillOval( 15, 12, 19, 30 );

		g2.fillOval( 21, 6, 7, 7 );

		return;
	}

	if( Character.toUpperCase( p ) == 'Q' ) {

		int[] x1 = { 10, 40, 37, 13 };
		int[] y1 = { 30, 30, 45, 45 };
		g2.fillPolygon( x1, y1, 4 );

		int[] x2 = { 10, 5,  12, 17, 25, 33, 38,  45, 40 };
		int[] y2 = { 30, 17, 23, 6,  17, 6,  23,  17, 30 };
		g2.fillPolygon( x2, y2, 9 );

		return;
	}

	if( Character.toUpperCase( p ) == 'K' ) {

		int[] x1 = { 10, 40, 37, 13 };
		int[] y1 = { 30, 30, 45, 45 };
		g2.fillPolygon( x1, y1, 4 );

		int[] x2 = { 10, 5,  15, 25 };
		int[] y2 = { 30, 15, 15, 30 };
		g2.fillPolygon( x2, y2, 4 );

		int[] x3 = { 25, 35, 45, 40 };
		int[] y3 = { 30, 15, 15, 30 };
		g2.fillPolygon( x3, y3, 4 );

		g2.fillRect( 19, 10, 12, 12 );

		if( Character.isUpperCase( p ) ) g2.setColor( Color.BLACK );
		else g2.setColor( Color.WHITE );

		g2.drawLine( 21, 16, 29, 16 );
		g2.drawLine( 25, 12, 25, 20 );

		return;
	}

	if( Character.toUpperCase( p ) == 'P' ) {

		g2.fillArc( 8, 37, 34, 17, 0, 180 );
		g2.fillOval( 19, 17, 12, 24 );
		g2.fillArc( 12, 13, 26, 15, 0, 180 );
		g2.fillArc( 20, 9, 10, 12, 0, 180 );

		return;
	}
}

/**********************************************************************************/

/*
  the instance variables x & y are to be used as offsets to the top left corner of the
  square where the piece will be placed. This allows piece images of different sizes to
  be placed properly on the board.
*/

class Piece {
	
	BufferedImage img;
	int x,y;
	
	Piece( BufferedImage img ) {
		this.img = img;
		int w = img.getWidth();
		int h = img.getHeight();
			
		x = (ss-w)/2;
		y = ((ss-h) == 0) ? 0 : ss-h-3;
	}
}

/**********************************************************************************/

}
