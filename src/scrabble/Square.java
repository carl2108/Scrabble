package scrabble;

import java.util.HashSet;
import java.util.Set;

public class Square {
	
	private int letterMultiplier;
	private int wordMultiplier;
	public Set<Character> legalSet;	//*dunno what this is for? - possibly calculating cross sets?
	private Tile tile;
	private boolean isAnchor;
	private String str;
	public int x, y;
	public char letter;
	
	public Square(String code, int i, int j) {
		this.str = code;
		this.tile = null;
		this.isAnchor = false;
		this.legalSet = new HashSet<Character>();
		this.x = i;
		this.y = j;
		this.letter = '!';
		if(code == "__"){
			this.wordMultiplier = 1;
			this.letterMultiplier = 1;
			System.out.println("HI");
		}
		else if(code.substring(1) == "w"){
			this.wordMultiplier = Integer.parseInt(code.substring(0, 1));
			this.letterMultiplier = 1;
		}
		else if(code.substring(1) == "l"){
			this.wordMultiplier = 1;
			this.letterMultiplier = Integer.parseInt(code.substring(0, 1));
		}
		else if(code.substring(1) == "_"){
			System.out.println("true");
		}
		//else throw new IllegalArgumentException("Invalid Code");
	}
	
	public char getLetter(){
		return this.letter;
	}

//	public void addAllToLegal(Set<Character> endSet) {
//		for (Character c : endSet) {
//			legalSet.add(Tile.value);
//		}
//
//	}
//
//	public void addLegalSet(char c) {
//		this.getLegalSet().add(Tile.valueOf(c));
//	}
	
	//check if its an anchor square
	public boolean isAnchor() {
		return isAnchor;
	}
	
	//set anchor/not
	public void setAnchor(boolean isAnchor) {
		this.isAnchor = isAnchor;
	}
	
	//get tile on board square
	public Tile getTile() {
		return tile;
	}
	
	public boolean hasTile() {
		return letter != '!';	//return true if tile is not equal to null?
	}
	
	//puts letter on board square
	public void setLetter(char letter) {
		this.letter = letter;
		this.str = " " + letter;
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
	
	public String getString(){
		return str;
	}

//	public boolean legal(Character c) {
//		if (legalSet.isEmpty()) {
//			return true;
//		}
//		return legalSet.contains(Tile.valueOf(c));
//	}

	
	//*dunno what these are for? - check if board square is empty
	public boolean legal(Tile t) {
		//System.out.println(t);
		if (legalSet.isEmpty()) {
			return true;
		}
		return legalSet.contains(t);
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

	public void addLegalSet(Character t) {
		this.getLegalSet().add(t);
	}

	public String getCode() {
		if (this.tile != null) {
			return tile.toString() + "_";
		}
		if (this.letterMultiplier > 1) {
			return letterMultiplier + "l";
		}
		if (this.wordMultiplier > 1) {
			return wordMultiplier + "w";
		}
		return "__";
	}

}
