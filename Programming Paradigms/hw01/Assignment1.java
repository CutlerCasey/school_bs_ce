import java.util.Scanner;

public class Assignment1{
	public static void main (String[] args) {
		int i =0, num =0, maxNum = 0;
		Scanner in = new Scanner(System.in);
		
		System.out.println("What would you like your max number to be?");
		maxNum = in.nextInt();
		
		//Empty String
		String primeNumbers = "";
		for (i = 1; i <= maxNum; i++) {
			int counter=0;
			for(num =i; num>=1; num--) {
				if(i%num==0) {
					counter = counter + 1;
				}
			}
			if (counter ==2) { //Appended the Prime number to the String
				primeNumbers = primeNumbers + i + " ";
			}
		}
		System.out.printf("Prime numbers from 1 to %d are :\n", maxNum);
		System.out.println(primeNumbers);
	}
}