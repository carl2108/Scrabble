package scrabble;

// pass to VM arguments under run configuration to set program memory allocation "-Xmx1024m"

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {	
		//testTraverseGADDAG();
		//testComputeMoves();		//empty board
		//testComputeMoves2();
		//testComputeMoves3();
		//testComputeCrossSets2();
		//testCalculateScore();
		//testCalculateScore2();
		//testGetUserInput();
		testGame();		//this - greedy AI
		//aiGetMoves();

		System.out.println("\nProgram Terminated");
	}
	
	public static void aiGetMoves(){
		GADDAG g = new GADDAG();
		Board board = new Board();
		board.computeAnchors();
		board.computeCrossSets(board, g.getRoot());
		//Letterbag l = new Letterbag();
		
		//char[] rackArray = {'C', 'A', 'R', 'E', 'R', 'S'};
		ArrayList<Tile> rack = new ArrayList<Tile>();
		rack.add(Tile.valueOf('C'));
		rack.add(Tile.valueOf('A'));
		rack.add(Tile.valueOf('R'));
		rack.add(Tile.valueOf('E'));
		rack.add(Tile.valueOf('R'));
		rack.add(Tile.valueOf('S'));
		
		List<Move> moves = g.findWords(g.getRoot(), rack, board);
		int max = 0;
		Move move_max = new Move();
		System.out.println("Moves: " + moves.size());
		for(Move m: moves){
			int score = board.calculateScore(m);
			if(score > max){
				max = score;
				move_max = m;
			}
			System.out.println("New Move-----\nScore: " + score);
			for(Play p: m){
				System.out.println("Tile: " + p.letter + " | X: " + p.x + " |Y: " + p.y);
			}
		}
		
		System.out.println("Best move: ");
		for(Play p : move_max)
			System.out.println("Tile: " + p.letter + " | X: " + p.x + " |Y: " + p.y);
		System.out.println("Highest Score: " + max);
		
	}
	
	public static void testGame() throws InterruptedException {
		//set up game objects and initialize board
		boolean playerTurn = true;
		int playerScore = 0;
		int aiScore = 0;
		GADDAG g = new GADDAG();
		GUI gui = new GUI();
		Board board = new Board();
		board.computeAnchors();
		board.computeCrossSets(board, g.getRoot());
		board.print();
		board.printAnchors();
		board.printNumCrossSets();
		Letterbag l = new Letterbag();
		
		//set up player objects, racks and initialize
		Player human = new Player("Player");
		Player ai = new Player("Comp");
		l.fillRack(human.rack);		//fill player racks
		l.fillRack(ai.rack);
		human.rack.print();
		gui.refreshRack(human.rack.myRack);	//push human rack to the GUI
		ai.rack.print();
		
		while(l.hasLetters() || (human.rack.myRack.length > 0 && ai.rack.myRack.length > 0)){	//*till end of game!
			if(playerTurn){	//process players turn
				System.out.println("Player Turn");
				//gui.consoleWrite("Players turn...");
				Move m = gui.getUserInput();
				while(m == null){	//keep waiting for move until we get one
					Thread.sleep(100);
					m = gui.getUserInput();
					//System.out.print();
				}
				System.out.println("Got user move");
				gui.resetUserMove();
				//reset move to null so we don't accidentally get the same move twice
				m.printMove();
				Move m_comp = board.completeUserMove(m);	//*currently need to pass isMoveValid() complete and incomplete play - have bool/delim in play to identify if it already exists on the board
				//m_comp.printMove();
				System.out.println("Completed user move");
				if(board.isMoveValid(m, g.getRoot(), m_comp)){	//if the move is valid - play it
					System.out.println("Valid user move");
					String word = new String();
					for(Play p : m_comp)	//get the word as a string
						word = word + p.letter;
					
					int score = board.calculateScore(m_comp);	//move score
					playerScore += score;				//add to player score
					System.out.println("Played: " + word + " | Score: " + score);
					board.placeMove(m_comp);			//place move on the board - print via board.print
					board.print();
					board.computeAnchors();				//*shouldn't have to recompute all anchors and cross sets, just ones that are effected by last move
					board.computeCrossSets(board, g.getRoot());
					playerTurn = false;		//set to computers turn
					human.rack.removeAll(word.toCharArray());
					l.fillRack(human.rack);
					gui.refreshRack(human.rack.myRack);
					gui.addToBoard(m);
				}
				else {	//else if move is invalid - try again
					System.out.println("Invalid user move - try again");
					gui.resetBoard();
					gui.resetUserMove();
					
				}
			}
			else {	////process AI turn
				System.out.println("Computer Turn");
				ArrayList<Tile> rack = new ArrayList<Tile>();		//**convert rack - fix this
				for(Character c : ai.rack.myRack){
					if(c != '_')
						rack.add(Tile.valueOf(c));
				}
				List<Move> moves = g.findWords(g.getRoot(), rack, board);		//find words
				
				int max = 0;
				Move move_max = new Move();								//get highest
				for(Move m: moves){
					int score = board.calculateScore(m);
					if(score > max){
						max = score;
						move_max = m;
					}
				}
				System.out.println("Best move score: " + max);
				String word = new String();
				for(Play p : move_max){
					word = word + p.letter;
					//System.out.println("Tile: " + p.letter + " | X: " + p.x + " |Y: " + p.y);
				}
				
				board.placeMove(move_max);
				board.computeAnchors();
				board.computeCrossSets(board, g.getRoot());
				gui.addToBoard(move_max);
				ai.rack.removeAll(word.toCharArray());
				l.fillRack(ai.rack);
				playerTurn = true;
			}
		}
		
		System.out.println("GAME OVER\nPlayer: " + playerScore + " Computer: " + aiScore);
		
	}
	
	public static void testGetUserInput(){
		GADDAG gad = new GADDAG();
		Board board = new Board();
		board.placeLetter('A', 3, 4);
		board.computeAnchors();
		board.computeCrossSets(board, gad.getRoot());
		GUI g = new GUI();
		
		
		char[] c = {'F', 'H', '_', 'E', 'Q', 'R'};
		g.refreshRack(c);
		
		System.out.println("Waiting...");
		Move m;
		while((m = g.getUserInput()) == null);	//keep waiting for move until we get one
		g.resetUserMove();						//reset move to null so we don't accidentally get the same move twice
		m.printMove();
		Move m_comp = board.completeUserMove(m);
		m_comp.printMove();
		
		System.out.println(board.isMoveValid(m, gad.getRoot(), m_comp));
		
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
	
	//computes moves with empty board
	public static void testComputeMoves(){
		GADDAG g = new GADDAG();
		Board board = new Board();
		ArrayList<Tile> rack = new ArrayList<Tile>();
		rack.add(Tile.valueOf('R'));
		rack.add(Tile.valueOf('A'));
		rack.add(Tile.valueOf('T'));
		System.out.println(rack.toString());
		
		board.print();
		board.computeAnchors();
		board.printAnchors();
		board.computeCrossSets(board, g.getRoot());
		board.printNumCrossSets();		//shouldn't be any
		
		List<Move> moves = g.findWords(g.getRoot(), rack, board);
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
		board.placeLetter('M', 7, 7);
		
		ArrayList<Tile> rack = new ArrayList<Tile>();
		//rack.add(Tile.valueOf('E'));
		rack.add(Tile.valueOf('M'));
		rack.add(Tile.valueOf('O'));
		rack.add(Tile.valueOf('N'));
		rack.add(Tile.valueOf('K'));
		//rack.print();
		
		System.out.println("Board");
		board.print();
		board.computeAnchors();
		System.out.println("Anchors:");
		board.printAnchors();
		board.computeCrossSets(board, g.getRoot());
		System.out.println("Cross Set Numbers:");
		board.printNumCrossSets();
		
		List<Move> moves = g.findWords(g.getRoot(), rack, board);
		System.out.println("Moves: " + moves.size());
		for(Move m: moves){
			System.out.println("New Move-------");
			for(Play p: m){
				System.out.println("Tile: " + p.letter + " | X: " + p.x + " |Y: " + p.y);
			}
		}
	}
	
	//tests PA_ABLE for Y and R, tiles either side
	public static void testComputeCrossSets2(){
		Board board = new Board();
		GADDAG g = new GADDAG();
		board.placeLetter('A', 6, 7);
		board.placeLetter('P', 5, 7);
		board.placeLetter('A', 8, 7);
		board.placeLetter('B', 9, 7);
		board.placeLetter('L', 10, 7);
		board.placeLetter('E', 11, 7);
		board.print();
		board.computeCrossSets(board, g.getRoot());
		board.printNumCrossSets();
	}
	
	//try using full dictionary and give 6 letters
	public static void testComputeMoves3() {
		GADDAG g = new GADDAG();
		Board board = new Board();

		board.placeLetter('T', 7, 7);
		
		ArrayList<Tile> rack = new ArrayList<Tile>();

		rack.add(Tile.valueOf('E'));
		rack.add(Tile.valueOf('A'));
		rack.add(Tile.valueOf('C'));
		rack.add(Tile.valueOf('L'));
		rack.add(Tile.valueOf('P'));
		rack.add(Tile.valueOf('T'));
		
		System.out.println("Board");
		board.print();
		board.computeAnchors();
		System.out.println("Anchors:");
		board.printAnchors();
		board.computeCrossSets(board, g.getRoot());
		System.out.println("Cross Set Numbers:");
		board.printNumCrossSets();
		
		//System pause!
		Scanner scan = new Scanner(System.in);
		System.out.println("Pause...");
		String s = scan.nextLine();
		
		List<Move> moves = g.findWords(g.getRoot(), rack, board);
		System.out.println("Moves: " + moves.size());
		for(Move m: moves){
			System.out.println("New Move-------");
			for(Play p: m){
				System.out.println("Tile: " + p.letter + " | X: " + p.x + " |Y: " + p.y);
			}
		}
	}
	
	public static void testCalculateScore(){
		Board board = new Board();
		//board.placeLetter('A', 7, 7);
		board.print();
		board.computeAnchors();
		
		Play p1 = new Play(7, 7, 'S');
		Play p2 = new Play(7, 8, 'E');
		Play p3 = new Play(7, 9, 'R');
		Play p4 = new Play(7, 10, 'A');
		Play p5 = new Play(7, 11, 'C');
		
		Move m = new Move();
		m.addPlay(p1);
		m.addPlay(p2);
		m.addPlay(p3);
		m.addPlay(p4);
		m.addPlay(p5);
		
		System.out.println("Score: " + board.calculateScore(m));
	}
	
	public static void testCalculateScore2(){
		Board board = new Board();
		board.placeLetter('A', 7, 7);
		board.print();
		board.computeAnchors();
		board.printAnchors();
		
		Play p1 = new Play(6, 7, 'Q');
		Play p2 = new Play(5, 7, 'B');
		Play p3 = new Play(7, 7, 'A');
		
		Move m = new Move();
		m.addPlay(p1);
		m.addPlay(p2);
		m.addPlay(p3);
		
		System.out.println("Score: " + board.calculateScore(m));
		board.placeMove(m);
		board.computeAnchors();
		board.print();
		board.printAnchors();
		
//		Play p4 = new Play(6, 9, 'F');
//		Play p5 = new Play(7, 9, 'E');
//		Play p6 = new Play(8, 9, 'Z');
//		Play p7 = new Play(9, 9, 'A');
//		
//		Move m1 = new Move();
//		m.addPlay(p4);
//		m.addPlay(p5);
//		m.addPlay(p6);
//		m.addPlay(p7);
//		
//		System.out.println("Score m1: " + board.calculateScore(m1));
//		board.placeMove(m1);
//		board.computeAnchors();
//		board.print();
		
		Play p10 = new Play(7, 7, 'A');
		Play p20 = new Play(7, 8, 'B');
		Play p30 = new Play(7, 9, 'Q');
		
		Move m2 = new Move();
		m2.addPlay(p10);
		m2.addPlay(p20);
		m2.addPlay(p30);
		
		System.out.println("Score: " + board.calculateScore(m2));
		board.placeMove(m2);
		board.computeAnchors();
		board.print();
		board.printAnchors();
	}
}