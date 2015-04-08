package scrabble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class AI {
	
	static HashMap<Character, Heuristic> heuristics;
	static int[][] VCMix = new int[][]{
		  {0, 0, -1, -2, -3, -4, -5},
		  {-1, 1, 1, 0, -1, -2},
		  {-2, 0, 2, 2, 1},
		  {-3, -1, 1, 3},
		  {-4, -2, 0},
		  {-5, -3},
		  {-6}
		};
	
	public AI(){
		heuristics = new HashMap<Character, Heuristic>();
		try {
			Scanner scan = new Scanner(new File("text/heuristics.txt"));
			while(scan.hasNextLine()){
				String s = scan.nextLine();
				char c = s.charAt(0);
				Scanner localScan = new Scanner(s.substring(1));
				float first = localScan.nextFloat();
				float second;
				if(localScan.hasNextFloat())
					second = localScan.nextFloat();
				else 
					second = -999;
				//System.out.println("Char: " + c + " First: " + first + " Second: "+ second);
				Heuristic temp = new Heuristic(first, second);
				heuristics.put(c, temp);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//(OVERALL MOVE COST) - calculates the rack leave cost + word score based on heuristic 3 in papers
	public float calcCost(int score, ArrayList<Tile> rackLeave){		
		float rackScore = (float) 0.0;
		for(Character c : Utilities.alphabetSet()){
			int occurrences = Collections.frequency(rackLeave, Tile.valueOf(c));
			while(occurrences > 0){
				rackScore += heuristics.get(c).first + (heuristics.get(c).second)*(occurrences-1);
				occurrences--;
			}
		}
		//VCMix
		int vowels = Utilities.countVowels(rackLeave);
		int consonants = Utilities.countConsonants(rackLeave);
		rackScore += VCMix[vowels][consonants];
		
		return rackScore + score;
	}
	
	//probabilistic search method. 
	//Inputs: search depth, board state, remaining letters, set of Moves to consider (# moves to consider?)
	//Outputs: best move choice over search depth
	public Move probSearch(int depth, Board board, Letterbag letterbag, List<Move> moves){
		Move bestMove = new Move();
		
		
		
		return bestMove;
	}
}
