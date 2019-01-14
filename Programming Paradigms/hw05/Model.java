import java.awt.Graphics;
import java.io.IOException;
import java.util.*;

class Model {
    private ArrayList<Atom> atoms;
    private int width;
    private int height;
    Random r;
    private int creator;

    Model(int w, int h) throws IOException {
        atoms = new ArrayList<Atom>();
        width = w;
        height = h;
        r = new Random();
        initialize();
    }
    
    public ArrayList<Atom> getAtomsList() { return atoms; }
    
    public void initialize() {
        synchronized (atoms) {
			if (creator >= 1) {
				Iterator<Atom> iter = atoms.iterator();
				while (iter.hasNext()) {
					Atom i = iter.next();
					iter.remove();
				}
			}
			for (int i = 0; i < 50; i++) {
				int newX = r.nextInt(width - 50);
				int newY = r.nextInt(height - 50);
				if (i % 2 == 0) {
					atoms.add(new Chlorine(newX, newY, width, height));
				} else {
					atoms.add(new Sodium(newX, newY, width, height));
				}
			}
			creator++;
		}
    }

    public void update(Graphics g) {
    	synchronized (atoms) {
			for (Atom atom : atoms) {
				atom.update(g);
			}
		}
    }

    public void forward() {
    	synchronized(atoms) {
    		for (Atom a : atoms) {
            	a.move();
            	
                for (Atom g : atoms) {
                    if(((a instanceof Chlorine) && (g instanceof Sodium)) || ((a instanceof Sodium) && (g instanceof Chlorine))) {
                        if (g.overlaps(a)) {
                        	if(g.shouldSeparate()) {
                            		g.setBoundFalse(); a.setBoundFalse();
                            		while(true) {
                            			int x1 = r.nextInt(11) - 5, y1 = r.nextInt(11) - 5;
                            			int x2 = r.nextInt(11) - 5, y2 = r.nextInt(11) - 5;
                            			if(x1 == x2 && y1 == x2) {
                            				continue;
                            			}
                            			else {
                            				a.setXSlope(x1); a.setXSlope(y1);
                            				g.setXSlope(x2); a.setXSlope(y2);
                            				break;
                            			}
                            		}
                            	}	
                            	else {
                            		if(!g.testBound() && !a.testBound()) {
                            			g.bound(); a.bound();
                            			double xx = (g.getXSlope() + a.getXSlope());
                            			double yy = (g.getYSlope() + a.getYSlope());
                            			int xSlope = (int) (xx / 2), ySlope = (int) (yy / 2);
                            			g.setXSlope(xSlope); a.setYSlope(ySlope);
                            		}
    							}
						}
					}
				}
			}
        }
    }
}

