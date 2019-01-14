import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Assignment3 {
	public static void main(String[] args) {
		try {
			//setting up the variables
			String charityName = "";
			double charityTotalExpenses = 0.0D;
			int numberOfEmployees = 0;
			int numAdminGrants = 0;
			
			double researchGrantAmount = 0.0D;
			int researchGrantPeopleImpacted = 0;
			String researchGrantImpactFactor = "";
			double shortGrantAmount = 0.0D;
			int shortGrantPeopleImpacted = 0;
			String shortGrantImpactFactor = "";
			
			charityName = JOptionPane.showInputDialog("Enter the name of the charity."); //simple way to get a string without testing
			charityTotalExpenses = Double.parseDouble(JOptionPane.showInputDialog("Enter the charity's total expenses.")); //get the sting, but test it is a form of a double
			numberOfEmployees = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of paid employees employed by the charity.")); //get the sting, but test it is a form of a int
			numAdminGrants = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of administrative grants.")); //get the sting, but test it is a form of a int
			
			Charity charity = new Charity(charityTotalExpenses, numberOfEmployees, charityName); //setting up the charity
			for(int i = 0; i < numAdminGrants; i++) { //get multi administrative grants with a simple for loop
				//new addmin grants
				charity.addAdminGrant(Double.parseDouble(JOptionPane.showInputDialog("Enter the cost of administrative grant " + (i + 1) + "."))); //get the sting, but test it is a form of a double
			}
			researchGrantAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter the cost of the research grant.")); //get the sting, but test it is a form of a double
			researchGrantPeopleImpacted = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of people impacted by the resarch grant.")); //get the sting, but test it is a form of a int
			researchGrantImpactFactor = JOptionPane.showInputDialog("Enter the impact factor of the research grant."); //simple way to get a string without testing, since chars only do not work as far as I know
			
			charity.addResearchGrant(researchGrantAmount, researchGrantPeopleImpacted, researchGrantImpactFactor); //setting up the research grant
			shortGrantAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter the cost of the short term grant.")); //get the sting, but test it is a form of a double
			shortGrantPeopleImpacted = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of people impacted by the short term grant.")); //get the sting, but test it is a form of a int
			shortGrantImpactFactor = JOptionPane.showInputDialog("Enter the impact factor of the short term grant."); //simple way to get a string without testing
			charity.addShortGrant(shortGrantAmount, shortGrantPeopleImpacted, shortGrantImpactFactor); //setting up the short term grants
			
			GradePanel panel = new GradePanel(charity.charityGrade()); //putting all the data in a nice panel to actually use.
			JFrame app = new JFrame(); //starting the JFrame
			
			app.setDefaultCloseOperation(3); //Automatically hide and dispose the frame after invoking any registered WindowListener objects
			app.setSize(500, 500); //setting the size of the panel to be used of 500x500 pixels
			app.add(panel); //from what I have read is a requirement for JFrame creation.
			app.setVisible(true); //something instructor said to do to make sure you can click on things in JFrames, not 100% sure why though.
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Invalid data entered. Exiting."); //I remember this basically being part of the requirements.
		}
	}
}
