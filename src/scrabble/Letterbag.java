package scrabble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Letterbag {
	
	Stack<Character> st;	//Stack used to represent the letter bag
	
	//Constructor
	public Letterbag() {
		st = new Stack<Character>();
		
		File f = new File("text//testLetters.txt");		//Read letters from text file provided
		try {
			Scanner scan = new Scanner(f).useDelimiter(" ");
			
			while(scan.hasNext()) {
				char c = scan.next().charAt(0);
				//int throwAway = scan.nextInt();	//remove this later!
				st.push(c);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Pops and returns a random letter from the letter stack - *How to handle when stack is empty??
	public char draw() {
		Random rand = new Random();
		int r = rand.nextInt(st.size());
		char c = st.get(r);
		st.removeElementAt(r);
		return c;
	}
	
	//Swap tiles - *How to handle when stack is empty??
	public char swap(char b) {
		char t = draw();
		st.push(b);
		return t;
	}
	
	public int size(){
		return st.size();
	}
	
	public boolean hasLetters(){
		return !st.empty();
	}
	
	public void fillRack(Rack r){
		int i=0;
		for(i=0; i<6; i++){
			if(r.myRack[i] == '_' && hasLetters()){
				r.add(draw());
			}
		}
	}
}
