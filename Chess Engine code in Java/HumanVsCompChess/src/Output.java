
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class Output extends JTextPane {

	Style[] styles;

	StyledDocument doc;
	
	Font diags = new Font( "monospaced", Font.PLAIN, 12 );
	public Output() {
		doc = this.getStyledDocument();
		styles = new Style[3];
		
		styles[0] = doc.addStyle( "style0", null );

		styles[1] = doc.addStyle( "style1", styles[0] );
		StyleConstants.setForeground( styles[1], new Color(160, 82, 45) );

		styles[2] = doc.addStyle( "style2", styles[0] );
		StyleConstants.setForeground( styles[2], new Color(34, 139, 34) );
	}

	public void append( String str ) {
	
		setFont( diags );
	
		try {
			doc.insertString( doc.getLength(), str, styles[0] );
		}
		catch( Exception e ) { e.printStackTrace(); }
	}

}