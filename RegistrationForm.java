package practice01;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Myframe extends JFrame implements ActionListener {
	
	JLabel label1, label2, label3, label4, label5;
	JTextField t1,t2;
	JRadioButton male,female;
	JComboBox day,month,year;
	JTextArea ta1;
	JCheckBox terms;
	JButton submit;
	JLabel msg;
	
	
	Myframe() {
		setTitle("Registration Form");
		setSize(700,500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container c=getContentPane();
		c.setLayout(null);
		
		label1=new JLabel("Name");
		label1.setBounds(20,50,100,20);
		c.add(label1);
		
		t1=new JTextField();
		t1.setBounds(130,50,100,20);
		c.add(t1);
		
		label2=new JLabel("Phone Number");
		label2.setBounds(20,100,100,20);
		c.add(label2);
		
		t2=new JTextField();
		t2.setBounds(130,100,100,20);
		c.add(t2);
		
		label3=new JLabel("Gender");
		label3.setBounds(20,150,100,20);
		c.add(label3);
		
		male=new JRadioButton("Male");
		female=new JRadioButton("Female");
		male.setBounds(130,150,80,20);
		female.setBounds(220,150,80,20);
		//male.setSelected(true);
		c.add(male);
		c.add(female);
		
		ButtonGroup gen=new ButtonGroup();
		gen.add(male);
		gen.add(female);
		
		label4=new JLabel("DOB");
		label4.setBounds(20,200,100,20);
		c.add(label4);
		
		String days[]= {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
		String months[] = {"jan","feb","march","april","may","jun","jul","Aug","Sep","Oct","Nov","Dec"};
		String years[] = {"1992","1993","1994","1995","1996","1997","1998","1999","2000"};
		
		
		day=new JComboBox(days);
		month=new JComboBox(months);
		year=new JComboBox(years);
		
		day.setBounds(130,200,50,20);
		month.setBounds(190,200,50,20);
		year.setBounds(250,200,60,20);
		
		c.add(day);
		c.add(month);
		c.add(year);
		
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
