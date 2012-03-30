package main;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class OknoWiz extends JFrame{
	ImageIcon budyne = new ImageIcon("gfx/budynek700.png");
	
	public OknoWiz(){
		super("Wizualizacja sieci domowej --devplus");
		JPanel budynek = new Budynek();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(budyne.getIconWidth(), budyne.getIconHeight());
		add(budynek);
		setVisible(true);
	}
}
