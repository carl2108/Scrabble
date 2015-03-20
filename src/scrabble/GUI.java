package scrabble;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class GUI implements Runnable{
	private JFrame frame;
	private StyledDocument doc;
	private JLabel boardFrame, rackFrame;
	private Image boardImage;
	private JTextPane txtConsole;
	private JScrollPane scrollPane;
	private JScrollBar vertical;
	private JLabel[] rackLetters;
	private JLabel[][] grid;
 
	public void run() {
		while(true);
	}
	
	//Constructor
	public GUI() {
		System.out.println("GUI Contructor");
		initialize();
		this.frame.setVisible(true);
	}

	//Initialize
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 0, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//rack frame and letter JLabels
		rackFrame = new JLabel();		//rack JFrame container
		rackFrame.setLayout(new BoxLayout(rackFrame, BoxLayout.X_AXIS));
		rackLetters = new JLabel[6];	//create and add 6 letter JLabel containers to be nested in the rack container
		for(int i = 0; i<6; i++){
			rackLetters[i] = new JLabel();
			rackFrame.add(rackLetters[i]);
		}
		
		//board/letter mask layers
		boardFrame = new JLabel();	//board JFrame container
		boardImage = new ImageIcon("img/scrabble_board.jpg").getImage().getScaledInstance(525, 525, Image.SCALE_DEFAULT);	
		boardFrame.setIcon(new ImageIcon(boardImage));
		boardFrame.setSize(525, 525);
		
		//add JLabels to the board which will store letter images later
		grid = new JLabel[15][15];
		for(int j=0; j<15; j++){
			for(int i=0; i<15; i++){
				grid[i][j] = new JLabel();
				grid[i][j].setSize(35, 35);
				grid[i][j].setLocation(i*35, j*35);
				boardFrame.add(grid[i][j]);
			}
		}
		
		Image letterImage;
		try {
			letterImage = ImageIO.read(new File("img/letters/A.tiff")).getScaledInstance(35, 35, Image.SCALE_DEFAULT);
			grid[3][4].setIcon(new ImageIcon(letterImage));
			System.out.println("Added");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		txtConsole = new JTextPane();
		txtConsole.setEditable(false);	
		txtConsole.setText("Console...\n");
		
		scrollPane = new JScrollPane(txtConsole);
		vertical = scrollPane.getVerticalScrollBar();
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(boardFrame, GroupLayout.PREFERRED_SIZE, 525, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane)
						.addComponent(rackFrame, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(rackFrame, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
						.addComponent(boardFrame, Alignment.LEADING))
					.addContainerGap(47, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
	
	//prints to GUI console
	public void consoleWrite(String s) {
		doc = txtConsole.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), s + "\n", null);	//Updates doc storing textConsole text
			vertical.setValue(vertical.getMaximum());			//Scroll down as text is output to console
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	//updates rack GUI given an array of letter characters
	public void refreshRack(char[] rack) {
		int i = 0;
		for(char c: rack){
			if(c != '_'){
				try {
					Image letterImage = ImageIO.read(new File("img/letters/" + Character.toString(c).toUpperCase() + ".tiff")).getScaledInstance(35, 35, Image.SCALE_DEFAULT);
					rackLetters[i].setIcon(new ImageIcon(letterImage));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			i++;
		}
	}

}
