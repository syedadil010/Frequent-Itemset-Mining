import java.io.IOException;
import java.util.Scanner;

public class Driver_class {
	
	public static void main(String args[]) throws IOException
	{
		Scanner sc=new Scanner(System.in);
		int mod=Integer.parseInt(sc.nextLine());
		Long startTime = System.currentTimeMillis();
		PCY driverObject=new PCY(mod);
		driverObject.pcyDriverMethod();
		long endTime = System.currentTimeMillis();
		long runTime = endTime - startTime;
		System.out.println("Runtime for pattern in milliseconds: " + runTime);
	}
	

}
