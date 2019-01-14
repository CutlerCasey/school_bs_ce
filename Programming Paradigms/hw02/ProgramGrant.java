public class ProgramGrant {
	private double rating; //have a private instance variable that keeps track of the rating
	private String impactGradePG; //have a private instance variable that keeps track of am impact grade (as a string)
	
	public ProgramGrant(double grant, int people, String impactFactor) {
		double tempG; double tempR;
		int tempP;
		if(grant < 0)
			tempG = 0;
		else
			tempG = grant;
		if(people < 0)
			tempP = 0;
		else
			tempP = people;
		tempR = ((tempP / tempG) * 100);
		if(tempR > 100)
			rating = 100.0;
		else
			rating = tempR;
		
		//setting impactGradePG based on ipact facter if no errors, giving error if not and setting to A
		if(impactFactor.equals("A") || impactFactor.equals("B") || impactFactor.equals("C") || impactFactor.equals("D") || impactFactor.equals("F")) {
			impactGradePG = impactFactor;
		}
		else {
			System.out.println("An impact grade other than A, B, C, D or F was entered.  Using the value A.");
			impactGradePG = "A";
		}

		return;
	}
		
	public double getRating() {
		return rating;
	}
	
	public String getIG() {
		return impactGradePG;
	}
}
