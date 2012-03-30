package main;

/**************************************************
/       Wizualizacja sieci domowej				   /
/ @Authors Pawlak Patryk				           /
/ patryk.dev@gmail.com /
/ http://devplus.zzl.org /
/ Systemy Lacznosci Bezprzewodowej          	   /
/ UTP EiT semestr 6, 2010/11                       /
/**************************************************/

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
	        UIManager.setLookAndFeel(
	            UIManager.getCrossPlatformLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) {}
	    catch (ClassNotFoundException e) {}
	    catch (InstantiationException e) {}
	    catch (IllegalAccessException e) {}

		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				JFrame okno = new OknoWiz();
				okno.setResizable(false);
			}		
		});
	}

}
