package scrabble;

import java.util.Set;

public class Square {
	
	private int letterMultiplier;
	private int wordMultiplier;
	private Set<Tile> legalSet;	//*dunno what this is for? - possibly calculating cross sets?
	private Tile tile;
	private boolean isAnchor;
	private String str;
	
	public Square(String code) {
		this.str = code;
		this.tile = null;
		this.isAnchor = false;
		legalSet = null;
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
//
//	public Square() {
//		this(1, 1);
//	}
//
//	public Square(int letterMultiplier, int wordMultiplier) {
//		this(letterMultiplier, wordMultiplier, new HashSet<Tile>());
//	}
//
//	public Square(int letterMultiplier, int wordMultiplier, Set<Tile> legalSet) {
//		this(letterMultiplier, wordMultiplier, legalSet, null);
//	}
//
//	public Square(int letterMultiplier, int wordMultiplier, Set<Tile> legalSet,
//			Character tile) {
//		super();
//		this.letterMultiplier = letterMultiplier;
//		this.wordMultiplier = wordMultiplier;
//		this.legalSet = legalSet;
//		setTile(tile);
//		this.isAnchor = false;
//	}

	public boolean isAnchor() {
		return isAnchor;
	}

	public void setAnchor(boolean isAnchor) {
		this.isAnchor = isAnchor;
	}

	public Tile getTile() {
		return tile;
	}

	public boolean hasTile() {
		return tile != null;	//return true if tile is not equal to null?
	}

//	public void setTile(Character tile) {
//		if (tile == null) {
//			this.tile = null;
//		} else {
//			this.tile = Tile.valueOf(Character.toLowerCase(tile));
//		}
//	}

	public int getLetterMultiplier() {
		return letterMultiplier;
	}

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

	
	//*dunno what these are for?
	public boolean legal(Tile t) {
		if (legalSet.isEmpty()) {
			return true;
		}
		return legalSet.contains(t);
	}

	public Set<Tile> getLegalSet() {
		return legalSet;
	}

	public void setLegalSet(Set<Tile> legalSet) {
		this.legalSet = legalSet;
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
