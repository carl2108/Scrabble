package scrabble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Board {
	
	public Square[][] square;
	File f = new File("text//scrabbleBoard.txt");
	
	public static final int width = 15;
	public static final int height = 15;
	
	//Constructor
	public Board() {
		square = new Square[15][15];
		
		try {
			Scanner scan = new Scanner(f).useDelimiter(" |\n");
			for(int j=0; j<15; j++){
				for(int i=0; i<15; i++){
					square[i][j] = new Square(scan.next(), i, j);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		square[7][7].setAnchor(true);
	}
	
	public void print(){
		for(int j=0; j<15; j++){
			for(int i=0; i<15; i++){
				System.out.print(square[i][j].toString() + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void placeLetter(char l, int x, int y){
		square[x][y].setAnchor(false);
		square[x][y].setLetter(l);
		square[x][y].legalHorizontalSet.clear();
		square[x][y].legalVerticalSet.clear();
	}
	
	public void placeMove(Move m){
		List<Play> list = new ArrayList<Play>(m.plays);
		for(Play p : list)
			square(p.x, p.y).setLetter(p.letter);
	}
	
	public void computeAnchors() {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				this.square(i, j).setAnchor(isValidAnchor(i, j));
			}
		}
	}
	
	public Square square(int i, int j) {
		return square[i][j];
	}
	
	public char get(int i, int j){
		return square[i][j].getLetter();
	}
	
	public Character getTile(int i, int j) {
		return square[i][j].letter;
	}

	private boolean isValidAnchor(int i, int j) {
		if (!this.hasTile(i, j)) {
//			if (i == 15 / 2 && j == 15 / 2) {
//				return true;
//			}
			if (hasTile(i - 1, j) || hasTile(i + 1, j) || hasTile(i, j + 1)
					|| hasTile(i, j - 1)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasTile(int i, int j) {
		try {
			return square[i][j].hasTile();
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public void printAnchors(){
		System.out.print("   ");
		for(int x=0; x<15; x++)
			System.out.print(" " + x);
		System.out.println();
		
		for(int j=0; j<15; j++){
			String temp = Integer.toString(j);
			if(temp.length() < 2)
				temp = " " + temp;
			System.out.print(temp + "|");
			
			for(int i=0; i<15; i++){
				if(square[i][j].isAnchor())
					System.out.print(" 1");
				else
					System.out.print(" 0");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void printNumCrossSets(Board board){
		System.out.print("Horizontal\n   ");
		for(int x=0; x<15; x++)
			System.out.print(" " + x);
		System.out.println();
		
		for(int j=0; j<15; j++){
			String temp = Integer.toString(j);
			if(temp.length() < 2)
				temp = " " + temp;
			System.out.print(temp + "|");
			
			for(int i=0; i<15; i++){
				System.out.print(" " + board.square(i, j).getLegalHorizontalSet().size());
			}
			System.out.println();
		}
		System.out.println();
		
		System.out.print("Vertical\n   ");
		for(int x=0; x<15; x++)
			System.out.print(" " + x);
		System.out.println();
		
		for(int j=0; j<15; j++){
			String temp = Integer.toString(j);
			if(temp.length() < 2)
				temp = " " + temp;
			System.out.print(temp + "|");
			
			for(int i=0; i<15; i++){
				System.out.print(" " + board.square(i, j).getLegalVerticalSet().size());
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void computeCrossSets(Board board, GADDAGNode g) {
		for(int j = 0; j < Board.height; j++) {
			for(int i=0; i<Board.width; i++){
				if(board.square(i, j).isAnchor()) {
					if(board.square(i + 1, j).hasTile() || board.square(i - 1, j).hasTile()){
						board.square(i, j).getLegalHorizontalSet().clear();
						computeHorizontalCrossSet(i, j, g);
					}
					if(board.square(i, j + 1).hasTile() || board.square(i, j - 1).hasTile()){
						board.square(i, j).getLegalVerticalSet().clear();
						computeVerticalCrossSet(i, j, g);
					}
				}
			}
		}
	}
	
	private void computeHorizontalCrossSet(int i, int j, GADDAGNode root) {
		GADDAGNode current = root;
		//if it has a tile either side
		if (hasTile(i - 1, j) && hasTile(i + 1, j)) {
			System.out.println(i + " " + j + " tile either side");
			int x = i - 1;
			//go to end of prefix
			while (hasTile(x, j)) {
				System.out.print(getTile(x, j));
				current = current.get(getTile(x, j));
				if (current == null) {
					return;
				}
				x--;
			}
			//start making suffix
			current = current.get('@');
			if (current != null) {
				GADDAGNode base = current;
				//char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
				for (char c : Utilities.alphabetSet()) {
					System.out.println("Trying: " + c);
					current = base;
					current = current.get(c);
					x = i + 1;
					while (current != null && hasTile(x + 1, j)) {	//while there is a letter to the right and haven't reached a null node
						current = current.get(getTile(x, j));
						x++;
					}
					if (current != null) {		//why is current == null??
						if (current.hasAsEnd(getTile(x, j))) {
							square(i, j).addLegalHorizontalSet(c);
						}
					}
				}
			}
			//else if it has a tile to the left
		} else if (hasTile(i - 1, j)) {
			int x = i - 1;
			while (hasTile(x, j)) {
				current = current.get(getTile(x, j));
				if (current == null) {
					return;
				}
				x--;
			}
			current = current.get('@');
			//if we can switch to making a suffix
			if (current != null) {
				square(i, j).addAllToLegalHorizontal(current.getEndSet());
			}
			//else if it has a tile to the right
		} else if (hasTile(i + 1, j)) {
			int x = i + 1;
			//go to right most tile
			while (hasTile(x + 1, j)) {
				x++;
			}
			while (x > i) {
				current = current.get(getTile(x, j));
				if (current == null) {
					return;
				}
				x--;
			}
			square(i, j).addAllToLegalHorizontal(current.getEndSet());
		}
	}
	
	private void computeVerticalCrossSet(int i, int j, GADDAGNode root) {
		GADDAGNode current = root;
		//if it has a tile either side
		if(hasTile(i, j - 1) && hasTile(i, j + 1)) {
			int y = j - 1;
			while (hasTile(i, y)) {
				current = current.get(getTile(i, y));
				if (current == null) {
					return;
				}
				y--;
			}
			current = current.get('@');
			if (current != null) {
				GADDAGNode base = current;
				//char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
				for (char c : Utilities.alphabetSet()) {
					current = base;
					current = current.get(c);
					y = j + 1;
					while (current != null && hasTile(i, y + 1)) {
						current = current.get(getTile(i, y));
						y++;
					}
					if (current != null) {
						if (current.hasAsEnd(getTile(i, y))) {
							square(i, j).addLegalVerticalSet(c);
							//System.out.println("Adding " + c);
						}
					}
				}
			}
			//else if it has a tile to the up
		} else if (hasTile(i, j - 1)) {
			int y = j - 1;
			while (hasTile(i, y)) {
				current = current.get(getTile(i, y));
				if (current == null) {
					return;
				}
				y--;
			}
			current = current.get('@');
			//if we can switch to making a suffix??
			if (current != null) {
				square(i, j).addAllToLegalVertical(current.getEndSet());
			}
			//else if it has a tile to the down
		} else if (hasTile(i, j + 1)) {
			int y = j + 1;
			//go to the end of this word
			while (hasTile(i, y + 1)) {
				y++;
			}
			while (y > j) {
				current = current.get(getTile(i, y));
				if (current == null) {
					return;
				}
				y--;
			}
			square(i, j).addAllToLegalVertical(current.getEndSet());
		}
	}
	
	public int calculateScore(Move m){
		int score = 0;
		int wordMult = 1;
		int wordScore = 0;
		for(Play p : m){
			if(square[p.x][p.y].hasTile())	//if square already has a tile skip it, will be counted by its anchor square
				continue;
			else if(square[p.x][p.y].isAnchor()){	
				int tempScore = 0;	//accumulated score of other words this play has made
				int l = p.x - 1;
				int r = p.x + 1;
				int u = p.y - 1;
				int d = p.y + 1;
				while(square[l][p.y].hasTile()){	//while tile to left
					tempScore += Tile.getScore(square[l][p.y].getLetter());
					l--;
				}
				while(square[r][p.y].hasTile()){	//while tile to right
					tempScore += Tile.getScore(square[r][p.y].getLetter());
					r++;
				}
				while(square[p.x][d].hasTile()){	//while tile to down
					tempScore += Tile.getScore(square[p.x][d].getLetter());
					d++;
				}
				while(square[p.x][u].hasTile()){	//while tile to up
					tempScore += Tile.getScore(square[p.x][u].getLetter());
					u--;
				}
				tempScore += Tile.getScore(p.letter)*square[p.x][p.y].getLetterMultiplier();	//combine these 2 lines
				score += tempScore*square[p.x][p.y].getWordMultiplier();
			}
			else{
				wordMult = wordMult*square[p.x][p.y].getWordMultiplier();
				wordScore += Tile.getScore(p.letter)*square[p.x][p.y].getLetterMultiplier();
			}
		}
		return score += wordScore*wordMult;
	}
	
	public boolean isMoveValid(Move m){
		//*check if word is in the dictionary first! - move should be ordered
		boolean hasAnchor = false;
		for(Play p : m){
			if(square[p.x][p.y].hasTile())
				return false;
			else if(square[p.x][p.y].isAnchor()){
				hasAnchor = true;
				if(!square[p.x][p.y].legalHorizontal(p.letter) && !square[p.x][p.y].legalVertical(p.letter))
					continue;
			}
		}
		return hasAnchor;
	}
}