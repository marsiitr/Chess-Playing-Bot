import javax.swing.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class  HumanVsComp extends JFrame {

	Gui table;
	
	static BufferedImage wk, wq, wb, wn, wr, wp, bk, bq, bb, bn, br, bp;
	
	HumanVsComp() {
	
		try {
			wk = ImageIO.read( getClass().getResourceAsStream("wk.png") );
			wq = ImageIO.read( getClass().getResourceAsStream("wq.png") );
			wb = ImageIO.read( getClass().getResourceAsStream("wb.png") );
			wn = ImageIO.read( getClass().getResourceAsStream("wn.png") );
			wr = ImageIO.read( getClass().getResourceAsStream("wr.png") );
			wp = ImageIO.read( getClass().getResourceAsStream("wp.png") );
			bk = ImageIO.read( getClass().getResourceAsStream("bk.png") );
			bq = ImageIO.read( getClass().getResourceAsStream("bq.png") );
			bb = ImageIO.read( getClass().getResourceAsStream("bb.png") );
			bn = ImageIO.read( getClass().getResourceAsStream("bn.png") );
			br = ImageIO.read( getClass().getResourceAsStream("br.png") );
			bp = ImageIO.read( getClass().getResourceAsStream("bp.png") );
		}
		catch(IOException e) {}
	
		table = new Gui( true );
		add( table );
	}
	 public static void main( String[] args ) {
		
		HumanVsComp frame = new HumanVsComp();
		frame.setTitle("HumanVsComputer Chess");
		frame.setResizable(false);
		frame.setLocation(200,50);
		//frame.setSize(490, 520);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}