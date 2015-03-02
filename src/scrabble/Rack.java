package scrabble;
public class Rack {
	Tile[] myRack;
	
	//Constructor
	public Rack() {
		myRack = new Tile[6];
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
	public void add(Tile t) {
		int i;
		for(i=0; myRack[i] != null; i++);
		myRack[i] = t;
	}
	
	//Remove letter at index
	public void remove(int index) {
		myRack[index] = null;
	}
	
	//Print rack contents
	public void print() {
		for(Tile t: myRack){
			if(t == null)
				System.out.print("_");
			else
				System.out.print(t.letter);
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
