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
	
	boolean flip = false;
	
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
	
	public void setFlip(boolean b) {
		this.flip = b;
	}
	
	public void placeLetter(char l, int x, int y){
		square[x][y].setLetter(l);
	}
	
	public void placeLetter(char l, Square s){
		s.setLetter(l);
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
		if(!flip) {
			return square[i][j];
		} else {
			return square[j][i];
		}
	}
	
	public char get(int i, int j){
		if(!flip) {
			return square[i][j].getLetter();
		} else {
			return square[j][i].getLetter();
		}
	}
	
	public Character getTile(int i, int j) {
		if (flip) {
			return square[i][j].letter;
		} else {
			return square[j][i].letter;
		}
	}

	private boolean isValidAnchor(int i, int j) {
		if (!this.hasTile(i, j)) {
			if (i == 15 / 2 && j == 15 / 2) {
				return true;
			}
			if (hasTile(i - 1, j) || hasTile(i + 1, j) || hasTile(i, j + 1)
					|| hasTile(i, j - 1)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasTile(int i, int j) {
		try {
			if (flip) {
				return square[i][j].hasTile();
			} else {
				return square[j][i].hasTile();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public void printAnchors(){
		System.out.print("   ");
		for(int x=1; x<16; x++)
			System.out.print(" " + x);
		System.out.println();
		
		for(int j=0; j<15; j++){
			String temp = Integer.toString(j+1);
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
	
	//////**********************************************
	
	public void computeCrossSets(Board board, GADDAGNode g) {
		for (int j = 0; j < Board.height; j++) {
			for (int i = Board.width - 1; i >= 0; i--) {	//why this way?
			//for(int i=0; i<Board.width; i++){
				if (!board.hasTile(i, j)) {
					board.square(i, j).getLegalSet().clear();
					computeCrossSet(i, j, g);
				}
			}
		}
	}

	private void computeCrossSet(int i, int j, GADDAGNode root) {
		GADDAGNode current = root;
		if (hasTile(i - 1, j) && hasTile(i + 1, j)) {
			int x = i - 1;
			while (hasTile(x, j)) {
				current = current.get(getTile(x, j));
				if (current == null) {
					return;
				}
				x--;
			}
			current = current.get('@');
			if (current != null) {
				GADDAGNode base = current;
				for (char c : CharIterator.iter()) {
					current = base;
					current = current.get(c);
					x = i + 1;
					while (current != null && hasTile(x + 1, j)) {
						current = current.get(getTile(x, j));
						x++;
					}
					if (current != null) {
						if (current.hasAsEnd(getTile(x, j))) {
							square(i, j).addLegalSet(c);
							System.out.println("Adding " + c);
						}
					}
				}
			}

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
			//if we can switch to making a suffix??
			if (current != null) {
				square(i, j).addAllToLegal(current.getEndSet());
				System.out.println("Added1: " + current.getEndSet().size() + " to X: " + i + " Y: " + j);
				for(Character c: current.getEndSet()){
					System.out.println(c.charValue());
				}
			}

		} else if (hasTile(i + 1, j)) {
			int x = i + 1;
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
			square(i, j).addAllToLegal(current.getEndSet());
			System.out.println("Added2: " + current.getEndSet().size() + " to X: " + i + " Y: " + j);
			for(Character c: current.getEndSet()){
				System.out.println(c.charValue());
			}
		}
	}
	
}