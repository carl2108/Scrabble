package scrabble;

import java.util.ArrayList;
import java.util.List;

public class Move {
	
	int score;
	List<Tile> tiles;
	List<Square> squares;
	
	public Move(){
		score = 0;
		tiles = new ArrayList<Tile>();
		squares = new ArrayList<Square>();
	}
	
	public void append(Tile t, Square s){
		this.tiles.add(t);
		this.squares.add(s);
	}

}
