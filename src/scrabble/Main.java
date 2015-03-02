package scrabble;

// pass to VM arguments under run config to set program memory allocation "-Xmx1024m"

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {	
		
		//testLetterbag();
		//testFindWords();
		//testCheckWords();
		//testBoard();
		//testTraverseGADDAG();
		//testCheckAllWords();
		//testGUI();
		//testPlaceTile();
		//testPlaceTile2();
		//testMove();
		testGUIRack();
		//testTileRackImage();

		System.out.println("Program Terminated");
	}
	
	public static void testLetterbag(){
		System.out.println("*****Letterbag Test*****\n");
		Letterbag l = new Letterbag();
		while(l.hasLetters()) {
			Tile t = l.draw();
			System.out.println("Letter: " + t.letter + " | Value: " + t.value);
		}
	}
	
	public static void testFindWords() {
		System.out.println("*****Find Words Test*****\n");
		GADDAG test = new GADDAG();
		
		ArrayList<Character> letters = new ArrayList<Character>(Arrays.asList('H', 'A', 'L', 'L', 'A', 'T'));
		//System.out.println("Traverse: " + Arrays.toString(test.traverse(test.getRoot()).toArray()));
		
		Set<String> words = test.findWords(test.getRoot(), letters);
		System.out.println("All Words:\n" + Arrays.toString(words.toArray()));
		
	}
	
	//test GUI and console writing function
	public static void testGUI(){
		System.out.println("*****GUI Test*****\n");
		GUI g = new GUI();
		Scanner scan = new Scanner(System.in);
		while(true){
			String s = scan.nextLine();
			g.consoleWrite(s);
		}
		////////////Try inputing and output through GUI console window only to get rid of focus manager messages?
	}
	
	public static void testCheckWords() {
		System.out.println("*****Check Words Test*****\n");
		GADDAG test = new GADDAG();
		Scanner scan = new Scanner(System.in);
		while (scan.hasNext()) {
			String s = scan.next().toUpperCase();
	    	System.out.println("Checking word: " + s + " || Result: " + test.getRoot().contains(s));
	    }
	}
	
	public static void testTextToUppercase(){
		File f = new File("text//OSPD1.txt");
		Utilities.upperCase(f, "OSPD.txt");
	}
	
	public static void testBoard() {
		Board b = new Board();
		b.print();
	}
	
	//allows traversal of GADDAG by inputing transition letters
	public static void testTraverseGADDAG(){
		GADDAG test = new GADDAG();
		GADDAGNode curr = test.getRoot();
		System.out.println(curr.toString());
		Scanner in = new Scanner(System.in);
		String word = new String();
		boolean forwards = false;
		while(in.hasNext()){
			char c = in.next().toUpperCase().charAt(0);
			try{
				curr = curr.get(c);
				if(c == '@')
					forwards = true;
				else if(!forwards)
					word = c + word;
				else
					word = word + c;
				System.out.println("Word:\n" + word + "\n" + curr.toString());
			}
			catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void testCountGADDAGWords(){
		GADDAG test = new GADDAG();
		
	}
	
	public static void testCheckAllWords(){
		System.out.println("*****Test Check All Words****\n");
		GADDAG test = new GADDAG();
		List<String> missing = new ArrayList<String>();
		try {
			Scanner in = new Scanner(new File("text//OSPD.txt"));
			boolean exist;
			while(in.hasNextLine()){
				String s = in.nextLine().toUpperCase();
				exist = test.rootMin.contains(s);
				if(!exist)
					System.out.println("Checking word: " + s + " || Result: " + exist);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void testGUIRack(){
		GUI g = new GUI();
		char[] c = {'f', 'h', 'x', 'e', 'q', 'r'};
		g.refreshRack(c);
	}
	
	public static void testPlaceTile(){
		Board b = new Board();
		b.print();
		Tile t = new Tile('A', 1);
		b.placeTile(t, 7, 7);
		b.print();
	}
	
	public static void testPlaceTile2(){
		Board b = new Board();
		b.print();
		Tile t = new Tile('B', 1);
		b.placeTile(t, b.square[7][7]);
		b.print();
	}
	
	public static void testMove(){
		Board board = new Board();
		board.print();
		Move m = new Move();
		Tile c = new Tile('C', 2);
		Tile a = new Tile('A', 1);
		Tile t = new Tile('T', 1);
		m.append(c, board.square[7][7]);
		m.append(a, board.square[8][7]);
		m.append(t, board.square[9][7]);
		board.placeMove(m);
		board.print();
	}
	
	public static void testTileRackImage(){
		Tile a = new Tile('A', 1);
		Rack r = new Rack();
		r.add(a);
		r.print();
	}
	
}
