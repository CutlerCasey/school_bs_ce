import java.awt.Graphics;

class Sodium extends Atom {
	private static int dest_x;
	private static int dest_y;
  
	Sodium(int paramInt1, int paramInt2) {
		super(paramInt1, paramInt2, "Sodium.png");
	}
  
	public void update(Graphics g) {
		int i = getX();
		int j = getY();
		
		if(i < dest_x) {
			i++;
		}
		else if(i > dest_x) {
			i--;
		}
		
		if(j < dest_y) {
			j++;
		} 
		else if(j > dest_y) {
			j--;
		}
		setX(i);
		setY(j);
    
		g.drawImage(getImage(), i, j, 100, 100, null);
	}
  
	public static void setDest(int paramInt1, int paramInt2) {
		dest_x = paramInt1;
		dest_y = paramInt2;
	}
}
