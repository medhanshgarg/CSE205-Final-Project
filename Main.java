
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Main implements ActionListener {
	
	private static JTextField emailText;
	private static JLabel passwordlabel;
	private static JPasswordField passwordText;
	private static JButton button;
	private static JButton button2;
	private static JLabel success;
	static JFrame frame;
	
	
	
	public static void main(String[] args) { 
		
		JPanel panel = new JPanel();
		frame = new JFrame();
		frame.setSize(350,300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(panel);
		
		panel.setLayout(null);
		
		
		JLabel emailLabel = new JLabel("Email Address");
		emailLabel.setBounds(10, 20, 80, 25);
		panel.add(emailLabel);
		
		emailText = new JTextField(20);
		emailText.setBounds(100,20,165,25);
		panel.add(emailText);
		
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
			frame.dispose();
		}
		if(e.getSource() == button) {
			String email = emailText.getText();
			String password = passwordText.getText();
			
			PostgresConnection db = new PostgresConnection();
			int id = db.findId(email);
			
			if(db.getPassword(id).equals(password)) {
				success.setText("Login is successful!!!");
				if (db.getPosition(db.findId(email)).equals(User.CUSTOMER_POSITION)) {
					openShoppingPage(new User(id));
				} else {
					openItemModification(db.getPosition(db.findId(email)), new ItemList());
				}
				frame.dispose();
			}else {
				success.setText("Login Failed");
			}
			db.close();
			return;
		}
		
	}  	
	
	public static void openCheckOut(User user) {
		CheckOutPage cp = new CheckOutPage(user);
	}
	
	public static void openShoppingPage(User user) {
		openShoppingPage(user, "");
	}
	
	public static void openShoppingPage(User user, String search) {
		ShoppingPage sp = new ShoppingPage(user, search);
	}
	
	public static void openItemModification(String position, ItemList itemBank) {
		ItemModification im = new ItemModification(position, itemBank);
	}
	
	public static void openPendingOrders(ItemList itemBank) {
		PendingOrders po = new PendingOrders(itemBank);
	}
	
}
