import java.util.*;

public class AtomMover implements Runnable {
    private Controller C;
    private View view;
    private static Random r;
    
    public AtomMover(Controller con, View v) {
        C = con;
        view = v;
    }
    
    public void run() {
        while (true) {
            ArrayList<Atom> atoms = C.getModel().getAtomsList();
            synchronized (atoms) {
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
			try { Thread.sleep(25); }
			catch (InterruptedException e) { }
			view.repaint();
		}
	}
}

