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
		    	//newList.removeAll(Arrays.asList(l));	//.remove(l) returns out of bounds exception??
		    	//newList.remove((char) l);
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
}
		
