package scrabble;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Move implements Iterable<Play> {

	Set<Play> plays;
	int score;

	public Move() {
		plays = new HashSet<Play>();
	}

	public Move(Move other) {
		this.plays = new HashSet<Play>(other.plays);
	}
	
	public void addPlay(int x, int y, char l){
		plays.add(new Play(x, y, l));
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
}
