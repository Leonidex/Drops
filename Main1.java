import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;


public class Main1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		
		FramedWindow win = new FramedWindow(device);
		new Thread(win).run();
	}
}
