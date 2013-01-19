package entrery.rushhour;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

public class ProgressSample {
  public static void main(String args[]) {
	    JDialog f = new WaitDialog(new JFrame());
	    f.show();
    


  }
  
  
  
  
  
  
  
  static class WaitDialog extends JDialog {
	  public WaitDialog(JFrame parent) {
	    super(parent, "Operation in progress", true);
	    JProgressBar progressBar = new JProgressBar();
	    progressBar.setSize(250, 20);
	    progressBar.setIndeterminate(true);
	    Border border = BorderFactory.createTitledBorder("Calculating...");
	    progressBar.setBorder(border);
	    getContentPane().add(progressBar, BorderLayout.CENTER);
	    setLocation(600, 200);
	    setSize(250, 80);
	  }
	}
  
  
  
  
  
  
}
