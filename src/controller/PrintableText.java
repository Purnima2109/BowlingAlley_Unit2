package controller;



import java.awt.*;
import java.awt.print.*;
import java.awt.geom.*;
import java.awt.font.*;

public class PrintableText implements Printable {
	String text;
	int pointsPerInch;

	public PrintableText(String t) {
		pointsPerInch = 72;
		text = t;
	}

	public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
		if (pageIndex > 0) {
			return NO_SUCH_PAGE;
		}
		
		Graphics2D g2d = (Graphics2D) g; // Allow use of Java 2 graphics on

		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		g2d.setPaint(Color.black);

		Point2D.Double pen = new Point2D.Double(0.25 * pointsPerInch, 0.25 * pointsPerInch);

		Font font = new Font ("courier", Font.PLAIN, 12);
   		FontRenderContext frc = g2d.getFontRenderContext();

		String []lines = text.split("\n");

		for (int i=0; i < lines.length; i++) {		
			if (lines[i].length() > 0) {
				TextLayout layout = new TextLayout(lines[i], font, frc);
				layout.draw(g2d, (float) pen.x, (float) (pen.y + i*14));
			}
		}

		return PAGE_EXISTS;
	}

}
