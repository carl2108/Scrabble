package scrabble;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

public class GUI implements Runnable, MouseListener{
	private JFrame frame;
	private StyledDocument doc;
	private JLabel boardFrame, rackFrame;
	private Image boardImage;
	private JTextPane txtConsole;
	private JScrollPane scrollPane;
	private JScrollBar vertical;
	private JLabel[] rackLetters;
	private JLabel[][] grid;
	private char[] rack;
	private char selectLetter;
	private char moveMask[][];
	private JButton clearButton, playButton;
	private Move userMove;
 
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
		//clear and play button initialization
		clearButton = new JButton("Clear");
		playButton = new JButton("Play");
		
		//user move variables
		moveMask = new char[15][15];
		for(int j=0; j<15; j++){
			for(int i=0; i<15; i++){
				moveMask[i][j] = '_';
			}
		}
		rack = new char[6];
		selectLetter = '_';
		userMove = null;
		
		//application window and frame
		frame = new JFrame("Scrabble Project");
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
		//console window
		txtConsole = new JTextPane();
		txtConsole.setEditable(false);	
		txtConsole.setText("Console...\n");
		
		scrollPane = new JScrollPane(txtConsole);
		vertical = scrollPane.getVerticalScrollBar();
		
		//GUI layout manager
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(	//horizontal grouping
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(boardFrame, GroupLayout.PREFERRED_SIZE, 525, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane)
						.addComponent(rackFrame, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(clearButton)
							.addComponent(playButton)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(	//vertical grouping
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(rackFrame, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
							.addGroup(groupLayout.createParallelGroup()
								.addComponent(clearButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(playButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(boardFrame, Alignment.LEADING))
					.addContainerGap(47, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
		
		//GUI event listeners
		rackFrame.addMouseListener(this);
		boardFrame.addMouseListener(this);
		clearButton.addActionListener(new ActionListener() { 
		    public void actionPerformed(ActionEvent e) { 
		    	System.out.println("Clear pressed");
		        resetBoard();
		    } 
		});
		playButton.addActionListener(new ActionListener() { 
		    public void actionPerformed(ActionEvent e) { 
		    	System.out.println("Play pressed");
		        setUserMove();
		    } 
		});
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
	public void refreshRack(char[] newRack) {
		for(int i=0; i<6; i++){
			if(newRack[i] != '_'){
				try {
					Image letterImage = ImageIO.read(new File("img/letters/" + Character.toString(newRack[i]).toUpperCase() + ".tiff")).getScaledInstance(35, 35, Image.SCALE_DEFAULT);
					rackLetters[i].setIcon(new ImageIcon(letterImage));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				try {
					Image letterImage = ImageIO.read(new File("img/letters/empty.tiff"));
					rackLetters[i].setIcon(new ImageIcon(letterImage));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			rack[i] = newRack[i];
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getComponent() == rackFrame){	//if clicked on the letter rack
			for(int i=0; i<6; i++){
				if((e.getX() < (i+1)*35) && (e.getX() >= i*35)){
					selectLetter = rack[i];
					if(selectLetter != '_'){		//if selected valid letter
						refreshRack(rack);		//reset all rack letter images
						try {					//highlight selected letter image
							Image letterImage = ImageIO.read(new File("img/letters/" + Character.toString(selectLetter).toUpperCase() + ".tiff")).getScaledInstance(35, 35, Image.SCALE_DEFAULT);
							BufferedImage buff = new BufferedImage(35, 35, BufferedImage.TYPE_INT_ARGB);
							buff.getGraphics().drawImage(letterImage, 0, 0 , null);
							RescaleOp rescaleOp = new RescaleOp(1.2f, 15, null);
							rescaleOp.filter(buff, buff);
							rackLetters[i].setIcon(new ImageIcon(buff));
						} catch (IOException error) {
							error.printStackTrace();
						}
						break;
					}
				}
			}
		}
		else if(e.getComponent() == boardFrame){	//if clicked on the board
			if(selectLetter != '_'){	//if a valid letter from the rack has been selected
				for(int j=0; j<15; j++){	//find board position
					for(int i=0; i<15; i++){
						if(e.getX() >= i*35 && e.getX() < (i+1)*35 && e.getY() >= j*35 && e.getY() < (j+1)*35){
							if(grid[i][j].getIcon() == null){	//if that board square is empty
								try {					//highlight selected letter image
									Image letterImage = ImageIO.read(new File("img/letters/" + Character.toString(selectLetter).toUpperCase() + ".tiff")).getScaledInstance(35, 35, Image.SCALE_DEFAULT);
									BufferedImage buff = new BufferedImage(35, 35, BufferedImage.TYPE_INT_ARGB);
									buff.getGraphics().drawImage(letterImage, 0, 0 , null);
									RescaleOp rescaleOp = new RescaleOp(1.2f, 15, null);
									rescaleOp.filter(buff, buff);
									grid[i][j].setIcon(new ImageIcon(buff));
									grid[i][j].setVisible(true);
									moveMask[i][j] = selectLetter;
									for(int l=0; l<6; l++){		//remove that letter from the rack and update it
										if(rack[l] == selectLetter){
											rack[l] = '_';
											selectLetter = '_';
											refreshRack(rack);
											return;
										}
									}	
								} catch (IOException error) {
									error.printStackTrace();
								}
							}
							break;
						}
					}
				}
			}
		}
	}
	
	//resets a move, returning all letters placed on the board to the player
	public void resetBoard(){
		Stack<Character> recoveredLetters = new Stack<Character>();
		for(int j=0; j<15; j++){
			for(int i=0; i<15; i++){
				if(moveMask[i][j] != '_'){	//if this square was changed during this move
					grid[i][j].setIcon(null);
					grid[i][j].setVisible(false);	//remove letter and reset the square it was on
					recoveredLetters.push(moveMask[i][j]);	//collect the recovered letters
					moveMask[i][j] = '_';
				}
			}
		}
		//add recovered letters back to the player rack and refresh the rack GUI
		for(int l=0; l<6 && !recoveredLetters.isEmpty(); l++){
			if(rack[l] == '_')
				rack[l] = recoveredLetters.pop();
		}
		Utilities.printCharArray(rack);
		refreshRack(rack);
	}
	
	public void setUserMove(){
		//System.out.println("Setting user move");
		Move myMove = new Move();
		//System.out.println("My Move: ");
		//myMove.printMove();
		for(int j=0; j<15; j++){	
			for(int i=0; i<15; i++){
				if(moveMask[i][j] != '_'){
					Play p = new Play(i, j, moveMask[i][j]);
					myMove.addPlay(p);
					//moveMask[i][j] = '_';
				}
			}
		}
		
		userMove = myMove;
		//System.out.println("Done setting user move");
		//this.userMove.printMove();
	}
	
	public Move getUserInput(){
		return userMove;
	}
	
	public void resetUserMove(){
		userMove = null;
	}
	
	public void addToBoard(Move m){
		for(Play p : m){
			try {					//highlight selected letter image
				Image letterImage = ImageIO.read(new File("img/letters/" + Character.toUpperCase(p.letter) + ".tiff")).getScaledInstance(35, 35, Image.SCALE_DEFAULT);
				grid[p.x][p.y].setIcon(new ImageIcon(letterImage));
				grid[p.x][p.y].setVisible(true);
				moveMask[p.x][p.y] = '_';	
			} catch (IOException error) {
				error.printStackTrace();
			}
			
		}
	}
	
	//unused mouse events
	public void mouseEntered(MouseEvent e) {/*do nothing*/}
	public void mouseExited(MouseEvent e) {/*do nothing*/}
	public void mousePressed(MouseEvent e) {/*do nothing*/}
	public void mouseReleased(MouseEvent arge0) {/*do nothing*/}

}
