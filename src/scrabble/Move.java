package scrabble;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//public class Move {
//	
//	int score;
//	List<Tile> tiles;
//	List<Square> squares;
//	
//	public Move(){
//		score = 0;
//		tiles = new ArrayList<Tile>();
//		squares = new ArrayList<Square>();
//	}
//	
//	public void append(Tile t, Square s){
//		this.tiles.add(t);
//		this.squares.add(s);
//	}
//
//}

public class Move implements Iterable<Play> {

	Set<Play> plays;
	int score;

	public Move() {
		plays = new HashSet<Play>();
	}

	public Move(Move other) {
		this.plays = new HashSet<Play>(other.plays);
	}

	public void addPlay(Tile c, int i, int j, Board board) {
		plays.add(new Play(board.get(j, i), c));	//get(j, i) because of way hashSets work? Like Vindinium project?
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public int size() {
		return plays.size();
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		for (Play p : plays) {
			out.append(p.tile.letter);// + " " + p.i + " " + p.j + "\n");
		}
		return out.toString();
	}

	@Override
	public Iterator<Play> iterator() {
		return plays.iterator();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Move) {
			return plays.equals(((Move) obj).plays);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return plays.hashCode();
	}
}
