import java.text.DecimalFormat;

public class Charity {
	private String charityName, charityGrade;
	private int employees;
	private ProgramGrant shortTermNeeds;
	private ProgramGrant researchNeeds;
	private AdminGrants adminGrants;
	private double charityRating;
	private double charityTotalExpenses;
	
	public Charity(double expenses, int employee, String name) {
		if(expenses > 0)
			charityTotalExpenses = expenses;
			
		if(employee > 0)
			employees = employee;
		
		charityName = name;
		return;
	}
	
	public void addResearchGrant(double amount, int numPeopleImapcted, String impactGrade) {
		researchNeeds = new ProgramGrant(amount, numPeopleImapcted, impactGrade);
		return;
	}
	
	public void addShortGrant(double amount, int numPeopleImapcted, String impactGrade) {
		shortTermNeeds = new ProgramGrant(amount, numPeopleImapcted, impactGrade);
		return;
	}
	
	public void addAdminGrant(double amount) {
		adminGrants = new AdminGrants(amount, employees);
		return;
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
