import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

abstract class Atom {
	private int x;
	private int y;
	private Image image;
  
	public Atom(int paramInt1, int paramInt2, String paramString)	{
		try	{
			image = ImageIO.read(new File(paramString));
		}
		
		catch (IOException localIOException) {
			System.out.println("Unable to load image file.");
		}
		x = paramInt1;
		y = paramInt2;
	}
  
	public int getX() {
		return x;
	}
  
	public int getY() {
		return y;
	}
  
	public void setX(int paramInt) {
		x = paramInt;
	}
  
	public void setY(int paramInt) {
		y = paramInt;
	}
  
	public Image getImage()	{
		return image;
	}
  
	public abstract void update(Graphics paramGraphics);
}
