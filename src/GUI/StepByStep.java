package GUI;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JProgressBar;

import org.w3c.dom.events.Event;

import cryptography.algorithms;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JScrollBar;
import javax.swing.UIManager;

public class StepByStep {

	private JFrame frame;
	private int stage = 1;
	private JTextField textField_s1_pt;
	private JTextField textField_s1_key;
	JPanel panel_s1;
	JTextArea textArea_display;
	JTextArea textArea_note;
	JTextPane textArea_split;
	JProgressBar progressBar;
	
	private algorithms alg;
	
	

	/**
	 * Create the application.
	 */
	public StepByStep(algorithms alg) {
		this.alg = alg;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 533, 385);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//frame_table = table();
		//frame_table.setVisible(true);
		//frame_table.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame_table.setLocation(700,100);
		
		JPanel panel_main = new JPanel();
		panel_main.setBackground(Color.GRAY);
		panel_main.setForeground(Color.LIGHT_GRAY);
		frame.getContentPane().add(panel_main, BorderLayout.CENTER);
		panel_main.setLayout(null);
		
		progressBar = new JProgressBar(1,6);
		progressBar.setForeground(Color.GREEN);
		progressBar.setBounds(0, 332, 517, 14);
		panel_main.add(progressBar);
		
		// Stage 1
		
		panel_s1 = new JPanel();
		panel_s1.setBounds(10, 45, 241, 276);
		panel_main.add(panel_s1);
		panel_s1.setLayout(null);
		
		JLabel label_s1_pt = new JLabel("Enter plain text:");
		label_s1_pt.setBounds(8, 11, 96, 14);
		panel_s1.add(label_s1_pt);
		
		textField_s1_pt = new JTextField();
		textField_s1_pt.setBounds(8, 25, 223, 20);
		panel_s1.add(textField_s1_pt);
		textField_s1_pt.setColumns(10);
		
		JLabel lable_s1_key = new JLabel("Enter key:");
		lable_s1_key.setBounds(8, 56, 78, 14);
		panel_s1.add(lable_s1_key);
		
		textField_s1_key = new JTextField();
		textField_s1_key.setColumns(10);
		textField_s1_key.setBounds(8, 69, 223, 20);
		panel_s1.add(textField_s1_key);
		
		JPanel panel_sbs = new JPanel();
		panel_sbs.setBounds(261, 11, 246, 310);
		panel_main.add(panel_sbs);
		panel_sbs.setLayout(null);
		
		textArea_note = new JTextArea();
		textArea_note.setBackground(UIManager.getColor("Panel.background"));
		textArea_note.setWrapStyleWord(true);
		textArea_note.setText("NOTE: enter non-numeric values only. For this example, keep plain text and key length to between 10-20 characters.");
		textArea_note.setLineWrap(true);
		textArea_note.setForeground(Color.RED);
		textArea_note.setEditable(false);
		textArea_note.setBounds(10, 11, 221, 100);
		panel_sbs.add(textArea_note);
		
		// Stage 2
		textArea_display = new JTextArea();
		textArea_display.setBackground(UIManager.getColor("Panel.background"));
		textArea_display.setBounds(8, 110, 223, 129);
		panel_s1.add(textArea_display);
		
		// Stage 3
		textArea_split = new JTextPane();
		textArea_split.setBackground(UIManager.getColor("Panel.background"));
		textArea_split.setForeground(Color.BLUE);
		textArea_split.setBounds(10, 107, 221, 147);
		panel_sbs.add(textArea_split);
		
		JButton button_next = new JButton("Next");
		button_next.setBounds(29, 276, 181, 23);
		button_next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (stage) {
				case 1:
					System.out.println("Stage 1.");
					
					if(textField_s1_pt.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Please enter plain text!");
						return;
					}
					
					if (textField_s1_key.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Please enter the correct key!");
						return;
					}
					
					String pt = textField_s1_pt.getText();
					String key = textField_s1_key.getText();
					alg.setKey(key);
					alg.setPlainText(pt);
										
					String show_pt = "Plain Text: \n" + alg.getPlainText() + "\n" + "\nKey: \n" + alg.getKey();
					textArea_display.setText(show_pt);
					
					alg.setKey(key);
					alg.setPlainText(pt);
					
					stage++;
					progressBar.setValue(stage);
					
					break;
				case 2:
					System.out.println("Stage 2.");
					
					String pt_split = alg.getPlainText();
					System.out.println("Step 2 plain text: " + pt_split);
					
					alg.genKey();
					alg.mapKeyAndText();
					try {
						alg.encrypt();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String mapped = alg.getSplitKey();
					
					textArea_note.setText("Below is the generated key repeated to match the length of the plain text message.\n\nNext, we generate the cipher text!");
					textArea_split.setText(pt_split + "\n" + mapped);
					
					stage++;
					progressBar.setValue(stage);
					
					break;
				case 3:
					System.out.println("Stage 3.");
					
					vinTable table = new vinTable();
					textArea_note.setText("To create the cipher text we use the table. \n\nColumn index = character in plaintext\nRow index = coresponding character in key");
					
					stage++;
					progressBar.setValue(stage);
					break;
				case 4:
					System.out.println("Stage 4.");
					
					textArea_split.setText("Plain Text: \n" + alg.getPlainText() + "\n\nKey: \n" + alg.getSplitKey() + "\n\nCipher Text:\n" + alg.getCipherText());
					
					stage++;
					progressBar.setValue(stage);
					
					break;
				case 5:
					System.out.println("Stage 5.");
					
					textArea_note.setText("Encryption complete!");
					
					stage++;
					progressBar.setValue(stage);
					break;
				default:
					frame.dispose();
					progressBar.setValue(stage);
					new User_Interface(new algorithms());
					System.out.println("Case statement default.");
					break;
				}
			}
		});
		panel_sbs.add(button_next);
		
		JButton button_exit = new JButton("EXIT");
		button_exit.setBounds(10, 11, 87, 23);
		button_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
					frame.dispose();
					new User_Interface(new algorithms());
				}
			});
		panel_main.add(button_exit);
		
		
		frame.setVisible(true);
		
	}
}
