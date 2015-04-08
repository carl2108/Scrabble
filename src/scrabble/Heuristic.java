package scrabble;

public class Heuristic {

	public float first, second;
	
	public Heuristic(float a, float b){
		first = a;
		second = b;
	}
	
	public String toString(){
		return "First: " + first + " Second: " + second;
	}
	
	
}
