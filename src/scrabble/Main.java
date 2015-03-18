package scrabble;

// pass to VM arguments under run config to set program memory allocation "-Xmx1024m"

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
		//testPlaceLetter();
		//testPlaceTile2();
		//testMove();
		//testGUIRack();
		//testTileRackImage();
		testComputeMoves();
		//testComputeMoves2();
		//testComputeCrossSets();
		//testComputeCrossSets2();
		//testComputeCrossSets3();
		//test();

		System.out.println("Program Terminated");
	}
	
	public static void testLetterbag(){
		System.out.println("*****Letterbag Test*****\n");
		Letterbag l = new Letterbag();
		while(l.hasLetters()) {
			char t = l.draw();
			System.out.println("Letter: " + t + " | Value: " + Tile.valueOf(t));
		}
	}
	/*
	public static void testFindWords() {
		System.out.println("*****Find Words Test*****\n");
		GADDAG test = new GADDAG();
		
		ArrayList<Character> letters = new ArrayList<Character>(Arrays.asList('H', 'A', 'L', 'L', 'A', 'T'));
		//System.out.println("Traverse: " + Arrays.toString(test.traverse(test.getRoot()).toArray()));
		
		Set<String> words = test.findWords(test.getRoot(), letters);
		System.out.println("All Words:\n" + Arrays.toString(words.toArray()));
		
	}
	*/
	
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
	
	public static void testPlaceLetter(){
		Board b = new Board();
		b.print();
		b.placeLetter('A', 7, 7);
		b.print();
	}
	
	public static void testPlaceTile2(){
		Board b = new Board();
		b.print();
		b.placeLetter('B', b.square[7][7]);
		b.print();
	}
	
	public static void testMove(){
		Board board = new Board();
		board.print();
		
		//make a move to be played
		Move move = new Move();
		//move.addPlay(c, 7, 7, board);
		//move.addPlay(a, 8, 7, board);
		//move.addPlay(t, 9, 7, board);
		
		System.out.println("move created");
		
		//place move and update anchors
		//board.placeMove(move);
		//board.print();
		board.computeAnchors();
		board.printAnchors();
	}
	
	public static void testTileRackImage(){
		Rack r = new Rack();
		r.add('A');
		r.print();
	}
	
	//computes moves with empty board
	public static void testComputeMoves(){
		GADDAG g = new GADDAG();
		Board board = new Board();
		//Rack rack = new Rack();
		ArrayList<Tile> rack = new ArrayList<Tile>();
		rack.add(Tile.valueOf('R'));
		rack.add(Tile.valueOf('A'));
		rack.add(Tile.valueOf('T'));
		System.out.println(rack.toString());
		
		board.print();
		board.computeAnchors();
		board.printAnchors();
		board.computeCrossSets(board, g.getRoot());
		board.printNumCrossSets(board);		//shouldn't be any
		
		
		Set<Move> moves = g.findWords(g.getRoot(), rack, board);
		System.out.println("Moves!");
		for(Move m: moves){
			System.out.println("New Move-------");
			for(Play p: m){
				System.out.println("Tile: " + p.letter + " | X: " + p.x + " |Y: " + p.y);
			}
		}
	}

	//computes moves with letters on the board
	public static void testComputeMoves2(){
		GADDAG g = new GADDAG();
		Board board = new Board();

		//board.placeLetter('C', 6, 7);
		//board.placeLetter('N', 7, 7);
		board.placeLetter('T', 7, 7);
		
		ArrayList<Tile> rack = new ArrayList<Tile>();
		//rack.add(Tile.valueOf('S'));
		rack.add(Tile.valueOf('R'));
		rack.add(Tile.valueOf('A'));
		//rack.print();
		//System.out.println(rack.toString());
		
		System.out.println("Board");
		board.print();
		board.computeAnchors();
		System.out.println("Anchors:");
		board.printAnchors();
		board.computeCrossSets(board, g.getRoot());
		System.out.println("Cross Set Numbers:");
		board.printNumCrossSets(board);
		
		//board.square(7, 8).printLegalSet();
		
		//System.out.println(board.square[7][7].getLegalSet().toArray()[0].toString());
		
		Set<Move> moves = g.findWords(g.getRoot(), rack, board);
		System.out.println("Moves: " + moves.size());
		for(Move m: moves){
			System.out.println("New Move-------");
			for(Play p: m){
				System.out.println("Tile: " + p.letter + " | X: " + p.x + " |Y: " + p.y);
			}
		}
//		board.placeMove(moves.iterator().next());
//		board.computeAnchors();
//		board.printAnchors();
//		board.print();
	}

//	public static void testComputeCrossSets(){
//		Board board = new Board();
//		GADDAG g = new GADDAG();
//		board.placeLetter('A', 7, 7);
//		board.print();
//		board.computeCrossSets(board, g.getRoot());
//		System.out.println("Size: " + board.square(8, 7).legalSet.size());
//		System.out.println("Size: " + board.square(7, 8).legalSet.size());
//		board.printNumCrossSets(board);
//		while(board.square(7, 7).legalSet.iterator().hasNext())
//			System.out.println(board.square(7, 7).getLegalSet().iterator().next());
//	}
	
	//tests PA_ABLE for Y and R, tiles either side
//	public static void testComputeCrossSets2(){
//		Board board = new Board();
//		GADDAG g = new GADDAG();
//		board.placeLetter('A', 6, 7);
//		board.placeLetter('P', 5, 7);
//		board.placeLetter('A', 8, 7);
//		board.placeLetter('B', 9, 7);
//		board.placeLetter('L', 10, 7);
//		board.placeLetter('E', 11, 7);
//		board.print();
//		board.computeCrossSets(board, g.getRoot());
//		System.out.println("Size: " + board.square(8, 7).legalSet.size());
//		System.out.println("Size: " + board.square(7, 8).legalSet.size());
//		board.printNumCrossSets(board);
//	}
	
//	public static void testComputeCrossSets3(){
//		Board board = new Board();
//		GADDAG g = new GADDAG();
//		//board.placeLetter('A', 6, 7);
//		board.placeLetter('C', 7, 7);
//		//board.placeLetter('T', 7, 7);	//then without T
//		
//		board.print();
//		board.computeCrossSets(board, g.getRoot());
//		System.out.println("Size: " + board.square(8, 7).legalSet.size());
//		System.out.println("Size: " + board.square(7, 8).legalSet.size());
//		board.printNumCrossSets(board);
//	}
	public static void test(){
		Board board = new Board();
		GADDAG g = new GADDAG();
		List<Character> rack = new ArrayList<Character>();
		rack.add('A');
		rack.add('C');
		rack.add('T');
		rack.add('S');
		
	}
}