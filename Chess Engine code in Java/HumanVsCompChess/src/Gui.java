import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Gui extends JPanel implements Runnable, Pieces, ActionListener, ItemListener,
								MouseListener, MouseMotionListener, ChangeListener {

static final String startFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

Board board;
Output output;
AiExplorer comp;
boolean isPlay;
JButton doB;
int undercheck = 0;
JComboBox modeBox;
int ply = 5;
int mode, buMode;
boolean setup = false;

Game game;
Move move;
	
int width = 480;
int height = 480;

int xOffset, yOffset;
boolean isDrag;
int src, dest;

Random rand = new Random();

/********************************************************************************/

public Gui( boolean web ) {

	Utils.initialize();
	setPreferredSize( new Dimension(width, height) );
	createGUI( web );
	reset( startFen );
}
	
/********************************************************************************/
	
private void createGUI( boolean web ) {
	
	setLayout( new BorderLayout() );
		
	board = new Board( web, width, width );
	board.addMouseListener(this);
	board.addMouseMotionListener(this);
	add( board, BorderLayout.NORTH );
		
	JPanel controlPanel = new JPanel();
	controlPanel.setLayout( new BorderLayout() );

	Object[] modes = { "normal"};
	modeBox = new JComboBox( modes );
	modeBox.setSelectedIndex(0);
	modeBox.addActionListener(this);
	
	output = new Output();
	JScrollPane scroller = new JScrollPane( output );
	scroller.setPreferredSize( new Dimension( width, height-width-62 ) );
	
}

/********************************************************************************/

// a method which resets the board and game state variables according to the fen string
// which is passed to the method
private void reset( String fen ) {

	game = new Game( fen );
	comp = new AiExplorer( output );

	isDrag = false;
	board.isDrag = false;

	modeBox.setSelectedIndex(0);
	mode=0;
	isPlay = false;

	ply = 5;

	board.posn = game.getPosn();
	board.repaint();

	output.setText( game.fen() );

}

/********************************************************************************/

// this method is called whenever we have a move from the GUI or the AiExplorer that needs
// to be played in the game

private void makeMove( Move move ) {

	output.append( move.alg() +"\n" );

	game.update( move );
	board.posn = game.getPosn();
	board.repaint();
	if( !isPlay ) {

	}

}

/********************************************************************************/

private void compMove() {

	GameState gs = game.getState();

	comp.init( game.getPosn(), ply );
	output.setText("");
	
	long t1 = (new Date()).getTime();
	int[] m = comp.getMove( gs, ply );
	long t2 = (new Date()).getTime();
	output.append( "time = " +(t2-t1) +"ms" +"\n" );
	
	if( m[0]==0 && m[1]==0 ) {
		comp.init( game.getPosn(), 0 );
		String s = comp.inCheck(gs) ? "checkmate" : "stalemate";
		JOptionPane.showMessageDialog(this, "Computer is in " + s, "Invalid MOve", JOptionPane.PLAIN_MESSAGE);
	//	output.append( ));
		if( isPlay ) {
			isPlay = false;
			modeBox.setEnabled(true);

		}
		return;
	}	
	comp.init( game.getPosn(), 0 );
	GameState gs1 = comp.makeMove( game.getState(), m );
	
	Move move = new Move( game, m, gs1 );
	makeMove( move );

}

public void run() {
	compMove();
}


// event handlers for the various buttons in the GUI.
	
public void actionPerformed( ActionEvent e ) {
	Object o = e.getSource();
	// tells the AiExplorer to make a move in the current position. Currently the selection is random.
	if( o == doB ) {
	
		String cmd = e.getActionCommand();
	
		output.setText("");
		GameState gs, gs1;
		int lCount=0;
		String s="";
		int[] m;
		int p0, p1;
		
		switch( mode ) {
		
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:

				if( cmd.equals("move") ) {
					(new Thread(this)).start();
					break;
				}
				else {
					comp.think = false;
				}
				break;

			case 6:
			
				String fen1 = "";
				if( fen1.equals("") ) output.setText( game.fen() );
				else try {
					String fen2 = "";
					game = new Game( fen2 );
					board.posn = game.getPosn();
					board.repaint();
				
					output.setText( game.fen() );
				}
				catch( Exception ex ) {
	
				}
				
				modeBox.setSelectedIndex(buMode);
				break;

			case 7:

				gs = game.getState();
				
				comp.init( game.getPosn(), 0 );
				comp.moveGen( gs, 0 );

				lCount = 0;

				output.setText( "" +(comp.nc[0] + comp.nm[0]) +" candidates" +"\n" );

				for( int i = 0; i<comp.nc[0]; i++ ) {
					m = comp.cList[0][i];
					s = Utils.toAlg.get(m[0]) +"-" +Utils.toAlg.get(m[1]) +" " +m[2];
					p0 = comp.posn[m[0]]; p1 = comp.posn[m[1]];
					gs1 = comp.makeMove( gs, m );
					if( gs1.isLegal ) {
						lCount++;
						s += " legal" +"\n";
					}
					else s += " illegal" +"\n";
					comp.unmakeMove( gs1, m, p0, p1 );
					output.append(s);
				}
				for( int i = 0; i<comp.nm[0]; i++ ) {
					m = comp.mList[0][i];
					s = Utils.toAlg.get(m[0]) +"-" +Utils.toAlg.get(m[1]) +" " +m[2];
					p0 = comp.posn[m[0]]; p1 = comp.posn[m[1]];
					gs1 = comp.makeMove( gs, m );
					if( gs1.isLegal ) {
						lCount++;
						s += " legal" +"\n";
					}
					else s += " illegal" +"\n";
					comp.unmakeMove( gs1, m, p0, p1 );
					output.append(s);
				}
				
				output.append( "" +lCount +" " );
				break;

			case 8:

				gs = game.getState();
				
				comp.init( game.getPosn(), 0 );
				comp.capGen( gs, 0 );

				output.setText( "" +comp.nCap[0] +" " +"\n" );

		
				for( int i = 0; i<comp.nCap[0]; i++ ) {
					m = comp.capList[0][i];
					s = Utils.toAlg.get(m[0]) +"-" +Utils.toAlg.get(m[1]) +" " +m[2];
					p0 = comp.posn[m[0]]; p1 = comp.posn[m[1]];
					gs1 = comp.makeMove( gs, m );
					if( gs1.isLegal ) {
						lCount++;
						s += " legal" +"\n";
					}
					else s += " illegal" +"\n";
					comp.unmakeMove( gs1, m, p0, p1 );
					output.append(s);
				}
				
				output.append( "" +lCount +" " );
				break;
			
			case 9:
				comp.init(game.getPosn(), 0);
				comp.buildPieceList( game.getState(), 0 );
				int[] p;
				for( int i=0; i<comp.np[0]; i++ ) {
					p = comp.pList[0][i];
					output.append( Utils.toAlg.get(p[0]) +"  " +p[1] +"\n" );
				}
				break;

			case 10:
				reset( startFen );
				break;
				
			case 11:
				output.setText("");
				output.append( "" +Utils.zKey( game.getState(), game.getPosn() ) );
				return;
		}
		
		return;
	}
	if( o == modeBox ) {
	
		buMode = mode;
	
		mode = modeBox.getSelectedIndex();
		
		switch(mode) {

			case 0:	comp.abp = true;
					comp.qs = true;
					comp.pE = true;
					comp.mO = true;
					comp.pO = true;
					break;
			
			case 1:	comp.abp = true;
					comp.qs = true;
					comp.pE = true;
					comp.mO = true;
					comp.pO = true;
					doB.setToolTipText("");
	
					break;

			case 2:	comp.abp = true;
					comp.qs = true;
					comp.pE = false;
					comp.mO = true;
					comp.pO = true;
					doB.setToolTipText("");
	
					break;
			
			case 3:	comp.abp = true;
					comp.qs = false;
					comp.pE = true;
					comp.mO = true;
					comp.pO = true;
					doB.setToolTipText("");
		
					break;

			case 4:	comp.abp = true;
					comp.qs = false;
					comp.pE = false;
					comp.mO = true;
					comp.pO = true;
					doB.setToolTipText("");

					break;

			case 5:	comp.abp = true;
					comp.qs = false;
					comp.pE = false;
					comp.mO = false;
					comp.pO = false;
					break;

			case 6:	break;

			case 7:	comp.pO = true;
					comp.mO = true;
					break;

			case 8:	break;
			case 9: comp.pO = true;
					break;
					
			case 10:break;				
			case 11:break;
		}
		return;
	}
}

/********************************************************************************/

private void restorePosn() {
	board.posn[src] = board.pieceMoved;
	isDrag = false;
	board.isDrag = false;
	board.repaint();
}

/********************************************************************************/

// that contains a piece, the drag and drop process is initiated.
public void mousePressed( MouseEvent e ) {
	for(int i = 0; i < 65; i++) {
		Board.posMov[i] = 0;
	}
	board.repaint();
	
	output.setText("");

	if( e.getSource() != board ) return;

	GameState gs = game.getState();

	int ss = width/8;	// square size variable, to simplify calculation

	int x = e.getX();
	int y = e.getY();
	
	int row, col;

	if( board.invert ) {
		row = 7-y/ss;
		col = 7-x/ss;
	} else {
		row = y/ss;
		col = x/ss;
	}
	
	// this is the source posn array index of the piece being moved
	src = 8*row + col;

//	board.repaint();
	
	int tempPiece = board.posn[src];

	if( tempPiece == 0 ) {
		output.setText( "" );
		return;
	}
	
	// only the piece of the side to move can be moved
	if( !setup && ((gs.white && (tempPiece < 0)) || ( !gs.white && (tempPiece > 0) )) ) {
		JOptionPane.showMessageDialog(this, "Not Your Piece", "Invalid Selection", JOptionPane.PLAIN_MESSAGE);

		return;
	}
	isLegal( gs, src, 67 );
	
	// distance of mouse click from top left corner of square
	// used to draw piece as it is being dragged
	if( board.invert ) {
		xOffset = x - (7-col)*ss;
		yOffset = y - (7-row)*ss;
	} else {
		xOffset = x - col*ss;
		yOffset = y - row*ss;
	}
	
	// initial top left corner of square of dragged piece
	board.xDrag = col*ss;
	board.yDrag = row*ss;
	
	// the piece is "picked up" and the source square is emptied
	board.pieceMoved = tempPiece;
	board.posn[src] = 0;

	isDrag = true;
	board.isDrag = true;

	board.repaint();

} // end mousePressed()

/********************************************************************************/

// called when ever the user releases the mouse button
public void mouseReleased( MouseEvent e ) {

	for(int i = 0; i < 65; i++) {
		Board.posMov[i] = 0;
	}
	board.repaint();


	if( !isDrag ) return;
	isDrag = false;
	board.isDrag = false;
	
	int x = e.getX();
	int y = e.getY();
	
	// if you attempt to move the piece off the board, the piece will be returned to
	// its original square, and the board repainted.
	if( (x >= width) || (y >= width) || (x <= 0) || (y <=0 ) ) {
		if( !setup ) restorePosn();
		return;
	}
	
	int row, col;
	int ss = width/8;

	if( board.invert ) {
		row = 7-y/ss;
		col = 7-x/ss;
	} else {
		row = y/ss;
		col = x/ss;
	}
	
	// this is the destination posn array index for the piece being moved
	dest = 8*row + col;
	
	// if setup mode, draw piece where dropped and bypass legality check;
	if( setup ) {
		board.posn[dest] = board.pieceMoved;
		board.repaint();
		return;
	}

	// make sure not to create a move object if the piece is dropped
	// back on it's original square

	if( src == dest ) {
		restorePosn();
		return;
	}
	
	// if the piece is not being moved off the board, and if it is the right side
	// to move, then we do a legality check. If the move is not legal, then put
	// the piece back on on the source square;

	GameState gs = game.getState();
	GameState gs1 = isLegal( gs, src, dest );

	if(Board.posMov[dest] == 1 && !gs1.isLegal) {
		undercheck++;
		if(undercheck > 6) {
			JOptionPane.showMessageDialog(this, "CheckMate", "Game End", JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		}
		JOptionPane.showMessageDialog(this, "King under check", "Illegel Move", JOptionPane.PLAIN_MESSAGE);
		if(undercheck > 6) {
			System.exit(0);
		}
		for(int i = 0; i < 65; i++) Board.posMov[i] = 0;
			board.repaint();
			restorePosn();
			return;
	}
	if( !gs1.isLegal ) {
		for(int i = 0; i < 65; i++) Board.posMov[i] = 0;
		board.repaint();
		restorePosn();
		
		return;
	}

	output.setText("");
	int[] m = { src, dest, 0 };
	
	// promotion is handled here.
	boolean promo = (board.pieceMoved==WP && dest/8==0) || (board.pieceMoved==BP && dest/8==7);
	if( promo ) {
		String[] promoValues = { "Queen", "Rook", "Bishop", "Knight" };
		String promoPiece = JOptionPane.showInputDialog( null, "S", "t",
					JOptionPane.INFORMATION_MESSAGE, null, promoValues, promoValues[0] ).toString();
	
		if( promoPiece.equals("Queen") ) m[2] = gs.white ? WQ : BQ;
		else if( promoPiece.equals("Rook") ) m[2] = gs.white ? WR : BR;
		else if( promoPiece.equals("Bishop") ) m[2] = gs.white ? WB : BB;
		else if( promoPiece.equals("Knight") ) m[2] = gs.white ? WN : BN;
	}
	Move move = new Move( game, m, gs1 );

	for(int i = 0; i < 65; i++) {
		Board.posMov[i] = 0;
	}
	board.repaint();
	makeMove( move );
	
	( new Thread(this) ).start();

} 
// end mouseReleased()

/********************************************************************************/

//initiates calls to repaint() when a piece is dragged
public void mouseDragged( MouseEvent e ) {

	if( !isDrag ) return;

	int x = e.getX();
	int y = e.getY();
	
	// xDrag & yDrag is the top left corner of the virtual square that is dragged
	// along with the piece.
	board.xDrag = x - xOffset;
	board.yDrag = y - yOffset;
	
	board.repaint();

}

/********************************************************************************/

// unused event handlers
public void mouseClicked( MouseEvent e ) {}
public void mouseEntered( MouseEvent e ) {}
public void mouseExited( MouseEvent e ) {}
public void mouseMoved( MouseEvent e ) {}
public void itemStateChanged( ItemEvent e ) {}
public void stateChanged( ChangeEvent e ) {}

/********************************************************************************/

private GameState isLegal( GameState gs, int src, int dest ) {

	comp.init( game.getPosn(), 0);

	boolean bu = comp.mO; comp.mO = false;
	comp.genMoves( gs, src, 0 );
	comp.mO = bu;
	
	int[] m;
	for( int i=0; i<comp.nm[0]; i++ ) {
		m = comp.mList[0][i];
		if( m[0]==src && m[1]==dest ) {
			return comp.makeMove( gs, m );
		}
	}

	return new GameState();
}

/********************************************************************************/
	
}