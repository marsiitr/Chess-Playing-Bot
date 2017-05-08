import java.util.*;

public class Game {

/********************************************************************/

	private MoveList<Move> mList;
	private int pointer;

	private final GameState initState;
	private GameState state;
	
	private final int[] initPosn;
	private int[] posn;

/********************************************************************/
	
	public Game( String fen ) {
	
		initState = new GameState( fen );
		state = initState.clone();
		initPosn = Utils.toArray( fen.substring(0, fen.indexOf(" ")) );
		posn = Arrays.copyOf( initPosn, 64 );
		mList = new MoveList<Move>();
		pointer = -1;
	}
	
/********************************************************************/

	// called for each move made in the game. Updates the central game state object
	// and adds the move to the moveList.
	public void update( Move m ) {
		pointer++;
		
		// if the pointer is not at the end of the current game, i.e. if we have previously
		// used the left button, then we remove the remaining moves of a new move is played.
		if( pointer < mList.size() ) {
			mList.undo(pointer);
		}
		
		// add the move to the list and update the central GameState.
		mList.add(m);
		state = mList.get(pointer).gs;
		posn = Arrays.copyOf( mList.get(pointer).posn, 64 );
	}
	
/********************************************************************/

	// called from the event handler for the left button in the GUI
	public boolean leftUpdate() {
		pointer--;
		if( pointer == -1 ) {
			state = initState;
			posn = initPosn;
			return true;
		}
		state = mList.get(pointer).gs;
		posn = Arrays.copyOf( mList.get(pointer).posn, 64 );
		return false;
	}
	
/********************************************************************/

	// called from the event handler for the right Button in the GUI.
	public boolean rightUpdate() {
		pointer++;
		state = mList.get(pointer).gs;
		posn = Arrays.copyOf( mList.get(pointer).posn, 64 );
		if( pointer == mList.size()-1 ) return true;
		return false;
	}
	
/********************************************************************/

	public boolean isLeft() {

		return pointer != -1;
	}
	
	public boolean isRight() {

		return ( mList.size() > 0 ) && ( pointer < (mList.size() - 1) );
	}

/********************************************************************/
	
	public void setState( GameState gs ) {
		state = gs;
	}
	
	public GameState getState() {
		return state.clone();
	}
	
	public void setPosn( int[] posn ) {
		this.posn = Arrays.copyOf( posn, 64 );
	}
	
	public int[] getPosn() {
		return Arrays.copyOf( posn, 64 );
	}
	
	public String fen() {
		return Utils.toFen(posn) +" " +state.toString();
	}
}
		
	
	