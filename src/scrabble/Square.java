package scrabble;

import java.util.HashSet;
import java.util.Set;

public class Square {
	
	private int letterMultiplier;
	private int wordMultiplier;
	public Set<Character> legalSet;
	private boolean isAnchor;
	public int x, y;
	public char letter;
	
	public Square(String code, int i, int j) {
		this.isAnchor = false;
		this.legalSet = new HashSet<Character>();
		this.x = i;
		this.y = j;
		this.letter = '!';
		if(code.charAt(1) == '_'){	
			this.wordMultiplier = 1;
			this.letterMultiplier = 1;
		}
		else if(code.charAt(1) == 'w'){
			this.wordMultiplier = Integer.parseInt(code.substring(0, 1));
			this.letterMultiplier = 1;
		}
		else if(code.charAt(1) == 'l'){
			this.wordMultiplier = 1;
			this.letterMultiplier = Integer.parseInt(code.substring(0, 1));
		}
		else throw new IllegalArgumentException("Invalid Code");
	}
	
	public char getLetter(){
		return this.letter;
	}
	
	//check if its an anchor square
	public boolean isAnchor() {
		return isAnchor;
	}
	
	//set anchor/not
	public void setAnchor(boolean isAnchor) {
		this.isAnchor = isAnchor;
	}
	
	public boolean hasTile() {
		return letter != '!';
	}
	
	//puts letter on board square
	public void setLetter(char letter) {
		this.letter = letter;
		this.isAnchor = false;
	}
	
	//return board square letter multiplier
	public int getLetterMultiplier() {
		return letterMultiplier;
	}

	//return board square word multiplier
	public int getWordMultiplier() {
		return wordMultiplier;
	}

	public boolean legal(Character c) {
		if(legalSet.isEmpty()) {		//*what if no letters can be legally placed here??
			return true;
		}
		return legalSet.contains(c); //return legalSet.contains(Tile.valueOf(c)); - *was this
	}

	public Set<Character> getLegalSet() {
		return legalSet;
	}

	public void setLegalSet(Set<Character> legalSet) {
		this.legalSet = legalSet;
	}
	
	public void addAllToLegal(Set<Character> endSet) {
		for (Character t : endSet) {
			legalSet.add(t);
		}
	}

	public void addLegalSet(char t) {
		this.getLegalSet().add(t);
	}
	
	public void printLegalSet(){
		for(char c : legalSet){
			System.out.print(c);
		}
		System.out.println();
	}
	
	public String toString() {
		if(this.letter != '!') {
			return " " + letter;
		}
		else if(this.letterMultiplier > 1) {
			return letterMultiplier + "l";
		}
		else if(this.wordMultiplier > 1) {
			return wordMultiplier + "w";
		}
		else
			return "__";
	}
	
}
