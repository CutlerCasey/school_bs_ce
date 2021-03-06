
public class AdminGrants {
	private double adminCost; //have a private instance variable that keeps track of the total (sum) money spent on administrative costs
	private int paidEmployee; //have a private instance variable that keeps track of the number of paid employees (it's possible this is 0)
	
	public AdminGrants(double amount, int employees) {
		adminExpense(amount);
		employeeExpense(employees);
		return;
	}
	
	private void adminExpense(double amount) { //have a function that allows you to add an administrative expense (this function needs to ensure the expense is non-negative)
		if(amount > 0)
			adminCost += amount;
		return;
	}
	
	public void employeeExpense(int people) { //have a function that allows you to set the number of paid employees (this function needs to ensure the number of paid employees is non-negative)
		if(people > 0)
			paidEmployee += people;
		return;
	}
	
	public double averagePerEmployee() { //have a function that returns the average administrative cost per paid employee
		return paidEmployee > 0 ? adminCost / paidEmployee : 0;
	}
		
	public double adminGrants() { //have a function that returns the total amount of administrative grants
		return adminCost;
	}
}
