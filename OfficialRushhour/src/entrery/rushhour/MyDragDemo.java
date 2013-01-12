package entrery.rushhour;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MyDragDemo {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	@SuppressWarnings("deprecation")
	private static void createAndShowGUI() {
		JFrame window = new JFrame();
		window.setTitle("RushHour Game");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(new RushHourBoard());
		window.pack();
		window.show();
		window.setLocation(400, 100);
	}
}