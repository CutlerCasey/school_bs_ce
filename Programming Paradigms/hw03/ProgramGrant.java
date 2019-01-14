public class ProgramGrant {
	private double rating; //have a private instance variable that keeps track of the rating
	private String impactGradePG; //have a private instance variable that keeps track of am impact grade (as a string)
	
	public ProgramGrant(double grant, int people, String impactFactor) {
		double tempG = 0.0D, tempR = 0.0D;
		int tempP = 0;
		if(grant > 0.0D)
			tempG = grant;
		if(people > 0)
			tempP = people;
		tempR = ((tempP / tempG) * 100);
		if(tempR > 100)
			this.rating = 100.0;
		else
			this.rating = tempR;
		
		//setting impactGradePG based on ipact facter if no errors, giving error if not and setting to A
		if(impactFactor.equals("A") || impactFactor.equals("B") || impactFactor.equals("C") || impactFactor.equals("D") || impactFactor.equals("F")) {
			this.impactGradePG = impactFactor;
		}
		else {
			System.out.println("An impact grade other than A, B, C, D or F was entered.  Using the value A.");
			this.impactGradePG = "A";
		}

		return;
	}
		
	public double getRating() {
		return this.rating;
	}
	
	public String getIG() {
		return this.impactGradePG;
	}
}
