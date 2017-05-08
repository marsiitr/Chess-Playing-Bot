// Right now this is just a JFrame shell to load the GUI

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class AiChess extends JFrame {

	Gui table;
	
	public static BufferedImage wk, wq, wr, wb, wn, wp, bk, bq, br, bb, bn, bp;
	
	public AiChess( String title ) {
	
		super( title );
	
		try {
			wk = ImageIO.read( new File( "wk.png" ) );
			wq = ImageIO.read( new File( "wq.png" ) );
			wr = ImageIO.read( new File( "wr.png" ) );
			wb = ImageIO.read( new File( "wb.png" ) );
			wn = ImageIO.read( new File( "wn.png" ) );
			wp = ImageIO.read( new File( "wp.png" ) );
			bk = ImageIO.read( new File( "bk.png" ) );
			bq = ImageIO.read( new File( "bq.png" ) );
			br = ImageIO.read( new File( "br.png" ) );
			bb = ImageIO.read( new File( "bb.png" ) );
			bn = ImageIO.read( new File( "bn.png" ) );
			bp = ImageIO.read( new File( "bp.png" ) );
		}
		catch( Exception e ) { System.out.println( "error" ); }


		table = new Gui( false );
		add( table );
		
	}
	
}
