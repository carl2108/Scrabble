package scrabble;

public class Play {
	public final Tile tile;
	public final Square square;

	public Play(Square s, Tile tile) {
		this.square = s;
		this.tile = tile;
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

