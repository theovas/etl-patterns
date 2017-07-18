/**
 * 
 */
package patternGeneration;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JRadioButton;

/**
 * @author vasileios
 *
 */
public class GUI {

	private JFrame frame;
	private JTextField txtFbffgf;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JInternalFrame internalFrame = new JInternalFrame("New JInternalFrame");
		frame.getContentPane().add(internalFrame, BorderLayout.CENTER);
		internalFrame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		internalFrame.getContentPane().add(splitPane);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		
		JToolBar toolBar = new JToolBar();
		splitPane.setRightComponent(toolBar);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("New radio button");
		toolBar.add(rdbtnNewRadioButton_1);
		
		JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("New radio button");
		toolBar.add(rdbtnNewRadioButton_2);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("New radio button");
		toolBar.add(rdbtnNewRadioButton);
		
		txtFbffgf = new JTextField();
		txtFbffgf.setText("fbffgf");
		toolBar.add(txtFbffgf);
		txtFbffgf.setColumns(10);
		internalFrame.setVisible(true);
	}

}
