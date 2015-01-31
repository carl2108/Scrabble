package scrabble;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Letterbag {
	
	Stack<Tile> st;	//Stack used to represent the letter bag
	
	//Constructor
	public Letterbag() {
		st = new Stack<Tile>();
		
		File f = new File("text//letters.txt");		//Read letters from text file provided
		try {
			Scanner scan = new Scanner(f).useDelimiter(" ");
			
			while(scan.hasNext()) {
				Tile t = new Tile(scan.next().charAt(0), scan.nextInt());
				st.push(t);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Pops and returns a random letter from the letter stack - *How to handle when stack is empty??
	public Tile draw() {
		Random rand = new Random();
		int r = rand.nextInt(st.size());
		Tile t = st.get(r);
		st.removeElementAt(r);
		return t;
	}
	
	//Swap tiles - *How to handle when stack is empty??
	public Tile swap(Tile b) {
		Tile t = draw();
		st.push(b);
		return t;
	}
	
}
