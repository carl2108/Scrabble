package scrabble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class AI {
	
	static HashMap<Character, Heuristic> heuristics;
	static Move bestProbMove;
	static int bestProbScore = 0;
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
	public Move probSearchStart(int depth, Board board, Letterbag letterbag, List<Move> moves, Rack rack, int score, GADDAG g, int player){
		bestProbScore = 0;
		
		for(Move m : moves){
			if(m == null)	//if move is null don't continue
				continue;
			Board newBoard = board;		//new predicted board state
			newBoard.placeMove(m);		//pretend move has been played
			
			Rack newRack = rack;		//remove the letters associated with this move
			newRack.removeAll(m);
			letterbag.fillRack(newRack); 	//draw random letters to fill the rack
			
			searchLevel(depth-1, letterbag, score + m.score*player, newRack, newBoard, g, (player == 1) ? -1:1);	//search another level
		}
		return bestProbMove;
	}
	
	public void probSearch(int depth, Board board, Letterbag letterbag, List<Move> moves, Rack rack, int score, GADDAG g, int player){
		for(Move m : moves){
			if(m == null)	//if move is null don't continue
				continue;
			Board newBoard = board;		//new predicted board state
			newBoard.placeMove(m);		//pretend move has been played
			
			Rack newRack = rack;		//remove the letters associated with this move
			newRack.removeAll(m);
			letterbag.fillRack(newRack); 	//draw random letters to fill the rack
			
			searchLevel(depth-1, letterbag, score + m.score, newRack, newBoard, g, (player == 1) ? -1:1);	//search another level
		}
	}
	
	public void searchLevel(int depth, Letterbag letterbag, int score, Rack rack, Board board, GADDAG g, int player){
		if(depth < 1)
			return;
		
		board.computeAnchors();		
		board.computeCrossSets(board, g.getRoot());
		
		ArrayList<Tile> rackArray = new ArrayList<Tile>();		//**convert rack - fix this
		for(Character c : rack.myRack){
			if(c != '_')
				rackArray.add(Tile.valueOf(c));
		}
		List<Move> moves = g.findWords(g.getRoot(), rackArray, board);		//find all moves

		Move topMoves[] = new Move[5];
		int topScore[] = {0, 0, 0, 0, 0};
		
		for(Move m : moves){
			m.score = board.calculateScore(m);
			int i;
			boolean top = false;
			for(i=4; (i>=0 && m.score > topScore[i]); i--)	//find if move is in the top 5 and where
				top = true;
			
			i++;
			
			if(top){		//store it in the top candidate moves
				for(int p=4; p > i; p--){
					topScore[p] = topScore[p-1];
					topMoves[p] = topMoves[p-1];
				}
				topScore[i] = m.score;
				topMoves[i] = m;
				
				//System.out.println(topMoves[0].score);
			}
			
		}
		
		if(depth == 0 && (score + topMoves[0].score*player > bestProbScore)){
			bestProbScore = score + topMoves[0].score*player;		// +/- for player/ai
			bestProbMove = topMoves[0];
		}
		else 
			probSearch(depth, board, letterbag, Arrays.asList(topMoves), rack, score, g, player);
	}
/*	*/
}
