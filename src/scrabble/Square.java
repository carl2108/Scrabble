package scrabble;

import java.util.HashSet;
import java.util.Set;

public class Square {
	
	private int letterMultiplier;
	private int wordMultiplier;
	public Set<Character> legalHorizontalSet, legalVerticalSet;
	private boolean isAnchor;
	public int x, y;
	public char letter;
	
	public Square(String code, int i, int j) {
		this.isAnchor = false;
		this.legalHorizontalSet = Utilities.alphabetSet();
		this.legalVerticalSet = Utilities.alphabetSet();
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

	public boolean legalHorizontal(Character c) {		//might be easier to initialize with A-Z and then recalculate as needed?
		return legalHorizontalSet.contains(c);
	}
	
	public boolean legalVertical(Character c){
		return legalVerticalSet.contains(c);
	}

	public Set<Character> getLegalHorizontalSet() {
		return legalHorizontalSet;
	}
	
	public Set<Character> getLegalVerticalSet() {
		return legalVerticalSet;
	}

	public void setLegalHorizontalSet(Set<Character> legalSet) {
		this.legalHorizontalSet = legalSet;
	}
	
	public void setLegalVerticalSet(Set<Character> legalSet) {
		this.legalVerticalSet = legalSet;
	}
	
	public void addAllToLegalHorizontal(Set<Character> endSet) {
		for (Character t : endSet) {
			if(Utilities.alphabetSet().contains(t))
				legalHorizontalSet.add(t);
		}
	}
	
	public void addAllToLegalVertical(Set<Character> endSet) {
		for(Character t : endSet) {
			if(Utilities.alphabetSet().contains(t))
				legalVerticalSet.add(t);
		}
	}

	public void addLegalHorizontalSet(char t) {
		this.getLegalHorizontalSet().add(t);
	}
	
	public void addLegalVerticalSet(char t) {
		this.getLegalVerticalSet().add(t);
	}
	
	public void printLegalHorizontalSet(){
		System.out.println("Legal Horizontal Set: ");
		for(char c : legalHorizontalSet){
			System.out.print(c);
		}
		System.out.println();
	}
	
	public void printLegalVerticalSet(){
		System.out.println("Legal Vertical Set: ");
		for(char c : legalVerticalSet){
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
