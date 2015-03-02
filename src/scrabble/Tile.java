package scrabble;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Tile {
	public final char letter;
	public final int value;
	public Image img;	//final?
	
	public Tile(char l, int v) {
		this.letter = l;
		this.value = v;
		this.img = getImg(l);
	}
	
	private Image getImg(char l){
		return new ImageIcon("img//letters//" + Character.toString(l).toUpperCase() + ".tiff").getImage().getScaledInstance(35, 35, Image.SCALE_DEFAULT);
	}
}
