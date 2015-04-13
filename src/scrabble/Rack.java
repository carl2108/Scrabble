package scrabble;
public class Rack {
	public char[] myRack;
	
	//Constructor
	public Rack() {
		myRack = new char[6];
		initRack();
	}
	
	public void initRack(){
		for(int i=0; i<6; i++)
			myRack[i] = '_';
	}
	
	//create filled rack
	public Rack(char[] c){
		if(c.length > 6)
			throw new IllegalArgumentException("Array too large!");
		for(int i=0; i<c.length; i++){
			myRack[i] = c[i];
		}
	}
	
	//Add letter to rack
	public void add(char t) {
		int i;
		for(i=0; myRack[i] != '_'; i++);
		myRack[i] = t;
	}
	
	//Remove letter at index
	public void remove(int index) {
		myRack[index] = '_';
	}
	
	//remove all the letters in the array
	public boolean removeAll(char[] all){
		for(Character c : all){
			if(!remove(c)){
				//System.out.println("Failed to remove " + c);
				//return false;
			}
		}
		return true;
	}
	
	//remove all the letters in the array - given move
		public boolean removeAll(Move m){
			for(Play p : m.plays){
				if(!remove(p.letter)){
					//System.out.println("Failed to remove " + p.letter);
					//return false;
				}
			}
			return true;
		}
	
	//find and remove the given letter from the rack
	public boolean remove(char c){
		for(int i=0; i<6; i++){
			if(myRack[i] == c){
				myRack[i] = '_';
				return true;
			}
		}
		return false;
	}
	
	//Print rack contents
	public void print() {
		for(char t: myRack)
			System.out.print(t);
		System.out.println();
	}
	
	//Realign rack contents - *To do
	public void align() {
		return;
	}
	
	//Random shuffle rack contents - *To do
	public void shuffle() {
		return;
	}

}
