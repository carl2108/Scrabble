package scrabble;

import java.awt.Image;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
 
	public void run() {
		while(true);
	}
	//Constructor
	public GUI() {
		initialize();
		System.out.println("GUI Contructor");
		this.frame.setVisible(true);
	}

	//Initialize
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 0, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		boardFrame = new JLabel("board");	//board JFrame container
		rackFrame = new JLabel("rack");		//rack JFrame container
		
		boardImage = new ImageIcon("img//scrabble_board.jpg").getImage().getScaledInstance(525, 525, Image.SCALE_DEFAULT);
		boardFrame.setIcon(new ImageIcon(boardImage));
		
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
	
	public void consoleWrite(String s) {
		doc = txtConsole.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), s + "\n", null);	//Updates doc storing textConsole text
			vertical.setValue(vertical.getMaximum());			//Scroll down as text is output to console
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
}
