package practice01;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Main implements ActionListener {
	
	private static JLabel userLabel;
	private static JTextField userText;
	private static JLabel passwordlabel;
	private static JPasswordField passwordText;
	private static JButton button;
	private static JButton button2;
	private static JLabel success;
	
	
	
	public static void main(String[] args) { 
		JPanel panel = new JPanel();
		JFrame frame = new JFrame();
		frame.setSize(350,300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(panel);
		
		panel.setLayout(null);
		
		
		JLabel userlabel = new JLabel("Email Address");
		userlabel.setBounds(10, 20, 80, 25);
		panel.add(userlabel);
		
		userText = new JTextField(20);
		userText.setBounds(100,20,165,25);
		panel.add(userText);
		
	    passwordlabel = new JLabel("Password");
		passwordlabel.setBounds(10, 50, 80, 25);
		panel.add(passwordlabel);
		
		passwordText = new JPasswordField(20);
		passwordText.setBounds(100,50,165,25);
		panel.add(passwordText);
		
		button = new JButton("Login");
		button.setBounds(10,80,120,25);
		button.addActionListener(new Main());
		panel.add(button);
		
		
		success = new JLabel("");
		success.setBounds(10,110,300,25);
		panel.add(success);
		
		
		 button2 = new JButton("Register");
		button2.setBounds(180,80,120,25);
		button2.addActionListener(new Main());
		panel.add(button2);
		
		
		frame.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if(e.getSource()==button2) {
			JFrame registration = new RegistrationForm();
			registration.setVisible(true);
			
		}
		if(e.getSource() == button) {
			String user = userText.getText();
			String password = passwordText.getText();
			System.out.println(user + ", " + password);
			// TODO Auto-generated method stub
			
			if(user.contentEquals("sam") && password.equals("sam")) {
				success.setText("Login is successful!!!");
			}else {
				success.setText("Login Failed");
			}
		}
		
	}  
	
	
}
