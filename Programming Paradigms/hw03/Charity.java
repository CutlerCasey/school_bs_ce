import java.text.DecimalFormat;

public class Charity {
	private String charityName, charityGrade;
	private int employees;
	private ProgramGrant shortTermNeeds, researchNeeds;
	private AdminGrants adminGrants;
	private double charityRating, charityTotalExpenses;
	
	public Charity(double expenses, int employee, String name) {
		this.charityTotalExpenses = 0.0D;
		if(expenses > 0)
			this.charityTotalExpenses = expenses;
		this.employees = 0;
		if(employee > 0)
			this.employees = employee;
		
		this.charityName = name;
		return; //I know this is not needed just like it to say it it the end
	}
	
	public void addResearchGrant(double amount, int numPeopleImapcted, String impactGrade) {
		this.researchNeeds = new ProgramGrant(amount, numPeopleImapcted, impactGrade);
		return;
	}
	
	public void addShortGrant(double amount, int numPeopleImapcted, String impactGrade) {
		this.shortTermNeeds = new ProgramGrant(amount, numPeopleImapcted, impactGrade);
		return;
	}
	
	public void addAdminGrant(double amount) {
		this.adminGrants = new AdminGrants(amount, this.employees);
		return;
	}
	
	public String charityGrade() {
		//temps to save a bit of calling later
		double tempG = adminGrants.adminGrants(); 
		double tempSTNr = shortTermNeeds.getRating();
		double tempRNr = researchNeeds.getRating();
		double tempCTEE = 0; double adminGrantRating = 100;
		String tempSTNig = shortTermNeeds.getIG();
		String tempRNig = researchNeeds.getIG();
		DecimalFormat formatter = new DecimalFormat("#0.00");
		
		// setting charityRating for output
		if(this.charityTotalExpenses != 0)
			adminGrantRating = ((1.0 - (tempG / charityTotalExpenses)) * 100.0);		
		if(adminGrantRating > 100)
			adminGrantRating = 100;
		this.charityRating = (tempSTNr * 0.4) + (tempRNr * 0.3) + (adminGrantRating * 0.3);
		if(this.charityRating < 0)
			this.charityRating = 0;
		else if(this.charityRating > 100)
			this.charityRating = 100;
		
		//setting charityGrade for output
		if(this.employees != 0)
			tempCTEE = tempG / this.employees;
		if(this.charityRating < 60 || tempSTNig.equals("F") || tempRNig.equals("F") || tempCTEE >= 100000.00)
			this.charityGrade = "F";
		else if(this.charityRating < 70 || tempSTNig.equals("D") || tempRNig.equals("D"))
			this.charityGrade = "D";
		else if(this.charityRating < 80 || tempSTNig.equals("C") || tempRNig.equals("C"))
			this.charityGrade = "C";
		else if(this.charityRating < 90 || tempSTNig.equals("B") || tempRNig.equals("B"))
			this.charityGrade = "B";
		else if(this.charityRating >= 90)
			this.charityGrade = "A";
		else
			System.out.println("This should not happen?");
		return this.charityGrade;
	}
	
	public void printReport() {
		//temps to save a bit of calling later
		double tempG = adminGrants.adminGrants(); 
		double tempSTNr = shortTermNeeds.getRating();
		double tempRNr = researchNeeds.getRating();
		double tempCTEE = 0; double adminGrantRating = 100;
		String tempSTNig = shortTermNeeds.getIG();
		String tempRNig = researchNeeds.getIG();
		DecimalFormat formatter = new DecimalFormat("#0.00");
		
		// setting charityRating for output
		if(charityTotalExpenses != 0)
			adminGrantRating = ((1.0 - (tempG / charityTotalExpenses)) * 100.0);		
		if(adminGrantRating > 100)
			adminGrantRating = 100;
		charityRating = (tempSTNr * 0.4) + (tempRNr * 0.3) + (adminGrantRating * 0.3);
		if(charityRating < 0)
			charityRating = 0;
		else if(charityRating > 100)
			charityRating = 100;
		
		//setting charityGrade for output
		if(employees != 0)
			tempCTEE = tempG / employees;	
		if(charityRating < 60 || tempSTNig.equals("F") || tempRNig.equals("F") || tempCTEE >= 100000.00)
			charityGrade = "F";
		else if(charityRating < 70 || tempSTNig.equals("D") || tempRNig.equals("D"))
			charityGrade = "D";
		else if(charityRating < 80 || tempSTNig.equals("C") || tempRNig.equals("C"))
			charityGrade = "C";
		else if(charityRating < 90 || tempSTNig.equals("B") || tempRNig.equals("B"))
			charityGrade = "B";
		else if(charityRating >= 90)
			charityGrade = "A";
		else
			System.out.println("This should not happen?");
		
		System.out.print("Final Rating for " + charityName + " is ");
	    System.out.print(formatter.format(charityRating));
		System.out.println(" with a grade of " + charityGrade);
		return;
	}
}
