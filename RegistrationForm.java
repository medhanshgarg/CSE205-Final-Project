import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class RegistrationForm extends JFrame implements ActionListener {
	
	JLabel label1, label2, label3, label4, label5, supervisingManager;
	JTextField t1,t2,t3,t4,managerEmailText,managerPasswordText;
	JRadioButton male,female;
	JComboBox day,month,year;
	JTextArea ta1;
	JCheckBox terms, employee, manager;
	JButton submit, position;
	JLabel msg, managerEmailLabel, managerPasswordLabel;
	User user;
	Container c;
	Boolean isStaff = false;
	
	
	RegistrationForm() {
		setTitle("Registration Form");
		setSize(700,500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		c=getContentPane();
		c.setLayout(null);
		
		label1=new JLabel("Username");
		label1.setBounds(20,50,100,20);
		c.add(label1);
		
		t1=new JTextField();
		t1.setBounds(130,50,100,20);
		c.add(t1);
		
		label2=new JLabel("Password");
		label2.setBounds(20,100,100,20);
		c.add(label2);
		
		t2=new JTextField();
		t2.setBounds(130,100,100,20);
		c.add(t2);
		
		label3=new JLabel("Full Name");
		label3.setBounds(20,150,100,20);
		c.add(label3);
		
		t3=new JTextField();
		t3.setBounds(130,150,150,20);
		c.add(t3);
		
		label4=new JLabel("Email Address");
		label4.setBounds(20,200,100,20);
		c.add(label4);
		
		t4=new JTextField();
		t4.setBounds(130,200,200,20);
		c.add(t4);
		
		
		
		label5=new JLabel("Address");
		label5.setBounds(20,250,100,20);
		c.add(label5);
		
		ta1=new JTextArea();
		ta1.setBounds(130,240,200,50);
		c.add(ta1);
		
		terms=new JCheckBox("Accept terms and conditions");
		terms.setBounds(50,300,200,20);
		c.add(terms);
		
		submit=new JButton("submit");
		submit.setBounds(150,350,80,20);
		submit.addActionListener(this);
		c.add(submit);
		
		msg=new JLabel();
		msg.setBounds(20,400,250,20);
		c.add(msg);
		
		position=new JButton("new employee");
		position.setBounds(500,50,120,20);
		position.addActionListener(this);
		c.add(position);		
		
		setVisible(true);
	}
	
	public void staffRegistration() {
		
		isStaff = true;
		
		c.remove(label5);
		c.remove(ta1);
		
		employee=new JCheckBox("Employee", true);
		employee.setBounds(50,250,80,20);
		employee.addActionListener(this);
		c.add(employee);
		
		manager=new JCheckBox("Manager");
		manager.setBounds(150,250,80,20);
		manager.addActionListener(this);
		c.add(manager);
		
		supervisingManager = new  JLabel("Supervisor Information");
		supervisingManager.setBounds(500,100,150,20);
		c.add(supervisingManager);
		
		managerEmailLabel = new  JLabel("Email");
		managerEmailLabel.setBounds(420,130,150,20);
		c.add(managerEmailLabel);
		
		managerEmailText=new JTextField();
		managerEmailText.setBounds(490,130,150,20);
		c.add(managerEmailText);
		
		managerPasswordLabel = new  JLabel("Password");
		managerPasswordLabel.setBounds(420,160,150,20);
		c.add(managerPasswordLabel);
		
		managerPasswordText=new JTextField();
		managerPasswordText.setBounds(490,160,150,20);
		c.add(managerPasswordText);
		
		position.setText("new customer");
		
		c.repaint();
		
	}
	
	public void customerRegistration() {
		
		isStaff = false;
		
		c.add(label5);
		c.add(ta1);
		
		c.remove(employee);
		c.remove(manager);
		c.remove(supervisingManager);
		c.remove(managerEmailLabel);
		c.remove(managerEmailText);
		c.remove(managerPasswordLabel);
		c.remove(managerPasswordText);
		
		position.setText("new employee");
		
		c.repaint();
		
	}
	
	public void failSubmit() {
		submit.setText("OOPS!");
		JLabel tryAgainLabel = new JLabel("Try again!");
		tryAgainLabel.setBounds(160,370,80,20);
		c.add(tryAgainLabel);
		c.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == submit && terms.isSelected()) {
			
			if (isStaff) {
				PostgresConnection db = new PostgresConnection();
				int id = db.findId(managerEmailText.getText());
				if(!db.getPassword(id).equals(managerPasswordText.getText()) || !(db.getPosition(id).equals(User.MANAGER_POSITION))) {
					db.close();
					failSubmit();
					return;
				}
				db.close();
			}
			
			User user = new User();
			user.setPosition(User.CUSTOMER_POSITION);
			if (isStaff) {
				if (employee.isSelected()) {
					user.setPosition(User.EMPLOYEE_POSITION);
				} else {
					user.setPosition(User.MANAGER_POSITION);
				}
			}
			user.setPassword(t2.getText());
			user.setName(t3.getText());
			user.setEmail(t4.getText());
			user.setAddress(ta1.getText());
			if (user.addToDatabase()) {
				submit.setText("REGISTRATION SUCCESSFUL!!");
				if (!user.getPosition().equals(User.CUSTOMER_POSITION)) {
					Main.openItemModification(user.getPosition(), new ItemList());
				} else {
					Main.openShoppingPage(user);
				}
				dispose();
				return;
			} else {
				failSubmit();
				return;
			}
			
		}
		
		if (e.getSource() == position) {
			if (isStaff) {
				customerRegistration();
				return;
			}
			staffRegistration();
			return;
		}
		
		if (e.getSource() == employee) {
			manager.setSelected(false);
			employee.setSelected(true);
			return;
		} else if (e.getSource() == manager) {
			manager.setSelected(true);
			employee.setSelected(false);
			return;
		}
		
	}
}
