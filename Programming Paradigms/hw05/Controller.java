import java.awt.Graphics;
import java.io.IOException;
import java.awt.event.*;

class Controller implements KeyListener, MouseListener {
    private Model model;
    private View view;
    char temp;

    Controller() throws IOException, Exception {
        view = new View(this);
        model = new Model(view.getWidth(), view.getHeight());
    }

    public void update(Graphics g) {
        if (model != null) {
            model.update(g);
        }
    }
    
    public Model getModel() { return model; }

    public void mousePressed(MouseEvent e) {
		model.forward();
        view.repaint();
    }
    public void mouseReleased(MouseEvent e) {    }
    public void mouseEntered(MouseEvent e) {    }
    public void mouseExited(MouseEvent e) {    }
    public void mouseClicked(MouseEvent e) {    }

	public void keyReleased(KeyEvent arg0) { }
	public void keyPressed(KeyEvent arg0) { }
	public void keyTyped(KeyEvent arg0) {
		temp = arg0.getKeyChar();
        if(temp == 'r' || temp == 'R') {
            model.initialize();
            view.repaint();
        }
        else if(temp == 's' || temp == 'S') {
            start();
        }
	}
	private void start() {
    	new Thread(new AtomMover(this, view) {}).start();
    }
	
	public static void main(String[] args) throws Exception {
        //  Use the following line to determine which directory your program
        //  is being executed from, since that is where the image files will
        //  need to be.
        //System.out.println("cwd=" + System.getProperty("user.dir"));
        new Controller();
    }
}

