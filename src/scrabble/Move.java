package scrabble;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Move implements Iterable<Play> {

	List<Play> plays;
	int score;

	public Move() {
		plays = new ArrayList<Play>();
	}

	public Move(Move other) {
		this.plays = new ArrayList<Play>(other.plays);
	}
	
	public void addPlay(int x, int y, char l){
		plays.add(new Play(x, y, l));
	}
	
	public void addPlay(int x, int y, char l, int pos){
		plays.add(pos, new Play(x, y, l));
	}
	
	public void addPlay(Play p){
		plays.add(p);
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

//	@Override
//	public String toString() {
//		StringBuilder out = new StringBuilder();
//		for (Play p : plays) {
//			out.append(p.tile.letter);// + " " + p.i + " " + p.j + "\n");
//		}
//		return out.toString();
//	}

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
	
	public void printMove() {
		System.out.println("New Move-----");
		for(Play p : this.plays)
			System.out.println("Letter: " + p.letter + " X: " + p.x + " Y: " + p.y);
	}
	
	public boolean isHorizontal(Board board) {
		if(this.plays.size() == 1){
			Play p = this.plays.get(0);
			if(board.hasTile(p.x - 1, p.y) || board.hasTile(p.x + 1, p.y))
				return true;
			else
				return false;
		}
		if(this.plays.get(0).y == this.plays.get(1).y)
			return true;
		else return false;
	}
}
