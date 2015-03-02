package scrabble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Board {
	
	Square[][] square;
	File f = new File("text//scrabbleBoard.txt");
	
	//Constructor
	public Board() {
		square = new Square[15][15];
		
		try {
			Scanner scan = new Scanner(f).useDelimiter(" |\n");
			for(int j=0; j<15; j++){
				for(int i=0; i<15; i++){
					square[i][j] = new Square(scan.next());
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void print(){
		for(int j=0; j<15; j++){
			for(int i=0; i<15; i++){
				System.out.print(square[i][j].getString() + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void placeTile(Tile t, int x, int y){
		square[x][y].setTile(t);
	}
	
	public void placeTile(Tile t, Square s){
		s.setTile(t);
	}
	
	public void placeMove(Move m){
		int i;
		for(i=0; i<m.tiles.size(); i++){
			m.squares.get(i).setTile(m.tiles.get(i));	//set each tile in each corresponding square position
		}
	}
	
}
