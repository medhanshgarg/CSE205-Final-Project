import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddItemPop extends JFrame implements ActionListener {
	
	JButton addButton, cancelButton;
	JTextField nameText, qtyText, tagsText, priceText;
	JLabel nameLabel, qtyLabel, tagsLabel, tagsExampleLabel, priceLabel;
	ItemModification im;
	Container c;
	
	AddItemPop(ItemModification im) {
		this.im = im;
		setTitle("Add Item");
		setSize(250,300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		c=getContentPane();
		c.setLayout(null);
		
		nameLabel = new JLabel("Item Name:");
		nameLabel.setBounds(20, 40, 100, 20);
		c.add(nameLabel);
		
		nameText = new JTextField();
		nameText.setBounds(120, 40, 100, 20);
		c.add(nameText);
		
		qtyLabel = new JLabel("Item Quantity:");
		qtyLabel.setBounds(20, 80, 100, 20);
		c.add(qtyLabel);
		
		qtyText = new JTextField();
		qtyText.setBounds(120, 80, 100, 20);
		c.add(qtyText);
		
		priceLabel = new JLabel("Item Price:          $");
		priceLabel.setBounds(20, 120, 100, 20);
		c.add(priceLabel);
		
		priceText = new JTextField();
		priceText.setBounds(120, 120, 100, 20);
		c.add(priceText);
		
		tagsLabel = new JLabel("Item Tags:");
		tagsLabel.setBounds(20, 160, 100, 20);
		c.add(tagsLabel);
		
		tagsText = new JTextField();
		tagsText.setBounds(120, 160, 100, 20);
		c.add(tagsText);
		
		tagsExampleLabel = new JLabel("Example: tag1 tag2 tag3");
		tagsExampleLabel.setBounds(35, 180, 200, 20);
		c.add(tagsExampleLabel);
		
		addButton = new JButton("Add Item");
		addButton.setBounds(20,220,90,30);
		addButton.addActionListener(this);
		c.add(addButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(120,220,90,30);
		cancelButton.addActionListener(this);
		c.add(cancelButton);
		
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			
			try {
			Item item = new Item();
			item.setTags(im.getSearchItems(tagsText.getText()));
			item.setName(nameText.getText());
			item.setPrice(Double.parseDouble(priceText.getText()));
			item.setQuantity(Integer.parseInt(qtyText.getText()));
			item.setId(im.getBankLength() + 1);
			
			dispose();
			im.addItemToBank(item);
			im.setSearchText(item.getName());
			im.search(item.getName());
			} catch (Exception E) {
				addButton.setText("Try Again!");
			}
			return;
		}
		
		dispose();
		
	}
	

}
