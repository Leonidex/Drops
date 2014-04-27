import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class FramedWindow extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3849262654124261033L;
	
	JPanel mainPanel;

	JPanel[] bufferPanels = new JPanel[2];
	
	JPanel activePanel;
	
	private Random random = new Random();
	int componentIndex = 0;
	
	private Color color ;
	GradientPaint paint;
	
	@Override
	public void run() 
	{
		validate();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public FramedWindow(GraphicsDevice device)
	{
		super(device.getDefaultConfiguration());
		
		setTitle("Drops");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setPreferredSize(new Dimension(800,800));
		setSize(800,800);
		
		setResizable(false);
		setUndecorated(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		
//		mainPanel = new JPanel();
//		mainPanel.setLayout(null);
		
		for(int i=0; i<bufferPanels.length; i++)
		{
			bufferPanels[i] = new JPanel();
			bufferPanels[i].setLayout(null);
//			bufferPanels[i].setOpaque(false);
//			bufferPanels[i].setBackground(new Color(Color.TRANSLUCENT));
			
//			mainPanel.add(bufferPanels[i]);
//			setGlassPane(bufferPanels[i]);
//			getContentPane().setBackground(getBackground());
		}
		activePanel = bufferPanels[0];
		
		getContentPane().add(bufferPanels[0]);
//		setGlassPane(bufferPanels[1]);
		
//		Graphics2D buffer =  (Graphics2D) getContentPane().getGraphics();
//		Image bufferedImage = (Image)buffer.getComposite();
//		bufferPanels[1].getGraphics().drawImage(bufferedImage, 0, 0, null);
		
		makeDrops();

//		Drop pond1 = new Drop(new Point(0, getHeight()-50), 800, 30);
//		
//		activePanel.add(pond1);
		
		addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent e)
			{
                super.mousePressed(e);
                System.out.println(activePanel.getComponentCount());
//                System.out.println("Press X: " + e.getX() + " Y: " + e.getY());
                Point point = new Point(e.getX(), e.getY());
                
                activePanel.add(new Drop(point));
            }
			
            @Override
            public void mouseReleased(MouseEvent e)
            {
            	super.mouseReleased(e);
            	
//            	System.out.println("Release X: " + e.getX() + " Y: " + e.getY());
//                Point point = new Point(e.getX(), e.getY());
//                
//                activePanel.add(new Drop(point));
            }
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e)
			{
				super.mouseDragged(e);
				
//				System.out.println("Hold X: " + e.getX() + " Y: " + e.getY());
				
//				if(e.getX()%4 == 0 && e.getY()%4 == 0)
//				{
					Point point = new Point(e.getX(), e.getY());
					activePanel.add(new Drop(point));
//				}
			}
		});
	}
	
	
	private void makeDrops()
	{
        repaint();
        validate();
        
        Timer consecTimer = new Timer();
        consecTimer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				Drop newDrop = new Drop(new Point((random.nextInt(getWidth())), random.nextInt(getHeight()/8)));
				activePanel.add(newDrop);
			}	
		}, 0L, 2L);
        
        Timer garbageTimer = new Timer();
        garbageTimer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				activePanel.removeAll();
				
			}	
		}, 0L, 5*60*1000L);
	}
	
	private String getColorString()
	{
		String colorString = null;
		Calendar calendar = new GregorianCalendar();
		int seconds = calendar.get(Calendar.SECOND);
		
		if(seconds<15)
		{
			colorString = "blue";
		}
		else if(seconds<30)
		{
			colorString = "green";
		}
		else if(seconds<45)
		{
			colorString = "red";
		}
		else
		{
			colorString = "pink";
		}
		
		return colorString;
	}
	
	private Color getNewColor(String colorName)
	{
		if(colorName == null)
		{
			this.color = new Color(random.nextInt(20), random.nextInt(55)+120, random.nextInt(55)+200);
			return this.color;
		}
		switch (colorName)
		{
			case "blue":
				this.color = new Color(random.nextInt(20), random.nextInt(55)+120, random.nextInt(55)+200);
				break;
				
			case "green":
				this.color = new Color(random.nextInt(20), random.nextInt(55)+200, random.nextInt(55)+120);
				break;
				
			case "red":
				this.color = new Color(random.nextInt(55)+200, random.nextInt(55), random.nextInt(20));
				break;
				
			case "pink":
				this.color = new Color(random.nextInt(35)+220, random.nextInt(55)+170, random.nextInt(45)+200);
				break;
				
			default:
				this.color = new Color(random.nextInt(20), random.nextInt(55)+120, random.nextInt(55)+200);
		}
		
		return this.color;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponents(g);
		Color sc = getNewColor(getColorString());
		Color ec = getNewColor(getColorString());
		
		paint = new GradientPaint( getWidth() / 2, 0, sc, getWidth()/2, getHeight(), ec, true );
		
		Graphics2D grap = (Graphics2D) getGraphics();
		grap.setPaint(paint);
		
	}
	
	private class Drop extends JComponent{

		/**
		 * 
		 */
		private static final long serialVersionUID = 3486317174629553906L;

		private Point point;
		private int width;
		private int height;
		private Color color;
		
		private double gravity;
		private double fallingTime;
		private double initialPosition;
		
		Timer dropTimer;
		private Random random = new Random();

		public Drop(Point point)
		{
			this.point = point;
			
			this.width = random.nextInt(12)+4;
			this.height = random.nextInt(16)+8;
			
			this.gravity = width*height/100+1;
			this.fallingTime = 0;
			this.initialPosition = point.y;
			
			this.color = getNewColor(getColorString());
			
			setPreferredSize(new Dimension(width, height));
			setBounds(point.x, point.y, width, height);
			setLocation(point);
			repaint();

			this.dropTimer = new Timer();
			this.dropTimer.scheduleAtFixedRate(new TimerTask(){
				@Override
				public void run() {
					dropping();
				}	
			}, 10L, 20L);
		}
		
//		public Drop(Point point, int width, int height)
//		{
//			this.point = point;
//			
//			this.width = width;
//			this.height = height;
//
//			this.gravity = 0;
//			this.fallingTime = 0;
//			this.initialPosition = point.y;
//			
//			setPreferredSize(new Dimension(width, height));
//			setBounds(point.x, point.y, width, height);
//			setLocation(point);
//			repaint();
//		}
		
		public void dropping()
		{
			point.y = (int) (0.5*gravity*Math.pow(fallingTime,2) + initialPosition);
			fallingTime++;
			setBounds(point.x, point.y, width, height);
			
			repaint();
			
			if(point.y > 760)
			{
				dropTimer.cancel();
			}
		}
		
		public void paint(Graphics g)
		{
			Graphics2D g2d = (Graphics2D)g;
			
			g2d.setColor(color);
			
			g2d.fillRoundRect(getWidth()/4, 0, getWidth()/2, getHeight()/2, 10, 10);
			g2d.fillOval(0, getHeight()/4, getWidth(), getHeight());
			
			g2d.setColor(color.darker());

			g2d.fillRoundRect(getWidth()/8*3, 0, getWidth()/4, getHeight()/2, 10, 10);
			g2d.fillOval(getWidth()/4, getHeight()/4, getWidth()/2, getHeight());
			
//            g2d.fillRect(0,0, getWidth(), getHeight());
//            g2d.fillRoundRect(0,0, getWidth(), getHeight(), 2, 5);
//            g2d.fill3DRect(0,0, getWidth(), getHeight(), true);
			
//			int[] xp1 = {150, 250, 325, 375, 450, 275, 100};
//			int[] yp1 = {150, 250, 325, 375, 450, 275, 100};
//			Polygon poly = new Polygon(xp1, yp1, xp1.length);
//			
//			g2d.drawPolygon(poly);
//            g2d.fillPolygon(poly);
//            
//           int[] xp2 = {0, 20, 40};
//           int[] yp2 = {50, 0, 50};
//           poly = new Polygon(xp2, yp2, xp2.length);
//			
//           g2d.drawPolygon(poly);
//           g2d.fillPolygon(poly);
		}
	}
}
