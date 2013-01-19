package entrery.rushhour.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import entrery.rushhour.HorizontalVehicle;
import entrery.rushhour.RushHourBoard;
import entrery.rushhour.Vehicle;
import entrery.rushhour.VerticalVehicle;
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
		private final State state;
		
		public ButtonListener(State state) {
			this.state = state;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {			
			new Thread() {
				public void run() {
					AStarSolutionFinder finder = new AStarSolutionFinder(new BlockingVehiclesHeuristic());
					
					final WaitDialog waitDialog = new WaitDialog(window);					
					
					SwingUtilities.invokeLater(new Runnable() {						
						@Override
						public void run() {
							waitDialog.setVisible(true);
						}					
					});		
									
					long startTime = System.currentTimeMillis();
					List<entrery.rushhour.ai.State> solutionStates = finder.solve(state);
					long endTime = System.currentTimeMillis();
					
					SwingUtilities.invokeLater(new Runnable() {						
						@Override
						public void run() {
							waitDialog.setVisible(false);
							waitDialog.dispose();
						}					
					});
										
					System.out.println("Solution found for " + (endTime - startTime) / 1000 + " seconds");
					for (final entrery.rushhour.ai.State state : solutionStates) {						
						System.out.println(Thread.currentThread() + " First dump");
						SwingUtilities.invokeLater(new Runnable() {						
							@Override
							public void run() {
								RushHourBoard board = (RushHourBoard)state;
								JPanel mainPanel = createMainPanel(board);
								paintWindow(mainPanel);
								System.out.println(Thread.currentThread());
							}					
						});		
						
						try {
							Thread.sleep(1700L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}						
					}
				}
			}.start();		
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
	
	static class WaitDialog extends JDialog {
		private static final long serialVersionUID = 1L;

		public WaitDialog(JFrame parent) {
			super(parent, "Operation in progress", true);
			JProgressBar progressBar = new JProgressBar();
			progressBar.setSize(250, 20);
			progressBar.setIndeterminate(true);
			Border border = BorderFactory.createTitledBorder("Calculating...");
			progressBar.setBorder(border);
			getContentPane().add(progressBar, BorderLayout.CENTER);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setLocation(570, 230);
			setSize(250, 80);
		}
	}
}