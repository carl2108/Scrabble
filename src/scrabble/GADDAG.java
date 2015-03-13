package scrabble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GADDAG {
	
	public Logger log = Logger.getLogger("GADDAG");
	
	GADDAGNode rootMin;
	long buildtime;

	public GADDAG() {
    
	    buildtime = System.currentTimeMillis();
	    rootMin = null;
	
	    File file = new File("text//OSPD.txt");
	    try {
			rootMin = buildGADDAG(new Scanner(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    
	    System.out.println("Done!");
	    System.out.println("Build time: " + (System.currentTimeMillis() - buildtime) / 1000.0);
	    System.out.println("Nodes: " + GADDAGNode.idCounter);
//	    Logger.getLogger("").addHandler(new SimpleHandler());
//	    Logger.getLogger("").setLevel(Level.ALL);
//	    Scanner in = new Scanner(System.in);
//	    while (in.hasNext()) {
//	    	long time = System.nanoTime();
//	    	System.out.println(rootMin.contains(in.next()));
//	    	System.out.println("Query time: " + (System.nanoTime() - time));
//	    }
	    
	  }
	  
	public static List<String> traverse(GADDAGNode root) {
	    List<String> words = new ArrayList<String>();
	    for(char c : root.getTransitions()) {
	    	GADDAGNode child = root.get(c);
		    if(child == null)
		        continue;
		    for(String s : traverse(child)) {
		        words.add("" + c + s);
		    }
	    }
	    for (char end : root.getEnd()) {
	    	words.add("" + end);
	    }
	    return words;
	}
	  
	  public static GADDAGNode buildGADDAG(Scanner words) {
	    System.out.println("Building GADDAG...");
	    System.out.flush();
	    GADDAGNode root = new GADDAGNode();
	    while (words.hasNext()) {
	      GADDAGNode node = root;
	      String word = words.next();
	      String splitword1 = reverse(word);
	      node = buildGADDAGbranch(node, splitword1);
	      node = root;
	      String splitword2 = reverse(word.substring(0, word.length() - 1)) + "@" + word.substring(word.length() - 1);
	      node = buildGADDAGbranch(root, splitword2);
	      for (int m = word.length() - 3; m >= 0; m-- ) {
	        GADDAGNode temp = node;
	        node = root;
	        for (int i = m; i >= 0; i-- ) {
	          node = node.put(word.charAt(i));
	        }
	        node = node.put('@');
	        node.put(word.charAt(m + 1), temp);
	      }
	      
	    }
	    System.out.println("GADDAG done!");
	    return root;
	  }
	  
	  public static GADDAGNode buildGADDAGLarge(Scanner words) {
	    GADDAGNode root = new GADDAGNode();
	    while (words.hasNext()) {
	      String word = words.next().trim();
	      for (int i = 1; i <= word.length(); i++ ) {
	        String splitword = reverse(word.substring(0, i)) + "@" + word.substring(i);
	        buildGADDAGbranch(root, splitword);
	      }
	    }
	    return root;
	  }
	  
	  private static GADDAGNode buildGADDAGbranch(GADDAGNode root, String word) {
	    GADDAGNode current = root;
	    for (int i = 0; i < word.length() - 1; i++ ) {
	      current = current.put(word.charAt(i));
	      if (i == word.length() - 2) {
	        current.putEndSet(word.charAt(word.length() - 1));
	      }
	    }
	    return current;
	  }
	  
	  private static String reverse(String substring) {
	    return (new StringBuffer(substring)).reverse().toString();
	  }
	  
	  public GADDAGNode getRoot() {
		  return rootMin;
	  }
	  
	 /* 
	  public Set<String> findWords(GADDAGNode root, List<Character> letters) {
			//System.out.println("Letters:   " + Arrays.toString(letters.toArray()) + "  Node:   " + root.getID());
		    Set<String> words = new HashSet<String>();
		    String word = new String();
		    for(int i=0; i<letters.size(); i++) {
		    	char l = letters.get(i);
		    	System.out.println("Start with " + l);
		    	if(root.hasAsEnd(l))
		    		words.add(word + l);
		    	GADDAGNode child = root.get(l);
		    	
		    	List<Character> newList = new ArrayList<Character>(letters);
		    	//newList.removeAll(Arrays.asList(l));
		    	if(l != '@')
		    		newList.remove(newList.indexOf(l));
		    	
		    	if(child == null) {
		    		child = root.get('@');
		    		l = '@';
	    			if(child == null)
	    				continue;
		    	}
		    	words.addAll(find(child, newList, (word + l)));
		    }	
		    return words;
		}
		
		public Set<String> find(GADDAGNode root, List<Character> letters, String word) {
			//if(letters.isEmpty())
				//return null;
			System.out.println("Letters: " + Arrays.toString(letters.toArray()) + " Node: " + root.getID() + " Word: " + word);
		    Set<String> words = new HashSet<String>();
		    
		    for(int i=0; i<letters.size(); i++) {
		    	char l = letters.get(i);
		    	System.out.println("Find " + l);
		    	if(root.hasAsEnd(l)){
		    		System.out.println("Word found: " + word + l);
		    		words.add(word + l);
		    		System.out.println("Added");
		    	}
		    	GADDAGNode child = root.get(l);
		    	System.out.println("Child");
		    	
		    	List<Character> newList = new ArrayList<Character>(letters);
		    	if(l != '@')
		    		newList.remove(newList.indexOf(l));
		    	
		    	System.out.println("New List");
		    	
		    	if(child == null){
		    		System.out.println("Null 1");
		    		child = root.get('@');
		    		l = '@';
		    		if(child == null){
		    			System.out.println("Null 2");
		    			continue;
		    		}
		    	}
		    	System.out.println("Shouldnt come after null 2");
		    	words.addAll(find(child, newList, (word + l)));
		    }
		    System.out.println("Words Returned:\n" + Arrays.toString(words.toArray()));
			return words;
		}
	*/	
		//////////****************************************************************
		
		public Set<Move> computeMoves(List<Tile> rack, Board board) {
			Set<Move> output = new HashSet<Move>();
			for (int j = 0; j < Board.height; j++) {
				for (int i = 0; i < Board.width; i++) {
					if (board.get(i, j).isAnchor()) {
						GADDAGNode current = this.getRoot();
						genMoves(0, i, j, output, new Move(), new ArrayList<Tile>(
								rack), current, false, board);
					}
				}
			}
			return output;
		}

		private void genMoves(int offset, int anchori, int anchorj,
				Set<Move> output, Move inMove, ArrayList<Tile> rack, GADDAGNode base,
				boolean offOfReverse, Board board) {

			//if current square is not on the board return
			if (anchorj + offset >= Board.height || anchorj + offset < 0) {
				return;
			}
			
			//if there is a tile already placed in the next position
			if (board.hasTile(anchori, anchorj + offset)) {
				Tile t = board.get(anchori, anchorj + offset).getTile();
				Character l = t.letter;
				Move move = new Move(inMove);
				GADDAGNode current;
				//if we are making prefix go to root node?? - why?
				if (offOfReverse) {
					current = base;
				} else {
					current = base.get(l);
				}
				goOn(offset, anchori, anchorj, t, output, move, rack, current,
						offOfReverse, board);
				//else if we still have tiles on our rack
			} else if (!rack.isEmpty()) {
				//for each tile in the rack
				for (Tile tile : rack) {
					//if the tile isnt blank and is a legal tile in the next space
					if (tile.letter != '*' && board.get(anchori, anchorj + offset).legal(tile.letter)) {
						log.fine("Trying out " + tile + " at " + anchori + ":"
								+ (anchorj + offset));
						ArrayList<Tile> newRack = new ArrayList<Tile>(rack);
						newRack.remove(tile);
						Move move = new Move(inMove);
						GADDAGNode current;
						if (offOfReverse) {
							current = base;
						} else {
							current = base.get(tile.letter);
						}
						goOn(offset, anchori, anchorj, tile, output,
								move, newRack, current, offOfReverse, board);
					} else {
						// implement blank tiles
					}
				}
			} else {
				log.fine("no more possibilities");
				//System.out.println("no more possibilities");
			}

		}

		private void goOn(int offset, int anchori, int anchorj, Tile tile,
				Set<Move> output, Move move, ArrayList<Tile> rack, GADDAGNode current2,
				boolean offOfReverse, Board board) {

			if (current2 != null) {
				if (offset <= 0) {
					addPlay(move, tile, anchori, anchorj + offset, board);
					if (current2.hasAsEnd(tile.letter) && !board.hasTile(anchori, anchorj + offset - 1) && !board.hasTile(anchori, anchorj + 1)) {
						recordMove(move, output);
					}
					GADDAGNode current = current2;
					log.fine("" + current);
					log.fine("genning 1 up with move:\n " + move);
					genMoves(offset - 1, anchori, anchorj, output, move, rack,
							current, false, board);
					current = current2.get('@');
					if (current != null && !board.hasTile(anchori, anchorj + offset - 1)) {
						log.fine("" + current);
						log.fine("genning reverse with move:\n " + move);
						genMoves(1, anchori, anchorj, output, move, rack, current,
								true, board);
					}

				} else if (offset > 0) {
					addPlay(move, tile, anchori, anchorj + offset, board);

					if (current2.hasAsEnd(tile.letter)
							&& !board.hasTile(anchori, anchorj + offset + 1)) {
						recordMove(move, output);
					}
					if (offOfReverse) {
						current2 = current2.get(tile.letter);
						if (current2 == null) {
							return;
						}
					}
					log.fine("genning 1 down with move:\n " + move);
					GADDAGNode current = current2;
					log.fine("" + current);
					genMoves(offset + 1, anchori, anchorj, output, move, rack,
							current, false, board);
				}
			} else {
				log.fine("Invalid trans");
			}

		}

		private void addPlay(Move move, Tile tile, int i, int j, Board board) {
			if(board.flip) {
				move.addPlay(tile, j, i, board);
			} else {
				move.addPlay(tile, i, j, board);
			}
		}

		private void recordMove(Move move, Set<Move> output) {
			Move record = new Move(move);
			output.add(record);
			log.fine("Recorded:\n " + record + "\n");
			//System.out.println("Recorded: " + record);
		}

//		public void computeCrossSets(Board board) {
//			for (int j = 0; j < Board.height; j++) {
//				for (int i = Board.width - 1; i >= 0; i--) {
//					if (!board.hasTile(i, j)) {
//						board.get(i, j).getLegalSet().clear();
//						computeCrossSet(i, j, board);
//					}
//				}
//			}
//		}
//
//		private void computeCrossSet(int i, int j, Board board) {
//			GADDAGNode current = this.getRoot();
//			if (board.hasTile(i - 1, j) && board.hasTile(i + 1, j)) {
//				int x = i - 1;
//				while (board.hasTile(x, j)) {
//					current = current.get(board.getTile(x, j));
//					if (current == null) {
//						return;
//					}
//					x--;
//				}
//				current = current.get('@');
//				if (current != null) {
//					GADDAGNode base = current;
//					for (char c : CharIterator.iter()) {
//						current = base;
//						current = current.get(c);
//						x = i + 1;
//						while (current != null && board.hasTile(x + 1, j)) {
//							current = current.get(board.getTile(x, j));
//							x++;
//						}
//						if (current != null) {
//							if (current.hasAsEnd(board.getTile(x, j))) {
//								board.get(i, j).addLegalSet(c);
//								System.out.println("Adding " + c);
//							}
//						}
//					}
//				}
//
//			} else if (board.hasTile(i - 1, j)) {
//				int x = i - 1;
//				while (board.hasTile(x, j)) {
//					current = current.get(board.getTile(x, j));
//					if (current == null) {
//						return;
//					}
//					x--;
//				}
//				current = current.get('@');
//				if (current != null) {
//					board.get(i, j).addAllToLegal(current.getEndSet());
//				}
//
//			} else if (board.hasTile(i + 1, j)) {
//				int x = i + 1;
//				while (board.hasTile(x + 1, j)) {
//					x++;
//				}
//				while (x > i) {
//					current = current.get(board.getTile(x, j));
//					if (current == null) {
//						return;
//					}
//					x--;
//				}
//				board.get(i, j).addAllToLegal(current.getEndSet());
//			}
//		}
}
		
