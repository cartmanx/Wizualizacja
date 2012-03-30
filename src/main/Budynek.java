package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextAttribute;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


public class Budynek extends JPanel implements MouseListener, MouseMotionListener{
	final double SCALE=2.851495273;
	/** Rzut parteru z gory */
	private BufferedImage budynek = null;
	/** png AccessPointa */
	private BufferedImage routek = null;
	/** Ikona routera(nasz AP) */
	private JLabel imageLabel ;//= new JLabel("router",iconaRoutera,JLabel.CENTER);
	/** Ikona laptopa */
	private JLabel laptopLabel;
	/** Lokalizacja laptopa lub routera na rzucie budynku */
	private Point polozenieLaptopa, polozenieRoutera;
	/** Po kliknieciu na router zostaje on zaznaczony, mozna go wtedy przesuwac */
	private Boolean ruterZaznaczony=false;
	/** Po kliknieciu na ikone laptopa zostaje on zaznaczony, mozna go wtedy przesuwac */
	private Boolean laptopZaznaczony=false;
	/** Symboliczna linia laczaca AP z laptopem */
	private Line2D.Double polaczenie;
	/** Tablica przechowujaca dane dotyczace scian, ich lokalizacje */
	private Rectangle aSciany[] = new Sciany[14];
	/** Odleglosc pomiedzy routerem a kompem */
	private double distance = 0;
	/** Do testow, ile przeszkod napotyka na swojej drodze sygnal */
	private int iloscInterferencji = 0;
	/** Usredniony wspolczynnik tlumienia dla przeszkod */
	private final double WSP_TLUMIENIA = 6.2;
	/** ProgressBar ustawiony na (-80,-40), pokazuje sile sygnalu */
	private JProgressBar zasieg = new JProgressBar(-80, -40);
	/** Obszar domu w ktorym mozna przemieszczac urzadzenia */
	private Rectangle houseArea0, houseArea1, houseArea2;
	/** Zapisuje ostatnia 'dobra' lokalizacje laptopa by nie wyjsc poza dom */
	private Point tempLaptopLocation = new Point();
	/** Zapisuje ostatnia 'dobra' lokalizacje routera by nie wyjsc poza dom */
	private Point tempRouterLocation = new Point();
	private JLabel stopka = new JLabel("Projekt Systemy Łączności Bezprzewodowej// Patryk Pawlak");
	private JLabel labelSygnal = new JLabel("Dostępnośc sieci :");
	private JButton buttonHelp = new JButton("Informacje");
	/** Kolor lini laczacej lapa z AP */
	private Color lineColor;
	
	
	/** Construct */
	public Budynek(){
		addMouseListener(this);
		addMouseMotionListener(this);
		
		houseArea0 = new Rectangle(141, 189, 284, 284);
		houseArea1 = new Rectangle(425, 251, 116, 201);
		houseArea2 = new Rectangle(236, 472, 148, 59);
		/* sciany ustawiamy */
		aSciany[0] = new Sciany(313,189,4,137);
		aSciany[1] = new Sciany(427, 188, 11, 284);
		aSciany[2] = new Sciany(358, 327, 68, 9);
		aSciany[3] = new Sciany(284, 327, 41,8);
		aSciany[4] = new Sciany(196, 327, 46, 8);
		aSciany[5] = new Sciany(141, 326, 23, 10);
		aSciany[6] = new Sciany(214, 392, 13, 30);
		aSciany[7] = new Sciany(227, 370, 6, 169);
		aSciany[8] = new Sciany(311, 367, 8, 75);
		aSciany[9] = new Sciany(319, 397, 106, 6);
		aSciany[10] = new Sciany(362, 403, 34, 14);
		aSciany[11] = new Sciany(273, 475, 107, 8);
		aSciany[12] = new Sciany(441, 239, 114, 10);
		aSciany[13] = new Sciany(360, 373, 3, 24);

		add(zasieg);
		zasieg.setLocation(140, 600);
		zasieg.setSize(400, 15);
		add(stopka);
		stopka.setLocation(70, 100);
		stopka.setSize(600,60);
		stopka.setFont(new Font("SansSerif", Font.BOLD, 13));
		add(labelSygnal);
		labelSygnal.setLocation(25, 597);
		Font fon = labelSygnal.getFont();
		labelSygnal.setFont(fon.deriveFont(5));
		labelSygnal.setSize(190,20);
	
		add(buttonHelp);
		buttonHelp.setLocation(570, 20);
		buttonHelp.setSize(120, 15);
		buttonHelp.setContentAreaFilled(false);
		buttonHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame informacje = new Informacje();
				informacje.setVisible(true);	
			}
		});
	
		
		try{
			this.polaczenie=new Line2D.Double();	
			budynek = ImageIO.read(new File("gfx/budynek700.png"));
			ImageIcon iconaRoutera = new ImageIcon("gfx/router.png");
			ImageIcon iconaLaptopa = new ImageIcon("gfx/laptop.png");
			laptopLabel = new JLabel("laptop",iconaLaptopa,JLabel.CENTER);
			//imageLabel.setIcon(iconaRoutera);
					//imageLabel = new JLabel("router",iconaRoutera,JLabel.CENTER);
			Image img = iconaRoutera.getImage();
			Image newImg = img.getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
			ImageIcon iconaRoutera2 = new ImageIcon(newImg);
			imageLabel = new JLabel("router",iconaRoutera2,JLabel.CENTER);
		}catch(IOException ex){System.out.println("nie udalo sie wczytac");}
		
		setLayout(null);

		Insets insets = getInsets();

		Dimension size2 = imageLabel.getPreferredSize();
		imageLabel.setBounds(25 +insets.left, 5+insets.top, size2.width, size2.height);
		imageLabel.setLocation(393, 295);
		 
		add(imageLabel);
		polozenieRoutera=imageLabel.getLocation();
		
		size2 =laptopLabel.getPreferredSize();
		laptopLabel.setBounds(25 +insets.left, 5+insets.top, size2.width, size2.height);
		laptopLabel.setLocation(146, 205);
		polozenieLaptopa=laptopLabel.getLocation();
		add(laptopLabel);
		repaint();
	}

	
	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		ruterZaznaczony=false;
		laptopZaznaczony=false;
	}

	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(budynek,0,0,null);
		final Hashtable<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();

        map.put(TextAttribute.FAMILY, "Serif");
        map.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        map.put(TextAttribute.SIZE, new Float(13.0));
     //   AttributedString vanGogh = new AttributedString(
     //           "Tłumienność wynosi: ",
     //           map);

       // String cos = new String("ds");
       // AttributedCharacterIterator.Attribute at = new Attribute(cos);
       // g2.drawString(vanGogh.getIterator(), 1, 21);
		g2.drawString("Tlumiennosc wynosi: "+String.valueOf(getTlumiennosc(distance))+ "dB", 10, 10);
		zasieg.setValue((int)getSilaSygnalu(false));
		g2.drawString("Siła sygnału w przestrzeni wynosi:(dla mocy wyjściowej 20dB)  "+getSilaSygnalu(true)+ "dB",10,30);
		g2.drawString("Siła sygnału uwzględniając ściany wynosi:(dla mocy wyjściowej 20dB)  "+getSilaSygnalu(false)+ "dB",10,50);
		g2.drawString("Odleglość urządzeń: "+(int)distance+" cm", 10, 70);
		g2.setColor(Color.RED);
		//for(Rectangle x:aSciany){
		//	g2.draw(x);
		//}
		g2.setColor(lineColor);
		polaczenie.setLine(polozenieLaptopa.getX(), polozenieLaptopa.getY(), polozenieRoutera.getX(), polozenieRoutera.getY());
		g2.draw(polaczenie);
		changeBarColor();
		zasieg.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(ruterZaznaczony==true && checkArea(arg0)==true){
			imageLabel.setLocation(arg0.getPoint());
			polozenieRoutera=imageLabel.getLocation();
			tempRouterLocation=imageLabel.getLocation();
		}
		if(ruterZaznaczony==true && checkArea(arg0)==false){
			imageLabel.setLocation(tempRouterLocation);
			polozenieRoutera = imageLabel.getLocation();
		}
		if(laptopZaznaczony==true && checkArea(arg0)==true){
			laptopLabel.setLocation(arg0.getPoint());
			polozenieLaptopa=laptopLabel.getLocation();
			tempLaptopLocation = laptopLabel.getLocation();

		}
		if(laptopZaznaczony==true && checkArea(arg0)==false){
			laptopLabel.setLocation(tempLaptopLocation);
			polozenieLaptopa=laptopLabel.getLocation();	
		}
		//-------------------------------------------
		System.out.println("kliknales: "+arg0.getX()+"  "+arg0.getY());
		iloscInterferencji = 0;
		for(Rectangle x: aSciany){
			if(x.intersectsLine(polaczenie)){
				iloscInterferencji++;
			}
		}

		System.out.println("ilosc interferencji: "+iloscInterferencji);
		System.out.println("POLOZENIE LAPA: "+polozenieLaptopa+"  POLOZENIE ROUTERA: "+polozenieRoutera);
		distance = (polozenieLaptopa.distance((Point2D)polozenieRoutera))*SCALE;
		System.out.println(distance);
		System.out.println("tlumiennosc: "+getTlumiennosc(distance));
		if(imageLabel.getX()-10  < arg0.getPoint().getX()){
			if(arg0.getPoint().getX() < imageLabel.getX()+30){
				if(imageLabel.getY()-5  < arg0.getPoint().getY()){
					if(arg0.getPoint().getY() < imageLabel.getY()+30){
						System.out.println("zaznaczyles dobrze");
						if(laptopZaznaczony==false)
						ruterZaznaczony=true;
					}else{
						ruterZaznaczony=false;
					}
				}else{
					ruterZaznaczony=false;
				}
			}else{
				ruterZaznaczony=false;
			}
		}else{
			ruterZaznaczony=false;
		}
	
		/*teraz dla laptopa przejscie czy zaznaczony jest */
		if(laptopLabel.getX()-10  < arg0.getPoint().getX()){
			if(arg0.getPoint().getX() < laptopLabel.getX()+50){
				if(laptopLabel.getY()-5  < arg0.getPoint().getY()){
					if(arg0.getPoint().getY() < laptopLabel.getY()+50){
						System.out.println("zaznaczyles dobrze");
						if(ruterZaznaczony==false)
						laptopZaznaczony=true;
					}else{
						laptopZaznaczony=false;
					}
				}else{
					laptopZaznaczony=false;
				}
			}else{
				laptopZaznaczony=false;
			}
		}else{
			laptopZaznaczony=false;
		}	
		//-------------------------------------------
		zasieg.repaint();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}
	
	/** Zwraca tlumienie w dB ze wzoru
	 * L = 20×log dist+20×log 2400+32.4
	 * @param dist odleglosc
	 * @return
	 */
	public double getTlumiennosc(double dist){
		double f = dist/100000;//zeskalowanie na kilometry
		double x = (20*(Math.log10(f))) + ((20*Math.log10(2400)))+32.4;
		return x;
	}
	
	public double getSilaSygnalu(boolean bezScian){
		double x;
		if(bezScian==true){
			x = 20- getTlumiennosc(distance);
		}else{
			x = 20- getTlumiennosc(distance) - (WSP_TLUMIENIA*iloscInterferencji);
		}
		return x;
	}
	
	/** Dostosowywuje kolor progressBara w zaleznosci od sily sygnalu */
	public void changeBarColor(){
		if(getSilaSygnalu(false)>=-45){
			zasieg.setForeground(Color.GREEN);
			lineColor = Color.green;
		}
		if(getSilaSygnalu(false)>=-64 & getSilaSygnalu(false) <-45){
			zasieg.setForeground(Color.ORANGE);
			lineColor = Color.ORANGE;
		}
		if(getSilaSygnalu(false)<-64 & getSilaSygnalu(false) >=-72){
			zasieg.setForeground(Color.LIGHT_GRAY);
			lineColor = Color.LIGHT_GRAY;
		}
		if(getSilaSygnalu(false)<-72){
			zasieg.setForeground(Color.DARK_GRAY);
			lineColor = Color.DARK_GRAY;
		}
	}
	
	/** Sprawdza czy user nie probuje ustawic urzadzenia poza budynkiem */
	public boolean checkArea(MouseEvent arg0){
		if(laptopZaznaczony==true || ruterZaznaczony==true){
		if(houseArea0.contains(polozenieLaptopa) || houseArea1.contains(polozenieLaptopa)
				|| houseArea2.contains(polozenieLaptopa)){
			if(houseArea0.contains(arg0.getPoint()) || houseArea1.contains(arg0.getPoint()) || houseArea2.contains(arg0.getPoint())){
				return true;
			}
			return false;
		}
			//poza domem			
		//------
			if(houseArea0.contains(polozenieRoutera) || houseArea1.contains(polozenieRoutera)
					|| houseArea2.contains(polozenieRoutera)){
				if(houseArea0.contains(arg0.getPoint()) || houseArea1.contains(arg0.getPoint())
						|| houseArea2.contains(arg0.getPoint())){
					return true;
				}
				return false;
			}else	
				//poza domem
				return false;
		}else return false;		

	
	}
	
}
