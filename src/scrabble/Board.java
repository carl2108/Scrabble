package scrabble;
public class Board {
	
	char[][] board;
	boolean[][] places;
	
	//Constructor
	public Board() {
		board = new char[15][15];
		places = new boolean[15][15];
		places[7][7] = true;		//At the start of a game only the starting square can be hooked into
	}
	
	
	
}
