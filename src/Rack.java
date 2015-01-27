public class Rack {
	Tile[] myRack;
	
	//Constructor
	public Rack() {
		myRack = new Tile[6];
	}
	
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
		for(int i=0; i<myRack.length; i++){
			System.out.print(myRack[i]);
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
