public class CharityTest {
	public static void main(String[] args) {
		Charity charity1 = new Charity(100000, 1, "Charity 1");
		charity1.addAdminGrant(10000.000);
		charity1.addShortGrant(80000, 100000, "A");
		charity1.addResearchGrant(10000, 10000, "A");
		charity1.printReport();
		
		Charity charity2 = new Charity(1000000, 10, "Charity 2");
		charity2.addAdminGrant(100000);
		charity2.addShortGrant(700000, 600000, "B");
		charity2.addResearchGrant(200000, 400000, "C");
		charity2.printReport();
		
		Charity charity3 = new Charity(-40000, 10, "Charity 3");
		charity3.addAdminGrant(100000);
		charity3.addShortGrant(700000, 400000, "B");
		charity3.addResearchGrant(200000, 400000, "C");
		charity3.printReport();
		
		Charity charity4 = new Charity(10000000, 300, "Charity 4");
		charity4.addAdminGrant(1000000);
		charity4.addShortGrant(4000000, 3750000, "A");
		charity4.addResearchGrant(5000000, 5500000, "E");
		charity4.printReport();
	}
}
