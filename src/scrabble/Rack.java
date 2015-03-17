package scrabble;
public class Rack {
	char[] myRack;
	
	//Constructor
	public Rack() {
		myRack = new char[6];
	}
	
	//create filled rack
	public Rack(char[] c){
		if(c.length > 6)
			throw new IllegalArgumentException("Array too large!");
		for(int i=0; i<c.length; i++){
			myRack[i] = c[i];
		}
	}
	
	/*		//should be handled by main game thread as it needs access to letterbag?
	//draw tiles till rack is full
	public void fill(){
		int i=0;
		for(i=0; i<6; i++){
			if(myRack[i] == null){
				
			}
		}
	}
	*/
	
	//Add letter to rack
	public void add(char t) {
		int i;
		for(i=0; myRack[i] != '!'; i++);
		myRack[i] = t;
	}
	
	//Remove letter at index
	public void remove(int index) {
		myRack[index] = '!';
	}
	
	//Print rack contents
	public void print() {
		for(char t: myRack){
			if(t == '!')
				System.out.print("_");
			else
				System.out.print(t);
		}
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
