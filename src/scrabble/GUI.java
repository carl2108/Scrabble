package scrabble;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
	
	private JLayeredPane layeredPane;
	private JLabel test;
 
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
		
		layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(525, 525));
		
//		test = new JLabel();
//		test.setPreferredSize(new Dimension(35, 35));
//		
//		Image letterImage;
//		try {
//			letterImage = ImageIO.read(new File("img/letters/A.tiff")).getScaledInstance(35, 35, Image.SCALE_DEFAULT);
//			test.setIcon(new ImageIcon(letterImage));
//			layeredPane.add(test, 0);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		
		
		//board/letter mask layers
		boardFrame = new JLabel();	//board JFrame container
		layeredPane.add(boardFrame, 1);	//add board frame as default layer to layered pane
		
		//rack frame and letter JLabels
		rackFrame = new JLabel();		//rack JFrame container
		rackFrame.setLayout(new BoxLayout(rackFrame, BoxLayout.X_AXIS));
		rackLetters = new JLabel[6];	//create and add 6 letter JLabel containers to be nested in the rack container
		for(int i = 0; i<6; i++){
			rackLetters[i] = new JLabel();
			rackFrame.add(rackLetters[i]);
		}
		
		boardImage = new ImageIcon("img/scrabble_board.jpg").getImage().getScaledInstance(525, 525, Image.SCALE_DEFAULT);	
		boardFrame.setIcon(new ImageIcon(boardImage));
		boardFrame.setSize(525, 525);
		
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
					.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 525, GroupLayout.PREFERRED_SIZE)
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
						.addComponent(layeredPane, Alignment.LEADING))
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
