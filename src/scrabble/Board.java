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
				System.out.print(square[i][j].toString() + " ");
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
		if (!flip) {
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
	
	//////**********************************************
	
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
			//for (int i = Board.width - 1; i >= 0; i--) {	//why this way?
			for(int i=0; i<Board.width; i++){
				if(/*!board.hasTile(i, j)*/ board.isValidAnchor(i, j)) {		//*if board position is an anchor??
					//System.out.println("X: " + i + " Y: " + j);
					board.square(i, j).getLegalHorizontalSet().clear();
					computeHorizontalCrossSet(i, j, g);
					board.square(i, j).getLegalVerticalSet().clear();
					computeVerticalCrossSet(i, j, g);
					board.square(i, j).printLegalHorizontalSet();
				}
			}
		}
	}
	
	//seems to compute horizontal cross sets correctly - will implement for vertical
	private void computeHorizontalCrossSet(int i, int j, GADDAGNode root) {
		//System.out.print("Calculating Horizontal Cross Sets: ");
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
				GADDAGNode temp = current;	//for loop will change current node so store it
				char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
				for (char c : alphabet) {
					System.out.println("Trying: " + c);
					current = temp;
					GADDAGNode next = current.get(c);	//try putting letter 'c' here and follow suffix to end to see if valid word
					x = i + 1;
					while (next != null && hasTile(x , j)) {	//while there is a letter to the right and haven't reached a null node
						System.out.println("Continue");
						current = next;
						next = next.get(getTile(x , j));
						x++;
					}
					//if (current != null) {		//why is current == null??
						if (current.hasAsEnd(getTile(x - 1, j))) {
							square(i, j).addLegalHorizontalSet(c);
							System.out.println("Adding " + c);
						}
					//}
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
				//square(i, j).addAllToLegal(current.getTransitionSet());
				//square(i, j).addAllToLegal(current.getEndSet());
				square(i, j).addAllToLegalHorizontal(current.getEndSet());
				System.out.println("Added1: " + current.getEndSet().size() + " to X: " + i + " Y: " + j);
//				for(Character c: current.getEndSet()){
//					System.out.println(c.charValue());
//				}
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
			System.out.println("\nAdded2: " + current.getEndSet().size() + " to X: " + i + " Y: " + j);
			for(Character c: current.getEndSet()){
				System.out.print(c.charValue());
			}
		}
	}
	
	private void computeVerticalCrossSet(int i, int j, GADDAGNode root) {
		//System.out.print("Calculating Vertical Cross Sets: ");
		GADDAGNode current = root;
		//if it has a tile either side
		if (hasTile(i, j - 1) && hasTile(i, j + 1)) {
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
				char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
				for (char c : alphabet) {
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

			//else if it has a tile to the left
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
//				System.out.println("Added1: " + current.getEndSet().size() + " to X: " + i + " Y: " + j);
//				for(Character c: current.getEndSet()){
//					System.out.println(c.charValue());
//				}
			}

			//else if it has a tile to the right
		} else if (hasTile(i, j + 1)) {
			int y = j + 1;
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
//			System.out.println("Added2: " + current.getEndSet().size() + " to X: " + i + " Y: " + j);
//			for(Character c: current.getEndSet()){
//				System.out.println(c.charValue());
//			}
		}
		//System.out.println("Done!");
	}
	
}