package entrery.rushhour.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import entrery.rushhour.heuristics.EuclideHeuristic;
import entrery.rushhour.heuristics.NullHeuristics;

public class RushHourMain {

	private static JFrame window;
	private static int selectionIndex = -1;
	private static Iterator<State> stateIterator;
	private static JButton nextButton;
	private static JButton showButton;
	
	
	
	
	public static void main(String[] args) {
		try {
		    System.setOut(new PrintStream(new File("C:\\Users\\EnTrERy\\Desktop\\rushLog.txt")));
		} catch (Exception e) {
		     e.printStackTrace();
		}
		
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
		JPanel mainPanel = createMainPanel(new RushHourBoard(createVehicles(), false), false);
		paintWindow(mainPanel);	
	}
	
	@SuppressWarnings("deprecation")
	public static void paintWindow(JPanel mainPanel) {
		window.getContentPane().removeAll();
		window.setContentPane(mainPanel);
		window.pack();
		window.show();
		window.setLocation(400, 100);
		//window.setResizable(false);
		window.repaint();
	}

	private static JPanel createMainPanel(RushHourBoard board, boolean buttonsEnabled) {
		JPanel mainPanel = new JPanel();
		mainPanel.add(board);
		
		GridLayout gridLayout = new GridLayout(4, 1);
		gridLayout.setVgap(5);
		JPanel buttonsPanel = new JPanel(gridLayout);
		buttonsPanel.add(createButton(board));
		buttonsPanel.add(createComboBox());
		buttonsPanel.add(createNextButton(buttonsEnabled));
		buttonsPanel.add(createShowButton(buttonsEnabled));
		
		mainPanel.add(buttonsPanel);
		
		return mainPanel;
	}

	private static Component createShowButton(boolean enabled) {
		showButton = new JButton("Show");
		showButton.setSize(new Dimension(100, 25));
		showButton.setEnabled(enabled);
		showButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						if(stateIterator != null && stateIterator.hasNext()) {
							while (stateIterator.hasNext()) {
								
								SwingUtilities.invokeLater(new Runnable() {						
									@Override
									public void run() {
										RushHourBoard board = (RushHourBoard) stateIterator.next();
										JPanel mainPanel = createMainPanel(board, true);
										paintWindow(mainPanel);
									}					
								});
								
								try {
									Thread.sleep(1700L);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							
							hideButtons();
						}
					}
				}).start();
			}
		});
		
		return showButton;
	}

	public static void hideButtons() {
		if(nextButton != null && showButton != null) {
			nextButton.setEnabled(false);
			showButton.setEnabled(false);
		}
	}
	
	private static Component createNextButton(boolean nextButtonEnabled) {
		nextButton = new JButton("Next");
		nextButton.setEnabled(nextButtonEnabled);
		nextButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (stateIterator != null && stateIterator.hasNext()) {

					RushHourBoard board = (RushHourBoard) stateIterator.next();
					JPanel mainPanel = createMainPanel(board, true);
					paintWindow(mainPanel);

				} else {
					hideButtons();
				}
			}
		});
		
		return nextButton;
	}

	private static Component createComboBox() {
		JComboBox<String> combo = new JComboBox<String>();
		
		combo.addItem("config1");
		combo.addItem("config2");
		combo.addItem("config3");
		combo.addItem("config4");
		combo.setSelectedIndex(selectionIndex);
		
		combo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				@SuppressWarnings("rawtypes")
				JComboBox jComboBox = (JComboBox)event.getSource();
				String fileName = (String) jComboBox.getSelectedItem();
				selectionIndex = jComboBox.getSelectedIndex();
				
				RushHourBoard board = new RushHourBoard(createVehicles(fileName), false);
				JPanel mainPanel = createMainPanel(board, false);
				paintWindow(mainPanel);
			}
		});
		
		
		return combo;
	}
	
	private static Component createButton(State state) {
		JButton button = new JButton("Solve");
		button.setPreferredSize(new Dimension(100,30));
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
//					AStarSolutionFinder finder = new AStarSolutionFinder(new BlockingVehiclesHeuristic());
//					AStarSolutionFinder finder = new AStarSolutionFinder(new NullHeuristics());
					AStarSolutionFinder finder = new AStarSolutionFinder(new EuclideHeuristic());
					
					final WaitDialog waitDialog = new WaitDialog(window);					
					
					SwingUtilities.invokeLater(new Runnable() {						
						@Override
						public void run() {
							waitDialog.setVisible(true);
						}					
					});		
									
					long startTime = System.currentTimeMillis();
					
					List<entrery.rushhour.ai.State> solutionStates = finder.solve(state);
					stateIterator = solutionStates.iterator();
					nextButton.setEnabled(true);
					showButton.setEnabled(true);
							
					long endTime = System.currentTimeMillis();
					
					SwingUtilities.invokeLater(new Runnable() {						
						@Override
						public void run() {
							waitDialog.setVisible(false);
							waitDialog.dispose();
						}					
					});
										
					System.out.println("Solution found for " + (endTime - startTime) / 1000 + " seconds");
					
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
	
	private static List<Vehicle> createVehicles(String fileName) {
		List<Vehicle> arrayList = new ArrayList<Vehicle>();

		try {
		  //FileInputStream fstream = new FileInputStream("resources/" + fileName + ".txt");
		  InputStream fstream = RushHourMain.class.getClassLoader().getResourceAsStream("resources/" + fileName + ".txt");
		  		  
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine;
		  String array[] = null;
		  while ((strLine = br.readLine()) != null)   {
			  array = strLine.split(",");
			  int x = Integer.parseInt(array[0]) * RushHourBoard.CELL_SIZE;
			  int y = Integer.parseInt(array[1])* RushHourBoard.CELL_SIZE;
			  int w = Integer.parseInt(array[2])* RushHourBoard.CELL_SIZE;
			  int h = Integer.parseInt(array[3])* RushHourBoard.CELL_SIZE;
			  Color color = colorConverter(array[4]);
			  VehicleType type = VehicleType.valueOf(array[5]);
			  boolean isRed = Boolean.valueOf(array[6]);
			  int index = Integer.parseInt(array[7]);

			  if(type.equals(VehicleType.Vertical)) {
				  arrayList.add(new VerticalVehicle(x, y, w, h, color, type, isRed, index));
			  } else {
				  arrayList.add(new HorizontalVehicle(x, y, w, h, color, type, isRed, index));
			  }
		}
	
		  br.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return arrayList;
	}
	
	
	private static Color colorConverter(String color) {
		if(color.equals("red")) {
			return Color.RED;
		} else if (color.equals("blue")) {
			return Color.BLUE;
		} else if (color.equals("pink")) {
			return Color.PINK;
		} else if (color.equals("yellow")) {
			return Color.YELLOW;
		} else if(color.equals("green")) {
			return Color.GREEN;
		} else return Color.BLACK;
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