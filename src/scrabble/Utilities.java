package scrabble;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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

	public static Set<Character> alphabetSet(){
		Set<Character> test = new HashSet<Character>();
		for(Character c : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray())
			test.add(c);
		return test;
	}
	
	public static Set<Character> consonantSet(){
		Set<Character> test = new HashSet<Character>();
		for(Character c : "BCDFGHJKLMNPQRSTVWXYZ".toCharArray())
			test.add(c);
		return test;
	}
	
	public static Set<Character> vowelSet(){
		Set<Character> test = new HashSet<Character>();
		for(Character c : "AEIOU".toCharArray())
			test.add(c);
		return test;
	}
	
	public static int countVowels(ArrayList<Tile> rackLeave){
		int count = 0;
		for(Tile t : rackLeave){
			if(Utilities.vowelSet().contains(t.character))
				count++;
		}
		return count;
	}
	
	public static int countConsonants(ArrayList<Tile> rackLeave){
		int count = 0;
		for(Tile t : rackLeave){
			if(Utilities.consonantSet().contains(t.character))
				count++;
		}
		return count;
	}
	
	public static void printCharArray(char[] c){
		for(Character l : c)
			System.out.print(l);
		System.out.println();
	}
	
}
