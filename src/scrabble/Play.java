package scrabble;

public class Play {
	public int x, y;
	public char letter;
	
	public Play(int x, int y, char l){
		this.x = x;
		this.y = y;
		this.letter = l;
	}

//	@Override
//	public boolean equals(Object obj) {
//		if (obj instanceof Play) {
//			Play that = (Play) obj;
//			return this.i == that.i && this.j == that.j
//					&& this.tile == that.tile;
//		}
//		return false;
//	}
//
//	@Override
//	public int hashCode() {
//		return (i + " " + j + " " + tile).hashCode();
//	}
}

