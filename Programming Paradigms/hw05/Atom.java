import java.awt.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Random;

abstract class Atom {
    private int x;
    private int y;
    private int size;
    private int w;
    private int h;
    private int xSlope;
    private int ySlope;
    private Image image;
    private static Random rand;
    private boolean bound;
    private int counter;

    public Atom(int xIn, int yIn, int width, int height, String imagePath, int imageSize) {
        if (rand == null) {
            rand = new Random();
        }
        size = imageSize;
        setImage(imagePath);
        x = xIn;
        y = yIn;
        w = width;
        h = height;
        xSlope = rand.nextInt(11) - 5;
        ySlope = rand.nextInt(11) - 5;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }
    public void setSize(int s) { size = s; }
    public void setX(int xIn) { x = xIn; }
    public void setY(int yIn) { y = yIn; }
    public int getXSlope() { return xSlope; }
    public int getYSlope() { return ySlope; }
	public void setXSlope(int xSIN) { xSlope = xSIN; }
	public void setYSlope(int ySIN) { ySlope = ySIN; }
    
    public void setImage(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException ioe) {
            System.out.println("Unable to load image file.");
        }
    }
    public Image getImage() { return image; }
    
    public boolean overlaps(Atom a) {
    	return x < a.x + a.w && x + w > a.x && y < a.y + a.h && y + h > a.y;
    }
    
    public void update(Graphics g) {
        g.drawImage(getImage(), x, y, getSize(), getSize(), null);
        if(bound) {
        	counter++;
        }
    }
    
    public void move() {
        // Move the Sprite
        int x = getX() + xSlope;
        int y = getY() + ySlope;
        if (x < 0) x = w;
        if (x > w) x = 0;
        if (y < 0) y = h;
        if (y > h) y = 0;
        setX(x);
        setY(y);
    }
    
    public void setBoundFalse() { bound = false; }
    
    public boolean testBound() { return bound; }
    
    public void bound() { bound = true; }
    
    public boolean shouldSeparate() {
        if (counter >= 20) { return true; }
        return false;
    }
}

