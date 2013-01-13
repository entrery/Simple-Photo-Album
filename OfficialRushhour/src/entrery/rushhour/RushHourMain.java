package entrery.rushhour;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import entrery.rushhour.ai.AStarSolutionFinder;
import entrery.rushhour.ai.State;
import entrery.rushhour.ai.VehicleType;
import entrery.rushhour.heuristics.BlockingVehiclesHeuristic;

public class RushHourMain {

	private static JFrame window;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI() {
		window = new JFrame();
		window.setTitle("RushHour Game");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = createMainPanel(new RushHourBoard(createVehicles(), false));
		paintWindow(mainPanel);	
	}
	
	@SuppressWarnings("deprecation")
	public static void paintWindow(JPanel mainPanel) {
		window.setContentPane(mainPanel);
		window.pack();
		window.show();
		window.setLocation(400, 100);
		window.repaint();
	}

	private static JPanel createMainPanel(RushHourBoard board) {
		JPanel mainPanel = new JPanel();
		mainPanel.add(board);
		mainPanel.add(createButton(board));
		return mainPanel;
	}

	private static Component createButton(State state) {
		JButton button = new JButton("Solve");
		button.addActionListener(new ButtonListener(state));

		return button;
	}
	
	public static class ButtonListener implements ActionListener {
		private State state;
		
		public ButtonListener(State state) {
			this.state = state;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			AStarSolutionFinder finder = new AStarSolutionFinder(new BlockingVehiclesHeuristic());
			List<State> solutionStates = finder.solve(state);
			
			for (State state : solutionStates) {
				RushHourBoard board = (RushHourBoard)state;
				JPanel mainPanel = createMainPanel(board);
				paintWindow(mainPanel);
			}
		}
	}
	
	private static List<Vehicle> createVehicles() {
		List<Vehicle> arrayList = new ArrayList<Vehicle>();
		
		arrayList.add(new HorizontalVehicle(0, 0, 240, 80, Color.GREEN, VehicleType.Horizontal, false, 1));
		arrayList.add(new HorizontalVehicle(160, 160, 240, 80, Color.GREEN, VehicleType.Horizontal, false, 2));
		arrayList.add(new HorizontalVehicle(80, 400, 240, 80, Color.GREEN, VehicleType.Horizontal, false, 3));
		arrayList.add(new HorizontalVehicle(320, 400, 160, 80, Color.YELLOW, VehicleType.Horizontal, false, 4));
		arrayList.add(new VerticalVehicle(160, 240, 80, 160, Color.RED, VehicleType.Vertical, true, 5));
		arrayList.add(new VerticalVehicle(400, 80, 80, 240, Color.BLUE, VehicleType.Vertical, false, 6));
		arrayList.add(new VerticalVehicle(320, 240, 80, 160, Color.BLUE, VehicleType.Vertical, false, 7));
		arrayList.add(new VerticalVehicle(0, 320, 80, 160, Color.BLUE, VehicleType.Vertical, false, 8));

		return arrayList;
	}
}