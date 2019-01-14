import java.awt.Graphics;
import javax.swing.JPanel;

public class GradePanel extends JPanel {
	String grade; //variable to hold the current grade.
	
	public GradePanel(String letterGrade) {
		this.grade = letterGrade; //getting the letter grade for the rest.
	}
	
	public void paintComponent(Graphics g) {
		//alows you to override the background so it does not come out.
		super.paintComponent(g); //paints the component as if you had not overridden the paintComponent
		
		//get height and width of the jpanel for use later
		int width = getWidth();
		int height = getHeight();
		if(this.grade.equals("A")) { //gets current grade and checks if == to "A"
			g.drawLine(0, height, width / 2, 0); //left x, bottom y, only half way up over x, top y: to draw / of the A.
			g.drawLine(width / 2, 0, width, height); //half way over left x, top y, far right x, bottom y: to draw \ of the A.
			g.drawLine(width / 4, height / 2, width * 3 / 4, height / 2); //1/4 x from the left, top y or center of, x to 3/4 the screen, bottom y to center of: to draw the - of the A.
		}
		else {
			g.drawString(this.grade, width / 2, height / 2); //gets the current grade, puts it in the center of the screen.
		}
	}
}
