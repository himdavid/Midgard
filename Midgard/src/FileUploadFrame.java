import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class FileUploadFrame extends JFrame {

	private JPanel contentPane;
	private static final long serialVersionUID = 1L;
	private JTextField filePath = new JTextField(), 
			dir = new JTextField(), delimiter = new JTextField();
	private JButton open = new JButton("Upload File");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileUploadFrame frame = new FileUploadFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FileUploadFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		open.addActionListener(new OpenL());
		contentPane.add(open);
		Container cp = getContentPane();
		//cp.add(contentPane, BorderLayout.SOUTH);
		dir.setEditable(false);
		filePath.setEditable(false);
		delimiter.setEditable(true);
		contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(2, 1));
		contentPane.add(filePath);
		contentPane.add(dir);
		contentPane.add(delimiter);
		cp.add(contentPane, BorderLayout.NORTH);
	}
	
	 class OpenL implements ActionListener {
		    public void actionPerformed(ActionEvent e) {
		      JFileChooser c = new JFileChooser();
		      // Demonstrate "Open" dialog:
		      int rVal = c.showOpenDialog(FileUploadFrame.this);
		      if (rVal == JFileChooser.APPROVE_OPTION) {
		    	filePath.setText(c.getSelectedFile().getAbsolutePath());
		        dir.setText(c.getCurrentDirectory().toString());
		      }
		      if (rVal == JFileChooser.CANCEL_OPTION) {
		        filePath.setText("You pressed cancel");
		        dir.setText("");
		      }    
		    }
		  }


}
