import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;


public class Main {

	public static void main(String[] args) {
		GUI gui = new GUI();
//		
//		Scanner scan = new Scanner(System.in);
//		System.out.println("wrtie somehting: ");
//		String s = scan.nextLine();
//		
//		gui.consoleWrite(s);
		
		Letterbag l = new Letterbag();
//		Stack st = new Stack();
//		st.push('A');
//		st.push('B');
//		st.push('C');
//		System.out.println(st.get(1));
//		st.removeElementAt(1);
//		System.out.println(st.get(1));

		for(int i=0; i<100; i++) {
			Tile t = l.draw();
			System.out.println("Letter: " + t.letter + " | Value: " + t.value);
		}
		
//		try {
//			Scanner scan = new Scanner(new File("text//letters.txt"));
//			scan.useDelimiter(" ");
//			while(scan.hasNext()) {
//				char c = scan.next().charAt(0);
//				int i = scan.nextInt();
//				System.out.println("Letter: " + c + " | Value: " + i);
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}

	}

}
