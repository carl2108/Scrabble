package scrabble;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Utilities {
	
	public static void upperCase(File in, String name){
		try {
			Scanner scanIn = new Scanner(in);
			File out = new File("text//" + name);
			
			if (!out.exists()) {
				try {
					out.createNewFile();
					FileWriter fw = new FileWriter(out.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					while(scanIn.hasNext())
						bw.write(scanIn.next().toUpperCase() + "\n");
					bw.close();
					 
					System.out.println("Done");
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
