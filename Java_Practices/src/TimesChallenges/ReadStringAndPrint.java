package TimesChallenges;
import java.io.*;
import java.util.*;

public class ReadStringAndPrint {

	/* Read input from STDIN. Print your output to STDOUT*/

	   public static void main(String args[] ) throws Exception 
	   {

			//Write code here
			//Read_5_STRINGS();
		    Read_STRING_INT_FLOAT();
	   
	   }

	   public static void Read_5_STRINGS()
	   {
		    Scanner readString = new Scanner(System.in);
		    StringBuilder str = new StringBuilder();
		    for (int i=0;i<=4;i++)
		    {
		        str = str.append(readString.nextLine()+"\n");
		    }
		    
		    System.out.println("The Final String is: " + str);
		    
		    readString.close();
		}
	   
	   public static void Read_STRING_INT_FLOAT()
	   {
		   Scanner readObject = new Scanner(System.in);
		   StringBuilder str = new StringBuilder();
		   
		   //Append the string
		   str.append(readObject.nextLine()+"\n");
		   str.append(readObject.nextInt()+"\n");
		   str.append(String.format ("%.2f", readObject.nextFloat())+"\n");
		   
		   System.out.print(str);
		   readObject.close();
		   
	   }

}// class end	   
