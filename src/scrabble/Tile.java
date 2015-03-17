//package scrabble;
//
//import java.awt.Image;
//
//import javax.swing.ImageIcon;
//
//public class Tile {
//	public final char letter;
//	public final int value;
//	public Image img;	//final?
//	
//	public Tile(char l, int v) {
//		this.letter = l;
//		this.value = v;
//		this.img = getImg(l);
//	}

package scrabble;

import java.awt.Image;

import javax.swing.ImageIcon;

public enum Tile {

	BLANK('#', 0), A('A', 1), E('E', 1), I('I', 1), L('L', 1), N('N', 1), O(
			'O', 1), R('R', 1), S('S', 1), T('T', 1), U('U', 1), D('D', 2), G(
			'G', 2), B('B', 3), C('C', 3), M('M', 3), P('P', 3), F('F', 4), H(
			'H', 4), V('V', 4), W('W', 4), Y('Y', 4), K('K', 5), J('J', 8), X(
			'X', 8), Q('Q', 10), Z('Z', 10), NULL('*', 0);

	public final char character;
	public final int score;

	private Tile(char character, int score) {
		this.character = character;
		this.score = score;
	}

	public static Tile valueOf(Character c) {
		for (Tile t : Tile.values()) {
			if (t.character == c) {
				return t;
			}
		}
		return Tile.NULL;
	}


	
	private Image getImg(char l){
		return new ImageIcon("img//letters//" + Character.toString(l).toUpperCase() + ".tiff").getImage().getScaledInstance(35, 35, Image.SCALE_DEFAULT);
	}
}
