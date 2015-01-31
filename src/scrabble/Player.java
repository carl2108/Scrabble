package scrabble;
public class Player {
	
	String name;
	Rack rack;
	int score;
	
	//Default constructor
	public Player() {
		name = "Insert Name";
		rack = new Rack();
		score = 0;
	}
	
	//Constructor
	public Player(String n) {
		name = n;
		rack = new Rack();
		score = 0;
	}
	
	

}
