package practice01;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class RegistrationForm extends JFrame implements ActionListener {
	
	JLabel label1, label2, label3, label4, label5;
	JTextField t1,t2,t3,t4;
	JRadioButton male,female;
	JComboBox day,month,year;
	JTextArea ta1;
	JCheckBox terms;
	JButton submit;
	JLabel msg;
	
	
	RegistrationForm() {
		setTitle("Registration Form");
		setSize(700,500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container c=getContentPane();
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
		
		label3=new JLabel("First and Last name");
		label3.setBounds(20,150,100,20);
		c.add(label3);
		
		t3=new JTextField();
		t3.setBounds(130,150,150,20);
		c.add(t3);
		
		label4=new JLabel("Email Adress");
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
		c.add(submit);
		
		msg=new JLabel();
		msg.setBounds(20,400,250,20);
		c.add(msg);
		
		
		setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(terms.isSelected()) {
			submit.setText("REGISTRATION SUCCESSFUL!!");
			
			
		}
		// TODO Auto-generated method stub
		
	}
}



public class RegistrationForm {
	public static void main(String args[]) {
		
		Myframe frame = new Myframe();
		
		
		
	}

}
