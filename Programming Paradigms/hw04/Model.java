import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.security.SecureRandom;
//import java.util.Random;

class Model {
	private ArrayList<Atom> atoms;
	private int count;

	Model() throws IOException {
		atoms = new ArrayList();
	}
  
	public void update(Graphics paramGraphics) {
		for (Atom localAtom : atoms) {
			localAtom.update(paramGraphics);
		}
	}
  
	public void setDestination(int x, int y, int width, int height) {
		Object localObject;
		if(count == 0) {
			localObject = new SecureRandom();
			int i = ((SecureRandom)localObject).nextInt(height);
			int j = ((SecureRandom)localObject).nextInt(height);
			Sodium localSodium = new Sodium(i, j);
			atoms.add(localSodium);
			Sodium.setDest(x, y);
			count++;
		}
		else {
			localObject = new Chlorine(x, y, width, height);
			atoms.add((Atom) localObject);
			count = 0;
		}
	}
}