package main;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Rectangle;

public class Informacje extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextArea jTextArea = null;
	private JScrollPane sbrText; //  @jve:decl-index=0:visual-constraint="536,16"
	/**
	 * This is the default constructor
	 */
	public Informacje() {
		super("Informacje..");
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		
		sbrText = new JScrollPane(getJTextArea());
		sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sbrText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.getContentPane().add(sbrText);
		this.setSize(500,250);
	}

	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea() {
		
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setBounds(new Rectangle(3, 4, 456, 280));
			jTextArea.setEditable(false);
			jTextArea.setLineWrap(false);
			jTextArea.setText("Projekt z przedmiotu 'Systemy Łączności Bezprzewodowej' \n" +
					"------------------------------------------------" +
					"-----------------------------------------------\n Wizualizacja ta jest " +
					"interaktywnym uzupełnieniem projektu, który\n polegał na zaprojektowaniu sieci " +
					"bezprzewodowej dla domku\n jednorodzinnego.\n\n " +
					"Aplikacja pokazuje, że w przypadku typowego domu nie będzie\n " +
					"problemów z rozmieszczeniem urządzeń i praktycznie cały dom pokryty\n " +
					"jest dobrym sygnałem. Można też zauważyc, że najoptymalniej jest gdy\n " +
					"AP umieszczony jest w centrum domu.\n Wizualizacja ta stanowi tylko element poglądowy" +
					" i jest modelem mocno\n uproszczonym. Nie uwzględnia chociażby na jakiej wysokości" +
					" czy blisko\n jakich urządzeń znajduje się AP.\n\n" +
					"Zastosowany wzór na tłumienie: L = 20*log(x) + 20*log(f) + 32,4,\n" +
					"gdzie x=długość drogi w km, f=częstotliwość w MHz, L=tłumienie w dB\n\n" +
					"Zakładamy że do dyspozycji mamy największą moc na jaką pozwala\n" +
					"prawo, czyli 100mW e.i.r.p (20dBm)\n\n" +
					"Tłumienie przeszkód zostało uśrednione i wynosi 6,2dB dla każdej z nich.\n\n" +
					"EiT, UTP2010/2011 sem VI" +
					"\nPatryk Pawlak");
		}
		return jTextArea;
	}

	/**
	 * This method initializes jScrollBar	
	 * 	
	 * @return javax.swing.JScrollBar	
	 */
	


}  //  @jve:decl-index=0:visual-constraint="10,10"
