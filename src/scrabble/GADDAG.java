package scrabble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class GADDAG {
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

	public Set<String> findWordList(GADDAGNode root, List<Character> letters){
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

	public Set<Move> findWords(GADDAGNode root, ArrayList<Tile> rack /*change to Rack object?*/, Board board) {
		Set<Move> moves = new HashSet<Move>();
		for(int j=0; j<Board.height; j++){
			for(int i=0; i<Board.width; i++){
				if(board.square(i, j).isAnchor()){	//if square is an anchor + *hasnt been used in prev gen
					findHorizontal(0, i, j, new Move(), rack, root, board, moves);
					findVertical(0, i, j, new Move(), rack, root, board, moves);
				}
			}
		}
		return moves;
	}

	public void findHorizontal(int offset, int anchorx, int anchory, Move inMove, ArrayList<Tile> rack, GADDAGNode currNode, Board board, Set<Move> moves){
		if(currNode == null)
			return;
		if(anchorx + offset >= Board.width || anchorx + offset < 0)
			return;

		if(board.hasTile(anchorx + offset, anchory)){		//if there is a tile in the square to the left we must include this in the current move
			char l = board.get(anchorx + offset, anchory);
			GADDAGNode nextNode = currNode.get(l);
			Move newMove = new Move(inMove);
			newMove.addPlay(anchorx + offset, anchory, l, false);
			goOnHorizontal(offset, anchorx, anchory, l, newMove, rack, currNode, nextNode, board, moves);	//move on to the next square
		}
		else if(!rack.isEmpty()){	//else if we still have letters we can play
			for(Tile t : rack){	//for each letter left on the rack
				if(t != Tile.BLANK && board.square(anchorx + offset, anchory).legal(t.character)){	//if not blank + is legal to play in next square
					ArrayList<Tile> newRack = new ArrayList<Tile>(rack);
					newRack.remove(t);
					GADDAGNode nextNode = currNode.get(t.character);		//null pointer exception here! currNode = null
					if(nextNode == null)
						System.out.println("Null nextNode");
					Move newMove = new Move(inMove);
					newMove.addPlay(anchorx + offset, anchory, t.character, false);
					goOnHorizontal(offset, anchorx, anchory, t.character, newMove, newRack, currNode, nextNode, board, moves);
				} 
			} 
		}
		else
			System.out.println("No more possibilities");
	}

	public void goOnHorizontal(int offset, int anchorx, int anchory, char letter, Move inMove, ArrayList<Tile> rack, GADDAGNode currNode, GADDAGNode nextNode, Board board, Set<Move> moves){
		if(currNode == null)	//if current node is null we cant continue so return
			return;

		if(offset <= 0){	//if making prefix
			//if its a valid move ending record it
			if(currNode.hasAsEnd(letter) && board.square(anchorx + offset, anchory).legal(letter) && !board.square(anchorx + offset, anchory).hasTile()/*needed?*/){
				recordMove(inMove, moves);
				System.out.println("Move found!");
			}
			//continue trying to generate prefixes
			findHorizontal(offset - 1, anchorx, anchory, inMove, rack, nextNode, board, moves);

			//if we can start making suffixes do so
			currNode = currNode.get('@');
			if(currNode != null){
				findHorizontal(1, anchorx, anchory, inMove, rack, currNode, board, moves);
				System.out.println("Delimiter: @");
			}
		}
		else if(offset > 0){	//else if making suffix
			//if its a valid move ending record it
			if(currNode.hasAsEnd(letter) && board.square(anchorx + offset, anchory).legal(letter) && !board.square(anchorx + offset, anchory).hasTile()){
				recordMove(inMove, moves);
				System.out.println("Move found!");
			}
			//continue trying to generate suffixes
			currNode = nextNode;
			findHorizontal(offset + 1, anchorx, anchory, inMove, rack, currNode, board, moves);
		}
		else
			System.out.println("Invalid");
	}

	public void findVertical(int offset, int anchorx, int anchory, Move inMove, ArrayList<Tile> rack, GADDAGNode currNode, Board board, Set<Move> moves){
		System.out.println("Verticals: " + rack.size());
		if(currNode == null)
			return;
		if(anchory + offset >= Board.height || anchory + offset < 0)
			return;

		if(board.hasTile(anchorx, anchory + offset)){		//if there is a tile in the square to the left we must include this in the current move
			char l = board.get(anchorx, anchory + offset);
			GADDAGNode nextNode = currNode.get(l);
			Move newMove = new Move(inMove);
			newMove.addPlay(anchorx, anchory + offset, l, true);
			goOnVertical(offset, anchorx, anchory, l, newMove, rack, currNode, nextNode, board, moves);	//move on to the next square
		}
		else if(!rack.isEmpty()){	//else if we still have letters we can play
			for(Tile t : rack){	//for each letter left on the rack
				if(t != Tile.BLANK && board.square(anchorx, anchory + offset).legal(t.character)){	//if not blank + is legal to play in next square
					ArrayList<Tile> newRack = new ArrayList<Tile>(rack);
					newRack.remove(t);
					GADDAGNode nextNode = currNode.get(t.character);
					Move newMove = new Move(inMove);
					newMove.addPlay(anchorx, anchory + offset, t.character, true);
					goOnVertical(offset, anchorx, anchory, t.character, newMove, newRack, currNode, nextNode, board, moves);
				} 
			} 
		}
		else
			System.out.println("No more possibilities vertical");
	}

	public void goOnVertical(int offset, int anchorx, int anchory, char letter, Move inMove, ArrayList<Tile> rack, GADDAGNode currNode, GADDAGNode nextNode, Board board, Set<Move> moves){
		if(currNode == null)	//if current node is null we cant continue so return
			return;

		if(offset <= 0){	//if making prefix
			//if its a valid move ending record it
			if(currNode.hasAsEnd(letter) && board.square(anchorx, anchory + offset).legal(letter) && !board.square(anchorx, anchory + offset).hasTile()/*needed?*/){
				recordMove(inMove, moves);
				System.out.println("Move found!");
			}
			//continue trying to generate prefixes
			findVertical(offset - 1, anchorx, anchory, inMove, rack, nextNode, board, moves);

			//if we can start making suffixes do so
			currNode = currNode.get('@');
			if(currNode != null){
				findVertical(1, anchorx, anchory, inMove, rack, currNode, board, moves);
				System.out.println("Delimiter: @ - Vertical");
			}
		}
		else if(offset > 0){	//else if making suffix
			//if its a valid move ending record it
			if(currNode.hasAsEnd(letter) && board.square(anchorx, anchory + offset).legal(letter) && !board.square(anchorx, anchory + offset).hasTile()){
				recordMove(inMove, moves);
				System.out.println("Move found!");
			}
			//continue trying to generate suffixes
			currNode = nextNode;
			findVertical(offset + 1, anchorx, anchory, inMove, rack, currNode, board, moves);
		}
		else
			System.out.println("Invalid Vertical");
	}

	private void recordMove(Move move, Set<Move> output) {
		output.add(move);
		System.out.println("Recorded:\n " + move + "\n");
	}
}

