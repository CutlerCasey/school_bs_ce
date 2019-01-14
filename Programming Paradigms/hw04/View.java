import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class View extends JFrame implements ActionListener {
	private class MyPanel extends JPanel {
		Controller controller;
    
		MyPanel(Controller paramController) {
			controller = paramController;
			addMouseListener(paramController);
		}
    
		public void paintComponent(Graphics g) {
			controller.update(g);
			revalidate();
		}
	}
  
	public View(Controller paramController) throws Exception {
		setTitle("Assignment 4");
		setSize(1000, 700);
		getContentPane().add(new View.MyPanel(paramController));
		setDefaultCloseOperation(3);
		setVisible(true);
	}
  
	public void actionPerformed(ActionEvent paramActionEvent) {
		repaint();
	}
}
