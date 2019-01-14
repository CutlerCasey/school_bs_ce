import java.awt.Graphics;
import java.security.SecureRandom;
//import java.util.Random;

class Chlorine extends Atom {
	private int w;
	private int h;
	private int xSlope;
	private int ySlope;
  
	public Chlorine(int x, int y, int width, int height) {
		super(x, y, "Chlorine.png");
		w = width;
		h = height;
		SecureRandom random = new SecureRandom();
		int x = 0, y = 0;
		while(0 == x * y && (y < 4 || x < 4) {
			x = random.nextInt(21);
			x = x - (2 * x);
			y = random.nextInt(21);
			y = y - (2 * y);
		}
		xSlope = x; ySlope = y;
	}
  
	public void update(Graphics g) {
		int i = getX() + xSlope;
		int j = getY() + ySlope;
		if (i < 0) {
			i = w;
		}
		if (i > w) {
			i = 0;
		}
		if (j < 0) {
			j = h;
		}
		if (j > h) {
			j = 0;
		}
		setX(i);
		setY(j);
    
		g.drawImage(getImage(), i, j, 100, 100, null);
	}
}
