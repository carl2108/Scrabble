package scrabble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Board {
	
	Square[][] board;
	File f = new File("text//scrabbleBoard.txt");
	
	//Constructor
	public Board() {
		board = new Square[15][15];
		
		try {
			Scanner scan = new Scanner(f).useDelimiter(" |\n");
			for(int j=0; j<15; j++){
				for(int i=0; i<15; i++){
					board[i][j] = new Square(scan.next());
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void print(){
		for(int j=0; j<15; j++){
			for(int i=0; i<15; i++){
				System.out.print(board[i][j].getString() + " ");
			}
			System.out.println();
		}
	}
	
	
}
