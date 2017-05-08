import java.util.*;

public class MoveList<Move> extends ArrayList<Move> {

	public void undo( int n ) {
	
		this.removeRange( n, this.size() );
	}


}